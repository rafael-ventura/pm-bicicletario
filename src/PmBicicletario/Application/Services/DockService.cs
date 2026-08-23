using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Domain.Constants;
using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;

namespace PmBicicletario.Application.Services;

public class DockService : IDockService
{
    private readonly IDockRepository _dockRepository;
    private readonly IBikeRepository _bikeRepository;
    private readonly IEmailService _emailService;
    private readonly IEmployeeService _employeeService;

    public DockService(
        IDockRepository dockRepository,
        IBikeRepository bikeRepository,
        IEmailService emailService,
        IEmployeeService employeeService)
    {
        _dockRepository = dockRepository;
        _bikeRepository = bikeRepository;
        _emailService = emailService;
        _employeeService = employeeService;
    }

    public async Task AddDockToNetworkAsync(AddDockToNetworkDto dto)
    {
        var dock = GetDockOrThrow(dto.DockId);

        if (dock.Status != DockStatus.New && dock.Status != DockStatus.InRepair)
        {
            throw new DomainInvalidDataException(Messages.DockNotAvailable);
        }

        if (dock.Status == DockStatus.InRepair)
        {
            if (!await _employeeService.IsValidEmployeeAsync(dto.EmployeeId))
            {
                throw new DomainInvalidDataException(Messages.InvalidEmployee);
            }

            if (dock.LastOperationEmployeeId != dto.EmployeeId)
            {
                throw new DomainInvalidDataException(Messages.EmployeeMismatch);
            }
        }

        dock.Status = DockStatus.Free;
        dock.StationJoinedAt = DateTime.UtcNow;
        dock.LastOperationEmployeeId = dto.EmployeeId;
        _dockRepository.Save(dock);

        await _emailService.SendDockNotificationAsync(dto.EmployeeId, dock, "Addition");
    }

    public async Task RemoveDockFromNetworkAsync(RemoveDockFromNetworkDto dto)
    {
        var dock = GetDockOrThrow(dto.DockId);

        if (DockHasBike(dock))
        {
            throw new DomainInvalidDataException(Messages.DockHasBike);
        }

        if (dto.RepairActionStatus is null)
        {
            throw new DomainInvalidDataException(Messages.InvalidRepairActionStatus);
        }

        dock.LastOperationEmployeeId = dto.EmployeeId;
        dock.StationLeftAt = DateTime.UtcNow;
        dock.Status = dto.RepairActionStatus == RepairActionStatus.InRepair
            ? DockStatus.InRepair
            : DockStatus.Retired;
        _dockRepository.Save(dock);

        await _emailService.SendDockNotificationAsync(dto.EmployeeId, dock, "Removal");
    }

    public Dock RegisterDock(NewDockDto dto)
    {
        ValidateNewDock(dto);
        var dock = new Dock
        {
            Number = dto.Number!.Value,
            Location = dto.Location!,
            ManufactureYear = dto.ManufactureYear!,
            Model = dto.Model!,
            Status = DockStatus.New
        };
        return _dockRepository.Save(dock);
    }

    public Dock UpdateDock(int dockId, NewDockDto dto)
    {
        var dock = GetDockOrThrow(dockId);
        ValidateNewDock(dto);
        dock.Number = dto.Number!.Value;
        dock.Model = dto.Model!;
        dock.ManufactureYear = dto.ManufactureYear!;
        if (dto.Status.HasValue)
        {
            dock.Status = dto.Status.Value;
        }
        return _dockRepository.Save(dock);
    }

    public void DeleteDock(int dockId)
    {
        var dock = GetDockOrThrow(dockId);
        if (DockHasBike(dock))
        {
            throw new DomainInvalidDataException(Messages.InvalidData);
        }

        dock.Status = DockStatus.Deleted;
        _dockRepository.DeleteById(dockId);
    }

    public List<Dock> ListDocks() => _dockRepository.FindAll();

    public Dock GetDock(int dockId) => GetDockOrThrow(dockId);

    public Bike GetBikeAtDock(int dockId)
    {
        var dock = _dockRepository.FindById(dockId) ?? throw new ResourceNotFoundException(Messages.InvalidDockId);
        return dock.Bike ?? throw new DomainInvalidDataException(Messages.BikeNotFound);
    }

    public Dock LockDock(int dockId, int? bikeId)
    {
        var dock = GetDockOrThrow(dockId);

        if (bikeId is null)
        {
            throw new DomainInvalidDataException(Messages.InvalidLockData);
        }

        var bike = _bikeRepository.FindById(bikeId.Value)
                   ?? throw new ResourceNotFoundException(Messages.BikeNotFound);

        bike.Status = BikeStatus.Available;
        dock.Bike = bike;
        dock.Status = DockStatus.Occupied;
        _bikeRepository.Save(bike);
        _dockRepository.Save(dock);

        return dock;
    }

    public Dock UnlockDock(int dockId, int? bikeId)
    {
        var dock = GetDockOrThrow(dockId);

        if (dock.Bike is null || dock.Bike.Id != bikeId)
        {
            throw new DomainInvalidDataException(Messages.InvalidData);
        }

        var bike = dock.Bike;
        bike.Status = BikeStatus.InUse;
        dock.Bike = null;
        dock.Status = DockStatus.Free;
        _bikeRepository.Save(bike);
        _dockRepository.Save(dock);

        return dock;
    }

    public Dock ChangeDockStatus(int dockId, DockStatus action)
    {
        var dock = GetDockOrThrow(dockId);
        dock.Status = action;
        return _dockRepository.Save(dock);
    }

    private Dock GetDockOrThrow(int dockId) =>
        _dockRepository.FindById(dockId) ?? throw new ResourceNotFoundException(Messages.NotFound);

    private static bool DockHasBike(Dock dock) => dock.Status == DockStatus.Occupied;

    private static void ValidateNewDock(NewDockDto dto)
    {
        if (string.IsNullOrEmpty(dto.Location) || string.IsNullOrEmpty(dto.Model)
            || string.IsNullOrEmpty(dto.ManufactureYear))
        {
            throw new DomainInvalidDataException(Messages.InvalidData);
        }
    }
}
