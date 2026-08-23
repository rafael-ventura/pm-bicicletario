using System.Net;
using PmBicicletario.Domain.Models;

namespace PmBicicletario.Infrastructure.Clients;

/// <summary>
/// Calls the rental service's employee endpoint. This service owns employee records;
/// equipment only ever reads them to validate who is performing an action.
/// </summary>
public class EmployeeClient : IEmployeeClient
{
    private readonly HttpClient _httpClient;

    public EmployeeClient(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<Employee?> GetByIdAsync(int employeeId)
    {
        var response = await _httpClient.GetAsync($"/api/employee/{employeeId}");

        if (response.StatusCode == HttpStatusCode.NotFound)
        {
            return null;
        }

        response.EnsureSuccessStatusCode();
        return await response.Content.ReadFromJsonAsync<Employee>();
    }
}
