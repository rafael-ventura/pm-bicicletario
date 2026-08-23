using Microsoft.Extensions.Options;
using PmBicicletario.Application.Services;
using PmBicicletario.Infrastructure.Clients;
using PmBicicletario.Infrastructure.Repositories;
using PmBicicletario.Web.Middleware;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(options =>
{
    options.SwaggerDoc("v1", new Microsoft.OpenApi.Models.OpenApiInfo
    {
        Title = "Bike Sharing — Equipment Service",
        Version = "v1",
        Description = "Manages bikes, docks and stations, and integrates with the rental and " +
                      "external services built by the rest of the team for this assignment."
    });
});

builder.Services.Configure<ExternalServicesOptions>(
    builder.Configuration.GetSection(ExternalServicesOptions.SectionName));

builder.Services.AddHttpClient<IEmployeeClient, EmployeeClient>((sp, client) =>
{
    var options = sp.GetRequiredService<IOptions<ExternalServicesOptions>>().Value;
    client.BaseAddress = new Uri(options.RentalServiceBaseUrl);
});

builder.Services.AddHttpClient<IEmailClient, EmailClient>((sp, client) =>
{
    var options = sp.GetRequiredService<IOptions<ExternalServicesOptions>>().Value;
    client.BaseAddress = new Uri(options.ExternalServiceBaseUrl);
});

builder.Services.AddSingleton<IBikeRepository, BikeRepository>();
builder.Services.AddSingleton<IDockRepository, DockRepository>();
builder.Services.AddSingleton<IStationRepository, StationRepository>();

builder.Services.AddScoped<IEmployeeService, EmployeeService>();
builder.Services.AddScoped<IEmailService, EmailService>();
builder.Services.AddScoped<IBikeService, BikeService>();
builder.Services.AddScoped<IDockService, DockService>();
builder.Services.AddScoped<IStationService, StationService>();

var app = builder.Build();

app.UseMiddleware<ExceptionHandlingMiddleware>();

app.UseSwagger();
app.UseSwaggerUI();

app.MapControllers();

app.Run();

// Exposed for WebApplicationFactory<Program> in the test project.
public partial class Program { }
