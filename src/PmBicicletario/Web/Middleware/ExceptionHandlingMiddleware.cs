using System.Net;
using System.Text.Json;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Domain.Models;

namespace PmBicicletario.Web.Middleware;

public class ExceptionHandlingMiddleware
{
    private readonly RequestDelegate _next;

    public ExceptionHandlingMiddleware(RequestDelegate next)
    {
        _next = next;
    }

    public async Task InvokeAsync(HttpContext context)
    {
        try
        {
            await _next(context);
        }
        catch (ResourceNotFoundException e)
        {
            await WriteError(context, HttpStatusCode.NotFound, e.Message);
        }
        catch (DomainInvalidDataException e)
        {
            await WriteError(context, HttpStatusCode.UnprocessableEntity, e.Message);
        }
        catch (BadRequestException e)
        {
            await WriteError(context, HttpStatusCode.UnprocessableEntity, e.Message);
        }
        catch (Exception e)
        {
            await WriteError(context, HttpStatusCode.InternalServerError, e.Message);
        }
    }

    private static Task WriteError(HttpContext context, HttpStatusCode status, string message)
    {
        var error = new ApiError(((int)status).ToString(), message);
        context.Response.ContentType = "application/json";
        context.Response.StatusCode = (int)status;
        return context.Response.WriteAsync(JsonSerializer.Serialize(error));
    }
}
