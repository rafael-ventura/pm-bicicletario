namespace PmBicicletario.Infrastructure.Clients;

public interface IEmailClient
{
    Task<bool> SendAsync(string to, string subject, string body);
}
