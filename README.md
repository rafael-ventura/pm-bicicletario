# Bike Sharing — Equipment Service

A microservice for managing a bike-share network's bikes, docks, and stations, built in .NET.

## Context
This was a university assignment: the professor split a bike-sharing system into three independent microservices — Rental, Equipment, and External — and assigned one to each member of a three-person team, so each of us would go through the full experience of designing and shipping a microservice, including integrating with the ones our teammates were building. The concept was intentionally simplified: each service could be written in whatever language and stack its author preferred, exposed a REST API for its own data, and called its teammates' REST APIs directly (no service discovery, no message broker) to complete operations that needed data it didn't own.

This repository is my piece: **Equipment** — bikes, docks, and stations, including locking/unlocking a bike into a dock and taking equipment in and out of the network for repairs. It calls the Rental service to validate employees and the External service to send notifications by email.

## Tech Stack
- .NET 8, ASP.NET Core Web API
- xUnit, Moq, `Microsoft.AspNetCore.Mvc.Testing`
- Swagger / OpenAPI (Swashbuckle)

## Architecture
```
Domain          entities, enums, error messages
Application     services (business rules), DTOs, exceptions
Infrastructure  in-memory repositories, HTTP clients for the peer services
Web             controllers, global exception-handling middleware
```
Repositories are in-memory, matching the original scope of the assignment (no database was part of it). The base URLs for the two peer services are configurable — see `ExternalServices` in `appsettings.json` — rather than hardcoded.

## Running it
```bash
dotnet restore
dotnet run --project src/PmBicicletario
```
The API listens on `http://localhost:8020` and serves Swagger UI at `/swagger`.

## Testing
```bash
dotnet test
```
Unit tests cover the repositories and services; integration tests exercise the controllers end-to-end through `WebApplicationFactory`, with the peer-service clients swapped for fakes.

## Status
Represents my part of the assignment — the Rental and External services were built by teammates and aren't part of this repository.
