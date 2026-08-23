using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Application.Services;
using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;
using Xunit;

namespace PmBicicletario.Tests.Unit.Services;

public class StationServiceTests
{
    private readonly IStationRepository _stationRepository = new StationRepository();
    private readonly IDockRepository _dockRepository = new DockRepository();
    private readonly StationService _sut;

    public StationServiceTests()
    {
        _sut = new StationService(_stationRepository, _dockRepository);
    }

    [Fact]
    public void RegisterStation_Throws_WhenLocationIsMissing()
    {
        Assert.Throws<DomainInvalidDataException>(() => _sut.RegisterStation(new NewStationDto { Description = "desc" }));
    }

    [Fact]
    public void ListDocksAtStation_MatchesByLocation()
    {
        var station = _stationRepository.Save(new Station { Location = "Central", Description = "desc" });
        _dockRepository.Save(new Dock { Location = "Central", Status = DockStatus.Free });
        _dockRepository.Save(new Dock { Location = "North", Status = DockStatus.Free });

        var docks = _sut.ListDocksAtStation(station.Id);

        Assert.Single(docks);
    }

    [Fact]
    public void ListBikesAtStation_Throws_WhenNoDockHasABike()
    {
        var station = _stationRepository.Save(new Station { Location = "Central", Description = "desc" });
        _dockRepository.Save(new Dock { Location = "Central", Status = DockStatus.Free });

        Assert.Throws<ResourceNotFoundException>(() => _sut.ListBikesAtStation(station.Id));
    }

    [Fact]
    public void ListBikesAtStation_ReturnsOnlyDockedBikes()
    {
        var station = _stationRepository.Save(new Station { Location = "Central", Description = "desc" });
        var bike = new Bike { Id = 1, Status = BikeStatus.Available };
        _dockRepository.Save(new Dock { Location = "Central", Status = DockStatus.Occupied, Bike = bike });
        _dockRepository.Save(new Dock { Location = "Central", Status = DockStatus.Free });

        var bikes = _sut.ListBikesAtStation(station.Id);

        Assert.Single(bikes);
    }

    [Fact]
    public void DeleteStation_Throws_WhenStationStillHasDocks()
    {
        var station = _stationRepository.Save(new Station { Location = "Central", Description = "desc" });
        _dockRepository.Save(new Dock { Location = "Central", Status = DockStatus.Free });

        Assert.Throws<DomainInvalidDataException>(() => _sut.DeleteStation(station.Id));
    }
}
