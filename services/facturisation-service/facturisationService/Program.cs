using SoapCore;
using FacturisationServiceNamespace.Interfaces;
using FacturisationServiceNamespace.Services;
using System;
using System.ServiceModel;



var builder = WebApplication.CreateBuilder(args);

// Add services
builder.Services.AddSoapCore();
builder.Services.AddScoped<IFactureService, FactureService>();

var app = builder.Build();

// Configure routing
app.UseRouting();

// SOAP endpoint
app.UseEndpoints(endpoints =>
{
    endpoints.UseSoapEndpoint<IFactureService>("/FactureService.svc",
        new SoapEncoderOptions(),
        SoapSerializer.DataContractSerializer);
});

app.Run();

