namespace PmBicicletario.Infrastructure.Clients;

/// <summary>
/// Base URLs for the two peer microservices built by the rest of the team for this assignment
/// (rental — employee records, and external — outbound email). Configurable instead of hardcoded,
/// see appsettings.json / environment variables.
/// </summary>
public class ExternalServicesOptions
{
    public const string SectionName = "ExternalServices";

    public string RentalServiceBaseUrl { get; set; } = string.Empty;
    public string ExternalServiceBaseUrl { get; set; } = string.Empty;
}
