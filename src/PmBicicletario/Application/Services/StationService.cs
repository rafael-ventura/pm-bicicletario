using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Domain.Constants;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;

namespace PmBicicletario.Application.Services;

public class StationService : IStationService
{
    private readonly IStationRepository _stationRepository;
    private readonly IDockRepository _dockRepository;

    public StationService(IStationRepository stationRepository, IDockRepository dockRepository)
    {
        _stationRepository = stationRepository;
        _dockRepository = dockRepository;
    }

    public List<Station> ListStations() => _stationRepository.FindAll();

    public Station RegisterStation(NewStationDto dto)
    {
        ValidateStation(dto);
        var station = new Station { Location = dto.Location!, Description = dto.Description! };
        return _stationRepository.Save(station);
    }

    public Station UpdateStation(int stationId, NewStationDto dto)
    {
        var station = GetStationOrThrow(stationId);
        ValidateStation(dto);
        station.Location = dto.Location!;
        station.Description = dto.Description!;
        return _stationRepository.Save(station);
    }

    public void DeleteStation(int stationId)
    {
        var station = GetStationOrThrow(stationId);
        var docks = ListDocksAtStation(station.Id);
        if (docks.Count > 0)
        {
            throw new DomainInvalidDataException(Messages.StationHasDocks);
        }

        _stationRepository.DeleteById(stationId);
    }

    public List<Dock> ListDocksAtStation(int stationId)
    {
        var station = GetStationOrThrow(stationId);
        return _dockRepository.FindByLocation(station.Location);
    }

    public List<Bike> ListBikesAtStation(int stationId)
    {
        var bikes = ListDocksAtStation(stationId)
            .Where(dock => dock.Bike is not null)
            .Select(dock => dock.Bike!)
            .ToList();

        if (bikes.Count == 0)
        {
            throw new ResourceNotFoundException(Messages.NotFound);
        }

        return bikes;
    }

    private Station GetStationOrThrow(int stationId) =>
        _stationRepository.FindById(stationId) ?? throw new ResourceNotFoundException(Messages.StationNotFound);

    private static void ValidateStation(NewStationDto dto)
    {
        if (string.IsNullOrEmpty(dto.Location) || string.IsNullOrEmpty(dto.Description))
        {
            throw new DomainInvalidDataException(Messages.InvalidData);
        }
    }
}
