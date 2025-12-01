# --- build stage ---
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /src

# copy project files
COPY *.csproj ./
# If you have subfolders (Interfaces, Services), copying project file only is fine because next COPY copies all
RUN dotnet restore

# copy rest and publish
COPY . .
RUN dotnet publish -c Release -o /app/publish

# --- runtime stage ---
FROM mcr.microsoft.com/dotnet/aspnet:9.0 AS runtime
WORKDIR /app

# set environment so Kestrel listens on port 80
ENV ASPNETCORE_URLS=http://+:80
# if you need ASPNETCORE_ENVIRONMENT, set it here (optional)
# ENV ASPNETCORE_ENVIRONMENT=Production

COPY --from=build /app/publish .
# If the produced dll is named differently, see notes below.

EXPOSE 80

# entrypoint: the DLL name should match your project assembly (usually same as csproj)
ENTRYPOINT ["dotnet", "facturisationService.dll"]
