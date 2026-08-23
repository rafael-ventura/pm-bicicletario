using PmBicicletario.Application.Exceptions;
using PmBicicletario.Domain.Constants;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Clients;

namespace PmBicicletario.Application.Services;

public class EmployeeService : IEmployeeService
{
    private readonly IEmployeeClient _employeeClient;

    public EmployeeService(IEmployeeClient employeeClient)
    {
        _employeeClient = employeeClient;
    }

    public async Task<Employee> GetAsync(int employeeId)
    {
        var employee = await _employeeClient.GetByIdAsync(employeeId);
        return employee ?? throw new ResourceNotFoundException(Messages.EmployeeNotFound);
    }

    public async Task<bool> IsValidEmployeeAsync(int employeeId)
    {
        var employee = await _employeeClient.GetByIdAsync(employeeId);
        return employee is not null && employee.Id == employeeId;
    }
}
