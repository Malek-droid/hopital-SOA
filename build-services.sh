#!/bin/bash

echo "🏗️  Construction des services Hospital SOA..."

# Construire auth-service
echo "📦 Construction de auth-service..."
cd services/auth-service/auth-service
mvn clean package -DskipTests
cd ../../..

# Construire rendezvous-service  
echo "📦 Construction de rendezvous-service..."
cd services/rendezvous-service/rendezvous-service
mvn clean package -DskipTests
cd ../../..

echo "✅ Tous les services sont construits!"
echo "🚀 Pour démarrer: docker-compose up --build"