namespace PmBicicletario.Domain.Models;

public class ApiError
{
    public ApiError(string code, string message)
    {
        Code = code;
        Message = message;
    }

    public string Code { get; set; }
    public string Message { get; set; }
}
