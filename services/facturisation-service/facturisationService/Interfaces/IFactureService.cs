using System.ServiceModel;

namespace FacturisationServiceNamespace.Interfaces
{
    [ServiceContract]
    public interface IFactureService
    {
        [OperationContract]
        string TestMethod(string input);

        [OperationContract]
        double CalculerFacture(int patientId, double montant);
    }
}
