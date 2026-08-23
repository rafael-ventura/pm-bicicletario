using PmBicicletario.Domain.Models;

namespace PmBicicletario.Application.Services;

public interface IEmployeeService
{
    Task<Employee> GetAsync(int employeeId);
    Task<bool> IsValidEmployeeAsync(int employeeId);
}
