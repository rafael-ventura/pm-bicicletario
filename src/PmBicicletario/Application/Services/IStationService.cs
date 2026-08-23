using PmBicicletario.Application.Dtos;
using PmBicicletario.Domain.Models;

namespace PmBicicletario.Application.Services;

public interface IStationService
{
    List<Station> ListStations();
    Station RegisterStation(NewStationDto dto);
    Station UpdateStation(int stationId, NewStationDto dto);
    void DeleteStation(int stationId);
    List<Dock> ListDocksAtStation(int stationId);
    List<Bike> ListBikesAtStation(int stationId);
}
