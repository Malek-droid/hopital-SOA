using FacturisationServiceNamespace.Interfaces;

namespace FacturisationServiceNamespace.Services
{
    public class FactureService : IFactureService
    {
        public string TestMethod(string input)
        {
            return $"Hello {input}";
        }
    }
}
