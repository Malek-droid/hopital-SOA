using FacturisationServiceNamespace.Interfaces;

namespace FacturisationServiceNamespace.Services
{
    public class FactureService : IFactureService
    {
        public string TestMethod(string input)
        {
            return $"Hello {input}";
        }

        // Simple billing logic: example tax or fee — tweak later as needed
        public double CalculerFacture(int patientId, double montant)
        {
            // Example: add 7% service fee and 14% tax (toy logic)
            double serviceFee = montant * 0.07;
            double tax = (montant + serviceFee) * 0.14;
            return Math.Round(montant + serviceFee + tax, 2);
        }
    }
}
