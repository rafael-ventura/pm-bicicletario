using PmBicicletario.Domain.Enums;

namespace PmBicicletario.Application.Dtos;

public class RemoveBikeFromNetworkDto
{
    public int DockId { get; set; }
    public int BikeId { get; set; }
    public int EmployeeId { get; set; }
    public RepairActionStatus? RepairActionStatus { get; set; }
}
