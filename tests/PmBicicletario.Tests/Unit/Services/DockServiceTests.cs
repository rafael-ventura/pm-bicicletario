using Moq;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Application.Services;
using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;
using Xunit;

namespace PmBicicletario.Tests.Unit.Services;

public class DockServiceTests
{
    private readonly IDockRepository _dockRepository = new DockRepository();
    private readonly IBikeRepository _bikeRepository = new BikeRepository();
    private readonly Mock<IEmailService> _emailService = new();
    private readonly Mock<IEmployeeService> _employeeService = new();
    private readonly DockService _sut;

    public DockServiceTests()
    {
        _sut = new DockService(_dockRepository, _bikeRepository, _emailService.Object, _employeeService.Object);
    }

    [Fact]
    public async Task AddDockToNetworkAsync_MakesTheDockFree()
    {
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.New });

        await _sut.AddDockToNetworkAsync(new AddDockToNetworkDto { DockId = dock.Id, EmployeeId = 1 });

        Assert.Equal(DockStatus.Free, dock.Status);
        _emailService.Verify(s => s.SendDockNotificationAsync(1, dock, "Addition"), Times.Once);
    }

    [Fact]
    public async Task AddDockToNetworkAsync_Throws_WhenDockIsOccupied()
    {
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.Occupied });

        await Assert.ThrowsAsync<DomainInvalidDataException>(() =>
            _sut.AddDockToNetworkAsync(new AddDockToNetworkDto { DockId = dock.Id, EmployeeId = 1 }));
    }

    [Fact]
    public async Task RemoveDockFromNetworkAsync_Throws_WhenDockStillHasABike()
    {
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.Occupied });

        await Assert.ThrowsAsync<DomainInvalidDataException>(() =>
            _sut.RemoveDockFromNetworkAsync(new RemoveDockFromNetworkDto
            {
                DockId = dock.Id,
                EmployeeId = 1,
                RepairActionStatus = RepairActionStatus.InRepair
            }));
    }

    [Fact]
    public void LockDock_AttachesTheBikeAndOccupiesTheDock()
    {
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.Free });
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.New });

        var result = _sut.LockDock(dock.Id, bike.Id);

        Assert.Equal(DockStatus.Occupied, result.Status);
        Assert.Equal(bike.Id, result.Bike!.Id);
        Assert.Equal(BikeStatus.Available, bike.Status);
    }

    [Fact]
    public void LockDock_Throws_WhenNoBikeIdIsGiven()
    {
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.Free });

        Assert.Throws<DomainInvalidDataException>(() => _sut.LockDock(dock.Id, null));
    }

    [Fact]
    public void UnlockDock_ReleasesTheBikeAndFreesTheDock()
    {
        var bike = _bikeRepository.Save(new Bike { Status = BikeStatus.Available });
        var dock = _dockRepository.Save(new Dock { Bike = bike, Status = DockStatus.Occupied });

        var result = _sut.UnlockDock(dock.Id, bike.Id);

        Assert.Equal(DockStatus.Free, result.Status);
        Assert.Null(result.Bike);
        Assert.Equal(BikeStatus.InUse, bike.Status);
    }

    [Fact]
    public void GetBikeAtDock_Throws_WhenDockHasNoBike()
    {
        var dock = _dockRepository.Save(new Dock { Status = DockStatus.Free });

        Assert.Throws<DomainInvalidDataException>(() => _sut.GetBikeAtDock(dock.Id));
    }
}
