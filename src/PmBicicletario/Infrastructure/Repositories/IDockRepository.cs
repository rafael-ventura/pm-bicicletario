using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Repositories;

public interface IDockRepository
{
    List<Dock> FindAll();
    Dock? FindById(int id);
    Dock Save(Dock dock);
    void DeleteById(int id);
    List<Dock> FindByLocation(string location);
    bool ExistsByBikeId(int bikeId);
}
