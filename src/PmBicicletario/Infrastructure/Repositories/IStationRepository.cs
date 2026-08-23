using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Repositories;

public interface IStationRepository
{
    List<Station> FindAll();
    Station? FindById(int id);
    Station Save(Station station);
    void DeleteById(int id);
    bool ExistsById(int id);
}
