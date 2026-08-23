using PmBicicletario.Domain.Enums;

namespace PmBicicletario.Domain.Models;

public class Bike
{
    public int Id { get; set; }
    public string Brand { get; set; } = string.Empty;
    public string Model { get; set; } = string.Empty;
    public string Year { get; set; } = string.Empty;
    public int Number { get; set; }
    public BikeStatus Status { get; set; }
    public DateTime? DockedAt { get; set; }
    public DateTime? RemovedAt { get; set; }
    public int? LastOperationEmployeeId { get; set; }
}
