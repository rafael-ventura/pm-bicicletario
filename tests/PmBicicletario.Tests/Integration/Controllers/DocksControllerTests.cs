using System.Net;
using System.Net.Http.Json;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Domain.Models;
using Xunit;

namespace PmBicicletario.Tests.Integration.Controllers;

public class DocksControllerTests : IClassFixture<CustomWebApplicationFactory>
{
    private readonly HttpClient _client;

    public DocksControllerTests(CustomWebApplicationFactory factory)
    {
        _client = factory.CreateClient();
    }

    [Fact]
    public async Task RegisterDock_ReturnsOk_AndTheCreatedDock()
    {
        var dto = new NewDockDto { Number = 1, Location = "Central", ManufactureYear = "2023", Model = "T1" };

        var response = await _client.PostAsJsonAsync("/api/docks", dto);

        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
        var dock = await response.Content.ReadFromJsonAsync<Dock>();
        Assert.NotNull(dock);
        Assert.Equal("Central", dock!.Location);
    }

    [Fact]
    public async Task AddToNetwork_ReturnsUnprocessableEntity_WhenDockDoesNotExist()
    {
        var dto = new AddDockToNetworkDto { DockId = 999999, EmployeeId = 1 };

        var response = await _client.PostAsJsonAsync("/api/docks/add-to-network", dto);

        Assert.Equal(HttpStatusCode.NotFound, response.StatusCode);
    }

    [Fact]
    public async Task GetDock_ReturnsNotFound_ForUnknownId()
    {
        var response = await _client.GetAsync("/api/docks/999999");

        Assert.Equal(HttpStatusCode.NotFound, response.StatusCode);
    }
}
