using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;
using Xunit;

namespace PmBicicletario.Tests.Unit.Repositories;

public class BikeRepositoryTests
{
    [Fact]
    public void Save_AssignsIdOnFirstSave()
    {
        var repository = new BikeRepository();
        var bike = new Bike { Brand = "Caloi", Model = "10", Year = "2023", Status = BikeStatus.New };

        var saved = repository.Save(bike);

        Assert.NotEqual(0, saved.Id);
    }

    [Fact]
    public void Save_UpdatesExistingBike()
    {
        var repository = new BikeRepository();
        var saved = repository.Save(new Bike { Brand = "Caloi", Status = BikeStatus.New });

        saved.Brand = "Monark";
        repository.Save(saved);

        Assert.Equal("Monark", repository.FindById(saved.Id)!.Brand);
    }

    [Fact]
    public void FindById_ReturnsNull_WhenBikeDoesNotExist()
    {
        var repository = new BikeRepository();
        Assert.Null(repository.FindById(999));
    }

    [Fact]
    public void DeleteById_RemovesBike()
    {
        var repository = new BikeRepository();
        var saved = repository.Save(new Bike { Brand = "Caloi", Status = BikeStatus.New });

        repository.DeleteById(saved.Id);

        Assert.False(repository.ExistsById(saved.Id));
    }
}
