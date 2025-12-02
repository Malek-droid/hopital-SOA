using System;
using System.ServiceModel;

// Use the same interface as your service
[ServiceContract(Namespace = "http://tempuri.org/")]
public interface IFactureService
{
    [OperationContract(Action = "http://tempuri.org/IFactureService/TestMethod",
                       ReplyAction = "http://tempuri.org/IFactureService/TestMethodResponse")]
    string TestMethod(string input);
}

class Program
{
    static void Main()
    {
        try
        {
            var binding = new BasicHttpBinding();
            var endpoint = new EndpointAddress("http://localhost:5196/FactureService.svc");

            var channelFactory = new ChannelFactory<IFactureService>(binding, endpoint);
            var client = channelFactory.CreateChannel();

            // Test the service
            var result = client.TestMethod("Hello from VS Code Client!");
            Console.WriteLine($"Service returned: {result}");

            ((ICommunicationObject)client).Close();
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error: {ex.Message}");
        }

        Console.WriteLine("Press any key to exit...");
        Console.ReadKey();
    }
}