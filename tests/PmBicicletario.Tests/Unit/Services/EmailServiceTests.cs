using Moq;
using PmBicicletario.Application.Exceptions;
using PmBicicletario.Application.Services;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Clients;
using Xunit;

namespace PmBicicletario.Tests.Unit.Services;

public class EmailServiceTests
{
    private readonly Mock<IEmailClient> _emailClient = new();
    private readonly Mock<IEmployeeService> _employeeService = new();
    private readonly EmailService _sut;

    public EmailServiceTests()
    {
        _sut = new EmailService(_emailClient.Object, _employeeService.Object);
    }

    [Fact]
    public async Task SendToEmployeeAsync_SendsToTheEmployeesEmail()
    {
        _employeeService.Setup(s => s.GetAsync(7))
            .ReturnsAsync(new Employee { Id = 7, Email = "repairer@example.com" });
        _emailClient.Setup(c => c.SendAsync("repairer@example.com", "Subject", "Body"))
            .ReturnsAsync(true);

        await _sut.SendToEmployeeAsync(7, "Subject", "Body");

        _emailClient.Verify(c => c.SendAsync("repairer@example.com", "Subject", "Body"), Times.Once);
    }

    [Fact]
    public async Task SendToEmployeeAsync_Throws_WhenClientFailsToSend()
    {
        _employeeService.Setup(s => s.GetAsync(7))
            .ReturnsAsync(new Employee { Id = 7, Email = "repairer@example.com" });
        _emailClient.Setup(c => c.SendAsync(It.IsAny<string>(), It.IsAny<string>(), It.IsAny<string>()))
            .ReturnsAsync(false);

        await Assert.ThrowsAsync<BadRequestException>(() => _sut.SendToEmployeeAsync(7, "Subject", "Body"));
    }
}
