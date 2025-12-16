using Microsoft.Data.SqlClient;

namespace FacturisationServiceNamespace
{
    public static class DbInitializer
    {
        public static void Initialize(string connectionString)
        {
            var builder = new SqlConnectionStringBuilder(connectionString);
            string targetDatabase = builder.InitialCatalog;

            // 1. Create Database if not exists (Connect to master)
            builder.InitialCatalog = "master";
            using (var conn = new SqlConnection(builder.ConnectionString))
            {
                conn.Open();
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = $"IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = '{targetDatabase}') CREATE DATABASE [{targetDatabase}]";
                    cmd.ExecuteNonQuery();
                }
            }

            // 2. Create Tables (Connect to target database)
            using (var conn = new SqlConnection(connectionString))
            {
                conn.Open();
                
                // Table Factures
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = @"
                        IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Factures' AND xtype='U')
                        CREATE TABLE Factures (
                            Id INT IDENTITY(1,1) PRIMARY KEY,
                            PatientId INT NOT NULL,
                            Montant DECIMAL(18,2) NOT NULL,
                            MontantTotal DECIMAL(18,2) NOT NULL,
                            NumeroFacture VARCHAR(255) NOT NULL UNIQUE
                        )";
                    cmd.ExecuteNonQuery();
                }

                // Table FactureMedicaments
                using (var cmd = conn.CreateCommand())
                {
                    cmd.CommandText = @"
                        IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='FactureMedicaments' AND xtype='U')
                        CREATE TABLE FactureMedicaments (
                            Id INT IDENTITY(1,1) PRIMARY KEY,
                            NumeroFacture VARCHAR(255) NOT NULL,
                            MedicamentId INT NOT NULL
                        )";
                    cmd.ExecuteNonQuery();
                }
            }
        }
    }
}
