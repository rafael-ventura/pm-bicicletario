using PmBicicletario.Application.Dtos;
using PmBicicletario.Domain.Models;

namespace PmBicicletario.Application.Services;

public interface IBikeService
{
    Task AddBikeToNetworkAsync(AddBikeToNetworkDto dto);
    Task RemoveBikeFromNetworkAsync(RemoveBikeFromNetworkDto dto);
    Bike RegisterBike(NewBikeDto dto);
    Bike UpdateBike(int bikeId, NewBikeDto dto);
    void DeleteBike(int bikeId);
    List<Bike> ListBikes();
    Bike GetBike(int bikeId);
    Bike ChangeBikeStatus(int bikeId, string action);
}
