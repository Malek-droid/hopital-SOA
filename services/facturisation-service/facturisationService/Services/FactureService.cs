using FacturisationServiceNamespace.Interfaces;
using Microsoft.Data.SqlClient;

namespace FacturisationServiceNamespace.Services
{
    public class FactureService : IFactureService
    {
        public string TestMethod(string input)
        {
            return $"Hello {input}";
        }

        // Billing logic with database persistence
        public string CalculerFacture(int patientId, double montant, int[] medicamentIds)
        {
            // Calculate fees and total
            double serviceFee = montant * 0.07;
            double tax = (montant + serviceFee) * 0.14;
            double montantTotal = Math.Round(montant + serviceFee + tax, 2);
            
            // Generate invoice number
            string numeroFacture = $"FACT-{patientId}-{DateTime.UtcNow:yyyyMMddHHmmss}";
            
            // Save to database
            var connectionString = Environment.GetEnvironmentVariable("ConnectionStrings__DefaultConnection");
            using (var conn = new SqlConnection(connectionString))
            {
                conn.Open();

                // Ensure FactureMedicaments table exists
                using (var cmdTable = conn.CreateCommand())
                {
                    cmdTable.CommandText = @"
                        IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='FactureMedicaments' AND xtype='U')
                        CREATE TABLE FactureMedicaments (
                            Id INT IDENTITY(1,1) PRIMARY KEY,
                            NumeroFacture VARCHAR(255) NOT NULL,
                            MedicamentId INT NOT NULL
                        )";
                    cmdTable.ExecuteNonQuery();
                }

                // Insert Facture
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = "INSERT INTO Factures (PatientId, Montant, MontantTotal, NumeroFacture) VALUES (@patientId, @montant, @montantTotal, @numeroFacture)";
                    cmd.Parameters.AddWithValue("@patientId", patientId);
                    cmd.Parameters.AddWithValue("@montant", montant);
                    cmd.Parameters.AddWithValue("@montantTotal", montantTotal);
                    cmd.Parameters.AddWithValue("@numeroFacture", numeroFacture);
                    cmd.ExecuteNonQuery();
                }

                // Insert Medicaments
                if (medicamentIds != null && medicamentIds.Length > 0)
                {
                    foreach (var medId in medicamentIds)
                    {
                        using (var cmdMed = conn.CreateCommand())
                        {
                            cmdMed.CommandText = "INSERT INTO FactureMedicaments (NumeroFacture, MedicamentId) VALUES (@numeroFacture, @medId)";
                            cmdMed.Parameters.AddWithValue("@numeroFacture", numeroFacture);
                            cmdMed.Parameters.AddWithValue("@medId", medId);
                            cmdMed.ExecuteNonQuery();
                        }
                    }
                }
            }
            
            return $"{numeroFacture} - Total: {montantTotal}";
        }
    }
}
