using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Repositories;

public class StationRepository : IStationRepository
{
    private readonly List<Station> _stations = new();
    private int _counter;

    public List<Station> FindAll() => new(_stations);

    public Station? FindById(int id) => _stations.FirstOrDefault(s => s.Id == id);

    public Station Save(Station station)
    {
        if (station.Id == 0)
        {
            station.Id = ++_counter;
            _stations.Add(station);
            return station;
        }

        var index = _stations.FindIndex(s => s.Id == station.Id);
        if (index >= 0)
        {
            _stations[index] = station;
        }

        return station;
    }

    public void DeleteById(int id) => _stations.RemoveAll(s => s.Id == id);

    public bool ExistsById(int id) => _stations.Any(s => s.Id == id);
}
