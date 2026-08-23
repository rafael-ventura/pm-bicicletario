using PmBicicletario.Application.Dtos;
using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;

namespace PmBicicletario.Application.Services;

public interface IDockService
{
    Task AddDockToNetworkAsync(AddDockToNetworkDto dto);
    Task RemoveDockFromNetworkAsync(RemoveDockFromNetworkDto dto);
    Dock RegisterDock(NewDockDto dto);
    Dock UpdateDock(int dockId, NewDockDto dto);
    void DeleteDock(int dockId);
    List<Dock> ListDocks();
    Dock GetDock(int dockId);
    Bike GetBikeAtDock(int dockId);
    Dock LockDock(int dockId, int? bikeId);
    Dock UnlockDock(int dockId, int? bikeId);
    Dock ChangeDockStatus(int dockId, DockStatus action);
}
