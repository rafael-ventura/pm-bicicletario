using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.DependencyInjection.Extensions;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Clients;

namespace PmBicicletario.Tests.Integration;

/// <summary>
/// Swaps the HTTP clients for the peer services (rental, external) with in-memory fakes,
/// so integration tests exercise the full pipeline without depending on those services being up.
/// </summary>
public class CustomWebApplicationFactory : WebApplicationFactory<Program>
{
    protected override void ConfigureWebHost(Microsoft.AspNetCore.Hosting.IWebHostBuilder builder)
    {
        builder.ConfigureServices(services =>
        {
            services.RemoveAll<IEmployeeClient>();
            services.AddSingleton<IEmployeeClient>(new FakeEmployeeClient());

            services.RemoveAll<IEmailClient>();
            services.AddSingleton<IEmailClient>(new FakeEmailClient());
        });
    }

    private class FakeEmployeeClient : IEmployeeClient
    {
        public Task<Employee?> GetByIdAsync(int employeeId) =>
            Task.FromResult<Employee?>(new Employee { Id = employeeId, Name = "Test Employee", Email = "employee@example.com" });
    }

    private class FakeEmailClient : IEmailClient
    {
        public Task<bool> SendAsync(string to, string subject, string body) => Task.FromResult(true);
    }
}
