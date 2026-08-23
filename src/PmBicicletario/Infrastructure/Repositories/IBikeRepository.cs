using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Repositories;

public interface IBikeRepository
{
    List<Bike> FindAll();
    Bike? FindById(int id);
    Bike Save(Bike bike);
    void DeleteById(int id);
    bool ExistsById(int id);
}
