using Microsoft.AspNetCore.Mvc;
using PmBicicletario.Application.Dtos;
using PmBicicletario.Application.Services;
using PmBicicletario.Domain.Enums;
using static PmBicicletario.Domain.Constants.Messages;

namespace PmBicicletario.Web.Controllers;

[ApiController]
[Route("api/docks")]
public class DocksController : ControllerBase
{
    private readonly IDockService _dockService;

    public DocksController(IDockService dockService)
    {
        _dockService = dockService;
    }

    [HttpPost("add-to-network")]
    public async Task<IActionResult> AddToNetwork([FromBody] AddDockToNetworkDto dto)
    {
        await _dockService.AddDockToNetworkAsync(dto);
        return Ok(DataRegistered);
    }

    [HttpPost("remove-from-network")]
    public async Task<IActionResult> RemoveFromNetwork([FromBody] RemoveDockFromNetworkDto dto)
    {
        await _dockService.RemoveDockFromNetworkAsync(dto);
        return Ok(DataRegistered);
    }

    [HttpGet]
    public IActionResult ListDocks()
    {
        Response.Headers.Append("Message", "OK");
        return Ok(_dockService.ListDocks());
    }

    [HttpPost]
    public IActionResult RegisterDock([FromBody] NewDockDto dto) => Ok(_dockService.RegisterDock(dto));

    [HttpGet("{dockId:int}")]
    public IActionResult GetDock(int dockId)
    {
        var dock = _dockService.GetDock(dockId);
        Response.Headers.Append("Message", DockFound);
        return Ok(dock);
    }

    [HttpPut("{dockId:int}")]
    public IActionResult UpdateDock(int dockId, [FromBody] NewDockDto dto)
    {
        var dock = _dockService.UpdateDock(dockId, dto);
        Response.Headers.Append("Message", DataRegistered);
        return Ok(dock);
    }

    [HttpDelete("{dockId:int}")]
    public IActionResult DeleteDock(int dockId)
    {
        _dockService.DeleteDock(dockId);
        return Ok(DockRemoved);
    }

    [HttpGet("{dockId:int}/bike")]
    public IActionResult GetBikeAtDock(int dockId)
    {
        var bike = _dockService.GetBikeAtDock(dockId);
        Response.Headers.Append("Message", DockFound);
        return Ok(bike);
    }

    [HttpPost("{dockId:int}/lock")]
    public IActionResult Lock(int dockId, [FromBody] LockDockDto dto)
    {
        var dock = _dockService.LockDock(dockId, dto.BikeId);
        Response.Headers.Append("Message", ActionSuccessful);
        return Ok(dock);
    }

    [HttpPost("{dockId:int}/unlock")]
    public IActionResult Unlock(int dockId, [FromBody] LockDockDto dto)
    {
        var dock = _dockService.UnlockDock(dockId, dto.BikeId);
        Response.Headers.Append("Message", ActionSuccessful);
        return Ok(dock);
    }

    [HttpPost("{dockId:int}/status/{action}")]
    public IActionResult ChangeStatus(int dockId, DockStatus action)
    {
        var dock = _dockService.ChangeDockStatus(dockId, action);
        Response.Headers.Append("Message", ActionSuccessful);
        return Ok(dock);
    }
}
