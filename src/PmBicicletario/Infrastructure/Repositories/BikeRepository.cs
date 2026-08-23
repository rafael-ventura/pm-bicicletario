using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Repositories;

/// <summary>
/// In-memory repository, matching the simplified scope of the original assignment
/// (no real database was involved for this service).
/// </summary>
public class BikeRepository : IBikeRepository
{
    private readonly List<Bike> _bikes = new();
    private int _counter;

    public List<Bike> FindAll() => new(_bikes);

    public Bike? FindById(int id) => _bikes.FirstOrDefault(b => b.Id == id);

    public Bike Save(Bike bike)
    {
        if (bike.Id == 0)
        {
            bike.Id = ++_counter;
            _bikes.Add(bike);
            return bike;
        }

        var index = _bikes.FindIndex(b => b.Id == bike.Id);
        if (index >= 0)
        {
            _bikes[index] = bike;
        }

        return bike;
    }

    public void DeleteById(int id) => _bikes.RemoveAll(b => b.Id == id);

    public bool ExistsById(int id) => _bikes.Any(b => b.Id == id);
}
