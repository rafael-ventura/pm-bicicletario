using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Clients;

public interface IEmployeeClient
{
    Task<Employee?> GetByIdAsync(int employeeId);
}
