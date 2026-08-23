using System.Net;
using System.Net.Http.Json;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Domain.Models;
using Xunit;

namespace PmBicicletario.Tests.Integration.Controllers;

public class BikesControllerTests : IClassFixture<CustomWebApplicationFactory>
{
    private readonly HttpClient _client;

    public BikesControllerTests(CustomWebApplicationFactory factory)
    {
        _client = factory.CreateClient();
    }

    [Fact]
    public async Task RegisterBike_ReturnsOk_AndTheCreatedBike()
    {
        var dto = new NewBikeDto { Brand = "Caloi", Model = "10", Year = "2023", Number = 1 };

        var response = await _client.PostAsJsonAsync("/api/bikes", dto);

        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
        var bike = await response.Content.ReadFromJsonAsync<Bike>();
        Assert.NotNull(bike);
        Assert.Equal("Caloi", bike!.Brand);
    }

    [Fact]
    public async Task RegisterBike_ReturnsUnprocessableEntity_WhenDataIsInvalid()
    {
        var response = await _client.PostAsJsonAsync("/api/bikes", new NewBikeDto());

        Assert.Equal(HttpStatusCode.UnprocessableEntity, response.StatusCode);
    }

    [Fact]
    public async Task GetBike_ReturnsNotFound_ForUnknownId()
    {
        var response = await _client.GetAsync("/api/bikes/999999");

        Assert.Equal(HttpStatusCode.NotFound, response.StatusCode);
    }

    [Fact]
    public async Task ListBikes_ReturnsOk()
    {
        var response = await _client.GetAsync("/api/bikes");

        Assert.Equal(HttpStatusCode.OK, response.StatusCode);
    }
}
