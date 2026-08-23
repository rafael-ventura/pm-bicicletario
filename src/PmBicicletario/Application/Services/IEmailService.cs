using PmBicicletario.Domain.Models;

namespace PmBicicletario.Application.Services;

public interface IEmailService
{
    Task SendToEmployeeAsync(int employeeId, string subject, string body);
    Task SendBikeNotificationAsync(int employeeId, Bike bike, Dock dock, string action);
    Task SendDockNotificationAsync(int employeeId, Dock dock, string action);
}
