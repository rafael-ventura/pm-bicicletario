using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Domain.Constants;
using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;

namespace PmBicicletario.Application.Services;

public class BikeService : IBikeService
{
    private readonly IBikeRepository _bikeRepository;
    private readonly IDockRepository _dockRepository;
    private readonly IEmailService _emailService;
    private readonly IEmployeeService _employeeService;

    public BikeService(
        IBikeRepository bikeRepository,
        IDockRepository dockRepository,
        IEmailService emailService,
        IEmployeeService employeeService)
    {
        _bikeRepository = bikeRepository;
        _dockRepository = dockRepository;
        _emailService = emailService;
        _employeeService = employeeService;
    }

    public async Task AddBikeToNetworkAsync(AddBikeToNetworkDto dto)
    {
        var bike = GetBikeOrThrow(dto.BikeId);
        var dock = GetDockOrThrow(dto.DockId);

        ValidateBikeStatusForAddition(bike);
        ValidateDockStatusForAddition(dock);

        if (bike.Status == BikeStatus.InRepair)
        {
            if (!await _employeeService.IsValidEmployeeAsync(dto.EmployeeId))
            {
                throw new DomainInvalidDataException(Messages.InvalidEmployee);
            }

            if (bike.LastOperationEmployeeId != dto.EmployeeId)
            {
                throw new DomainInvalidDataException(Messages.EmployeeMismatch);
            }
        }

        bike.DockedAt = DateTime.UtcNow;
        bike.LastOperationEmployeeId = dto.EmployeeId;
        AssociateBikeWithDock(bike, dock);

        await _emailService.SendBikeNotificationAsync(dto.EmployeeId, bike, dock, "Addition");
    }

    public async Task RemoveBikeFromNetworkAsync(RemoveBikeFromNetworkDto dto)
    {
        var bike = GetBikeOrThrow(dto.BikeId);
        var dock = GetDockOrThrow(dto.DockId);

        ValidateDockStatusForRemoval(dock);
        ValidateBikeStatusForRemoval(bike, dto.RepairActionStatus);

        bike.RemovedAt = DateTime.UtcNow;
        bike.LastOperationEmployeeId = dto.EmployeeId;
        DisassociateBikeFromDock(bike, dock, ResolveStatusAfterRemoval(dto.RepairActionStatus!.Value));

        await _emailService.SendBikeNotificationAsync(dto.EmployeeId, bike, dock, "Removal");
    }

    public Bike RegisterBike(NewBikeDto dto)
    {
        ValidateNewBike(dto);
        var bike = new Bike
        {
            Brand = dto.Brand!,
            Model = dto.Model!,
            Year = dto.Year!,
            Number = dto.Number!.Value,
            Status = BikeStatus.New
        };
        return _bikeRepository.Save(bike);
    }

    public Bike UpdateBike(int bikeId, NewBikeDto dto)
    {
        var bike = GetBikeOrThrow(bikeId);
        bike.Brand = dto.Brand ?? bike.Brand;
        bike.Model = dto.Model ?? bike.Model;
        bike.Year = dto.Year ?? bike.Year;
        bike.Number = dto.Number ?? bike.Number;
        return _bikeRepository.Save(bike);
    }

    public void DeleteBike(int bikeId)
    {
        var bike = GetBikeOrThrow(bikeId);
        if (bike.Status != BikeStatus.Retired || _dockRepository.ExistsByBikeId(bike.Id))
        {
            throw new BadRequestException(Messages.BikeNotRetired);
        }

        _bikeRepository.DeleteById(bikeId);
    }

    public List<Bike> ListBikes() => _bikeRepository.FindAll();

    public Bike GetBike(int bikeId) => GetBikeOrThrow(bikeId);

    public Bike ChangeBikeStatus(int bikeId, string action)
    {
        var bike = GetBikeOrThrow(bikeId);
        bike.Status = action.ToLowerInvariant() switch
        {
            "available" => BikeStatus.Available,
            "in-use" => BikeStatus.InUse,
            "new" => BikeStatus.New,
            "retired" => BikeStatus.Retired,
            "repair-requested" => BikeStatus.RepairRequested,
            "in-repair" => BikeStatus.InRepair,
            _ => throw new DomainInvalidDataException(Messages.InvalidAction)
        };
        return _bikeRepository.Save(bike);
    }

    private Bike GetBikeOrThrow(int bikeId) =>
        _bikeRepository.FindById(bikeId) ?? throw new ResourceNotFoundException(Messages.NotFound);

    private Dock GetDockOrThrow(int dockId) =>
        _dockRepository.FindById(dockId) ?? throw new ResourceNotFoundException(Messages.InvalidDockId);

    private static void ValidateNewBike(NewBikeDto dto)
    {
        if (string.IsNullOrEmpty(dto.Brand) || string.IsNullOrEmpty(dto.Model)
            || dto.Number is null || string.IsNullOrEmpty(dto.Year))
        {
            throw new DomainInvalidDataException(Messages.InvalidData);
        }
    }

    private static void ValidateBikeStatusForAddition(Bike bike)
    {
        if (bike.Status != BikeStatus.New && bike.Status != BikeStatus.InRepair)
        {
            throw new DomainInvalidDataException(Messages.InvalidBikeStatus);
        }
    }

    private static void ValidateDockStatusForAddition(Dock dock)
    {
        if (dock.Status != DockStatus.Free)
        {
            throw new DomainInvalidDataException(Messages.DockNotAvailable);
        }
    }

    private void AssociateBikeWithDock(Bike bike, Dock dock)
    {
        bike.Status = BikeStatus.Available;
        _bikeRepository.Save(bike);

        dock.Status = DockStatus.Occupied;
        dock.Bike = bike;
        _dockRepository.Save(dock);
    }

    private void DisassociateBikeFromDock(Bike bike, Dock dock, BikeStatus statusAfterRemoval)
    {
        bike.Status = statusAfterRemoval;
        _bikeRepository.Save(bike);

        dock.Status = DockStatus.Free;
        dock.Bike = null;
        _dockRepository.Save(dock);
    }

    private static void ValidateDockStatusForRemoval(Dock dock)
    {
        if (dock.Status != DockStatus.Occupied)
        {
            throw new DomainInvalidDataException(Messages.DockNotOccupied);
        }
    }

    private static void ValidateBikeStatusForRemoval(Bike bike, RepairActionStatus? repairActionStatus)
    {
        if (bike.Status != BikeStatus.RepairRequested)
        {
            throw new DomainInvalidDataException(Messages.InvalidBikeStatus);
        }

        if (repairActionStatus is null)
        {
            throw new DomainInvalidDataException(Messages.InvalidAction);
        }
    }

    private static BikeStatus ResolveStatusAfterRemoval(RepairActionStatus repairActionStatus) => repairActionStatus switch
    {
        RepairActionStatus.InRepair => BikeStatus.InRepair,
        RepairActionStatus.Retired => BikeStatus.Retired,
        _ => throw new DomainInvalidDataException(Messages.InvalidAction)
    };
}
