using PmBicicletario.Domain.Enums;

namespace PmBicicletario.Domain.Models;

public class Dock
{
    public int Id { get; set; }
    public Bike? Bike { get; set; }
    public int Number { get; set; }
    public string Location { get; set; } = string.Empty;
    public string ManufactureYear { get; set; } = string.Empty;
    public string Model { get; set; } = string.Empty;
    public DockStatus Status { get; set; }
    public DateTime? StationJoinedAt { get; set; }
    public DateTime? StationLeftAt { get; set; }
    public int? LastOperationEmployeeId { get; set; }
}
