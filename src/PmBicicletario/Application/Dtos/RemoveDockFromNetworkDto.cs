using PmBicicletario.Domain.Enums;

namespace PmBicicletario.Application.Dtos;

public class RemoveDockFromNetworkDto
{
    public int DockId { get; set; }
    public int EmployeeId { get; set; }
    public RepairActionStatus? RepairActionStatus { get; set; }
}
