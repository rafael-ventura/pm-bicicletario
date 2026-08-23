using PmBicicletario.Domain.Enums;

namespace PmBicicletario.Application.Dtos;

public class NewDockDto
{
    public int? Number { get; set; }
    public string? Location { get; set; }
    public string? ManufactureYear { get; set; }
    public string? Model { get; set; }
    public DockStatus? Status { get; set; }
}
