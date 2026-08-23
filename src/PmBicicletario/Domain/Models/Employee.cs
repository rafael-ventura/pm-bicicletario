namespace PmBicicletario.Domain.Models;

/// <summary>
/// Employee record as returned by the rental service's employee endpoint.
/// This service only ever reads employees; it never creates or updates them.
/// </summary>
public class Employee
{
    public int Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
    public string Cpf { get; set; } = string.Empty;
    public int Age { get; set; }
    public string Role { get; set; } = string.Empty;
}
