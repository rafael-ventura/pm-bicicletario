using PmBicicletario.Application.Dtos;

namespace PmBicicletario.Infrastructure.Clients;

/// <summary>
/// Calls the external service's email endpoint — a shared utility built by a third teammate
/// so none of the three services had to integrate with a mail provider on its own.
/// </summary>
public class EmailClient : IEmailClient
{
    private readonly HttpClient _httpClient;

    public EmailClient(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<bool> SendAsync(string to, string subject, string body)
    {
        try
        {
            var response = await _httpClient.PostAsJsonAsync("/api/send-email", new EmailDto(to, subject, body));
            return response.IsSuccessStatusCode;
        }
        catch (HttpRequestException)
        {
            return false;
        }
    }
}
