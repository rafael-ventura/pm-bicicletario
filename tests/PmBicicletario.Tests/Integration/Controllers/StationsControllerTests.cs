using System.Net;
using System.Net.Http.Json;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Domain.Models;
using Xunit;

namespace PmBicicletario.Tests.Integration.Controllers;

public class StationsControllerTests : IClassFixture<CustomWebApplicationFactory>
{
    private readonly HttpClient _client;

    public StationsControllerTests(CustomWebApplicationFactory factory)
    {
        _client = factory.CreateClient();
    }

    [Fact]
    public async Task RegisterStation_ReturnsOk_AndTheCreatedStation()
    {
        var dto = new NewStationDto { Location = "Central", Description = "Main square" };

        var response = await _client.PostAsJsonAsync("/api/stations", dto);

        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
        var station = await response.Content.ReadFromJsonAsync<Station>();
        Assert.NotNull(station);
        Assert.Equal("Central", station!.Location);
    }

    [Fact]
    public async Task ListDocksAtStation_ReturnsNotFound_ForUnknownStation()
    {
        var response = await _client.GetAsync("/api/stations/999999/docks");

        Assert.Equal(HttpStatusCode.NotFound, response.StatusCode);
    }

    [Fact]
    public async Task ListStations_ReturnsOk()
    {
        var response = await _client.GetAsync("/api/stations");

        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
    }
}
