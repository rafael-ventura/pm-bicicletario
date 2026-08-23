using PmBicicletario.Domain.Enums;
using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;
using Xunit;

namespace PmBicicletario.Tests.Unit.Repositories;

public class DockRepositoryTests
{
    [Fact]
    public void FindByLocation_ReturnsOnlyDocksAtThatLocation()
    {
        var repository = new DockRepository();
        repository.Save(new Dock { Location = "Central Station", Status = DockStatus.New });
        repository.Save(new Dock { Location = "North Station", Status = DockStatus.New });

        var docks = repository.FindByLocation("Central Station");

        Assert.Single(docks);
        Assert.Equal("Central Station", docks[0].Location);
    }

    [Fact]
    public void ExistsByBikeId_TrueOnlyWhenABikeIsLockedToADock()
    {
        var repository = new DockRepository();
        var bike = new Bike { Id = 42, Status = BikeStatus.Available };
        repository.Save(new Dock { Bike = bike, Status = DockStatus.Occupied });

        Assert.True(repository.ExistsByBikeId(42));
        Assert.False(repository.ExistsByBikeId(43));
    }
}
