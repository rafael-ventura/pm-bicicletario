using PmBicicletario.Application.Exceptions;
using PmBicicletario.Domain.Constants;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Clients;

namespace PmBicicletario.Application.Services;

public class EmailService : IEmailService
{
    private readonly IEmailClient _emailClient;
    private readonly IEmployeeService _employeeService;

    public EmailService(IEmailClient emailClient, IEmployeeService employeeService)
    {
        _emailClient = emailClient;
        _employeeService = employeeService;
    }

    public async Task SendToEmployeeAsync(int employeeId, string subject, string body)
    {
        var employee = await _employeeService.GetAsync(employeeId);
        var sent = await _emailClient.SendAsync(employee.Email, subject, body);
        if (!sent)
        {
            throw new BadRequestException(Messages.EmailSendError);
        }
    }

    public Task SendBikeNotificationAsync(int employeeId, Bike bike, Dock dock, string action)
    {
        var subject = $"Bike {action} on the network";
        var body = $"""
            Bike {action.ToLowerInvariant()} on the network:
            Number: {bike.Number}
            Brand: {bike.Brand}
            Model: {bike.Model}
            Year: {bike.Year}
            Dock: {dock.Id}
            {action} date: {(action == "Addition" ? bike.DockedAt : bike.RemovedAt)}
            Status after {action.ToLowerInvariant()}: {bike.Status}
            Responsible employee: {employeeId}
            """;
        return SendToEmployeeAsync(employeeId, subject, body);
    }

    public Task SendDockNotificationAsync(int employeeId, Dock dock, string action)
    {
        var subject = $"Dock {action} at station";
        var body = $"""
            Dock {action.ToLowerInvariant()} at station:
            Number: {dock.Number}
            Model: {dock.Model}
            Manufacture year: {dock.ManufactureYear}
            Location: {dock.Location}
            {action} date: {(action == "Addition" ? dock.StationJoinedAt : dock.StationLeftAt)}
            Status after {action.ToLowerInvariant()}: {dock.Status}
            Responsible employee: {employeeId}
            """;
        return SendToEmployeeAsync(employeeId, subject, body);
    }
}
