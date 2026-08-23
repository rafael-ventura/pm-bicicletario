using PmBicicletario.Domain.Models;
using PmBicicletario.Infrastructure.Repositories;
using Xunit;

namespace PmBicicletario.Tests.Unit.Repositories;

public class StationRepositoryTests
{
    [Fact]
    public void Save_ThenFindById_ReturnsSameStation()
    {
        var repository = new StationRepository();
        var saved = repository.Save(new Station { Location = "Central", Description = "Main square" });

        var found = repository.FindById(saved.Id);

        Assert.NotNull(found);
        Assert.Equal("Central", found!.Location);
    }

    [Fact]
    public void DeleteById_RemovesStation()
    {
        var repository = new StationRepository();
        var saved = repository.Save(new Station { Location = "Central", Description = "Main square" });

        repository.DeleteById(saved.Id);

        Assert.False(repository.ExistsById(saved.Id));
    }
}
