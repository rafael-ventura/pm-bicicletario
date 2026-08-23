using Microsoft.AspNetCore.Mvc;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Services;
using static PmBicicletario.Domain.Constants.Messages;

namespace PmBicicletario.Web.Controllers;

[ApiController]
[Route("api/bikes")]
public class BikesController : ControllerBase
{
    private readonly IBikeService _bikeService;

    public BikesController(IBikeService bikeService)
    {
        _bikeService = bikeService;
    }

    [HttpGet]
    public IActionResult ListBikes() => Ok(_bikeService.ListBikes());

    [HttpPost]
    public IActionResult RegisterBike([FromBody] NewBikeDto dto)
    {
        var bike = _bikeService.RegisterBike(dto);
        Response.Headers.Append("Message", DataRegistered);
        return Ok(bike);
    }

    [HttpPost("add-to-network")]
    public async Task<IActionResult> AddToNetwork([FromBody] AddBikeToNetworkDto dto)
    {
        await _bikeService.AddBikeToNetworkAsync(dto);
        return Ok(DataRegistered);
    }

    [HttpPost("remove-from-network")]
    public async Task<IActionResult> RemoveFromNetwork([FromBody] RemoveBikeFromNetworkDto dto)
    {
        await _bikeService.RemoveBikeFromNetworkAsync(dto);
        return Ok(DataRegistered);
    }

    [HttpGet("{bikeId:int}")]
    public IActionResult GetBike(int bikeId)
    {
        var bike = _bikeService.GetBike(bikeId);
        Response.Headers.Append("Message", DataRegistered);
        return Ok(bike);
    }

    [HttpPut("{bikeId:int}")]
    public IActionResult UpdateBike(int bikeId, [FromBody] NewBikeDto dto)
    {
        var bike = _bikeService.UpdateBike(bikeId, dto);
        Response.Headers.Append("Message", DataRegistered);
        return Ok(bike);
    }

    [HttpDelete("{bikeId:int}")]
    public IActionResult DeleteBike(int bikeId)
    {
        _bikeService.DeleteBike(bikeId);
        return Ok("Data removed");
    }

    [HttpPost("{bikeId:int}/status/{action}")]
    public IActionResult ChangeStatus(int bikeId, string action)
    {
        var bike = _bikeService.ChangeBikeStatus(bikeId, action);
        Response.Headers.Append("Message", ActionSuccessful);
        return Ok(bike);
    }
}
