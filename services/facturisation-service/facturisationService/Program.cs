using SoapCore;
using FacturisationServiceNamespace.Interfaces;
using FacturisationServiceNamespace.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSoapCore();
builder.Services.AddScoped<IFactureService, FactureService>();

var app = builder.Build();

app.UseRouting();

app.UseEndpoints(endpoints =>
{
    endpoints.UseSoapEndpoint<IFactureService>("/FactureService.svc",
        new SoapEncoderOptions(),
        SoapSerializer.DataContractSerializer);
});

app.Run();
