using FacturisationServiceNamespace.Interfaces;
using FacturisationServiceNamespace.Services;
using FacturisationServiceNamespace;
using SoapCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();

// Add CORS policy to allow requests from gateway
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

// Register your SOAP service
builder.Services.AddSingleton<IFactureService, FactureService>();

// Add SoapCore with optional configurations
builder.Services.AddSoapCore();

// Initialize Database
var connectionString = Environment.GetEnvironmentVariable("ConnectionStrings__DefaultConnection") 
                       ?? builder.Configuration.GetConnectionString("DefaultConnection");
if (!string.IsNullOrEmpty(connectionString))
{
    try 
    {
        DbInitializer.Initialize(connectionString);
        Console.WriteLine("Database and tables initialized successfully.");
    }
    catch (Exception ex)
    {
        Console.WriteLine($"Error initializing database: {ex.Message}");
    }
}

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseDeveloperExceptionPage();
}

// Enable CORS
app.UseCors();

// Serve static files (wwwroot/index.html)
app.UseDefaultFiles();
app.UseStaticFiles(new StaticFileOptions
{
    OnPrepareResponse = ctx =>
    {
        ctx.Context.Response.Headers.Append("Cache-Control", "no-cache, no-store, must-revalidate");
        ctx.Context.Response.Headers.Append("Pragma", "no-cache");
        ctx.Context.Response.Headers.Append("Expires", "0");
    }
});

app.UseRouting();

// Map SOAP endpoint
app.UseEndpoints(endpoints =>
{
    endpoints.UseSoapEndpoint<IFactureService>("/FactureService.svc", new SoapEncoderOptions(), 
        SoapSerializer.DataContractSerializer);
});

app.MapControllers();

app.Run();
