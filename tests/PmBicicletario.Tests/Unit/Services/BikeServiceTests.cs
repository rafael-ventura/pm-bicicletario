using Moq;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Application.Services;
using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;
using Xunit;

namespace PmBicicletario.Tests.Unit.Services;

public class BikeServiceTests
{
    private readonly IBikeRepository _bikeRepository = new BikeRepository();
    private readonly IDockRepository _dockRepository = new DockRepository();
    private readonly Mock<IEmailService> _emailService = new();
    private readonly Mock<IEmployeeService> _employeeService = new();
    private readonly BikeService _sut;

    public BikeServiceTests()
    {
        _sut = new BikeService(_bikeRepository, _dockRepository, _emailService.Object, _employeeService.Object);
    }

    [Fact]
    public void RegisterBike_Throws_WhenRequiredFieldsAreMissing()
    {
        var dto = new NewBikeDto { Brand = "Caloi" };

        Assert.Throws<DomainInvalidDataException>(() => _sut.RegisterBike(dto));
    }

    [Fact]
    public void RegisterBike_SetsStatusToNew()
    {
        var dto = new NewBikeDto { Brand = "Caloi", Model = "10", Year = "2023", Number = 1 };

        var bike = _sut.RegisterBike(dto);

        Assert.Equal(BikeStatus.New, bike.Status);
    }

    [Fact]
    public async Task AddBikeToNetworkAsync_LocksTheBikeIntoTheDock()
    {
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.New, Brand = "Caloi" });
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.Free });

        await _sut.AddBikeToNetworkAsync(new AddBikeToNetworkDto { BikeId = bike.Id, DockId = dock.Id, EmployeeId = 1 });

        Assert.Equal(BikeStatus.Available, bike.Status);
        Assert.Equal(DockStatus.Occupied, dock.Status);
        Assert.Equal(bike.Id, dock.Bike!.Id);
        _emailService.Verify(s => s.SendBikeNotificationAsync(1, bike, dock, "Addition"), Times.Once);
    }

    [Fact]
    public async Task AddBikeToNetworkAsync_Throws_WhenDockIsNotFree()
    {
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.New });
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.Occupied });

        await Assert.ThrowsAsync<DomainInvalidDataException>(() =>
            _sut.AddBikeToNetworkAsync(new AddBikeToNetworkDto { BikeId = bike.Id, DockId = dock.Id, EmployeeId = 1 }));
    }

    [Fact]
    public async Task RemoveBikeFromNetworkAsync_FreesTheDockAndSetsRepairStatus()
    {
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.RepairRequested });
        var dock = _dockRepository.Save(new Dock { Bike = bike, Status = DockStatus.Occupied });

        await _sut.RemoveBikeFromNetworkAsync(new RemoveBikeFromNetworkDto
        {
            BikeId = bike.Id,
            DockId = dock.Id,
            EmployeeId = 1,
            RepairActionStatus = RepairActionStatus.InRepair
        });

        Assert.Equal(BikeStatus.InRepair, bike.Status);
        Assert.Equal(DockStatus.Free, dock.Status);
        Assert.Null(dock.Bike);
    }

    [Fact]
    public void DeleteBike_Throws_WhenBikeIsNotRetired()
    {
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.Available });

        Assert.Throws<BadRequestException>(() => _sut.DeleteBike(bike.Id));
    }

    [Fact]
    public void DeleteBike_Succeeds_WhenBikeIsRetiredAndNotDocked()
    {
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.Retired });

        _sut.DeleteBike(bike.Id);

        Assert.False(_bikeRepository.ExistsById(bike.Id));
    }

    [Fact]
    public void ChangeBikeStatus_Throws_OnUnknownAction()
    {
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.New });

        Assert.Throws<DomainInvalidDataException>(() => _sut.ChangeBikeStatus(bike.Id, "flying"));
    }
}
