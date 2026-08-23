namespace PmBicicletario.Application.Exceptions;

public class DomainInvalidDataException : Exception
{
    public DomainInvalidDataException(string message) : base(message)
    {
    }

    public DomainInvalidDataException(string message, Exception innerException) : base(message, innerException)
    {
    }
}
