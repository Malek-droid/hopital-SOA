#!/bin/bash

echo "🐳 Démarrage de l'application Hospital SOA avec Docker..."

# Construire et démarrer
docker-compose up --build -d

echo "✅ Services démarrés:"
echo "   🌐 API Gateway: http://localhost:8082"
echo "   🔐 Auth Service: http://localhost:8083"
echo "   📅 Rendezvous Service: http://localhost:8084"
echo "   🏥 Patients Service: http://localhost:8082/patients-app/"
echo "   💊 Pharmacie Service: http://localhost:8082/pharmacie-app/"
echo "   🗄️  MySQL: localhost:3307"
echo ""
echo "📋 Commandes utiles:"
echo "   Voir les logs: docker-compose logs -f"
echo "   Arrêter: docker-compose down"
echo "   Statut: docker-compose ps"