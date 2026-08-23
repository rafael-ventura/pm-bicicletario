using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Repositories;

public class DockRepository : IDockRepository
{
    private readonly List<Dock> _docks = new();
    private int _counter;

    public List<Dock> FindAll() => new(_docks);

    public Dock? FindById(int id) => _docks.FirstOrDefault(d => d.Id == id);

    public Dock Save(Dock dock)
    {
        if (dock.Id == 0)
        {
            dock.Id = ++_counter;
            _docks.Add(dock);
            return dock;
        }

        var index = _docks.FindIndex(d => d.Id == dock.Id);
        if (index >= 0)
        {
            _docks[index] = dock;
        }

        return dock;
    }

    public void DeleteById(int id) => _docks.RemoveAll(d => d.Id == id);

    /// <summary>
    /// Docks are matched to a station by comparing this free-text location field —
    /// there's no real foreign key between them, mirroring how the original assignment modeled it.
    /// </summary>
    public List<Dock> FindByLocation(string location) =>
        _docks.Where(d => d.Location == location).ToList();

    public bool ExistsByBikeId(int bikeId) => _docks.Any(d => d.Bike?.Id == bikeId);
}
