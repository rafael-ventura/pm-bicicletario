using Microsoft.AspNetCore.Mvc;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Services;
using static PmBicicletario.Domain.Constants.Messages;

namespace PmBicicletario.Web.Controllers;

[ApiController]
[Route("api/stations")]
public class StationsController : ControllerBase
{
    private readonly IStationService _stationService;

    public StationsController(IStationService stationService)
    {
        _stationService = stationService;
    }

    [HttpGet]
    public IActionResult ListStations() => Ok(_stationService.ListStations());

    [HttpPost]
    public IActionResult RegisterStation([FromBody] NewStationDto dto)
    {
        var station = _stationService.RegisterStation(dto);
        Response.Headers.Append("Message", DataRegistered);
        return Ok(station);
    }

    [HttpPut("{stationId:int}")]
    public IActionResult UpdateStation(int stationId, [FromBody] NewStationDto dto)
    {
        var station = _stationService.UpdateStation(stationId, dto);
        Response.Headers.Append("Message", DataRegistered);
        return Ok(station);
    }

    [HttpDelete("{stationId:int}")]
    public IActionResult DeleteStation(int stationId)
    {
        _stationService.DeleteStation(stationId);
        return Ok("Station removed");
    }

    [HttpGet("{stationId:int}/docks")]
    public IActionResult ListDocks(int stationId) => Ok(_stationService.ListDocksAtStation(stationId));

    [HttpGet("{stationId:int}/bikes")]
    public IActionResult ListBikes(int stationId) => Ok(_stationService.ListBikesAtStation(stationId));
}
