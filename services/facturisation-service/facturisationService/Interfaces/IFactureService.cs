using System.ServiceModel;

[ServiceContract]
public interface IFactureService
{
    [OperationContract]
    string TestMethod(string input);
}

namespace FacturisationServiceNamespace.Interfaces
{
    public interface IFactureService
    {
        double CalculerFacture(int patientId, double montant);
    }
}
