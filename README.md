# 🏥 Hôpital-SOA

> **Système de Gestion Hospitalière basé sur une Architecture Orientée Services (SOA)**

![Architecture SOA](https://img.shields.io/badge/Architecture-SOA-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=spring-boot)
![.NET](https://img.shields.io/badge/.NET-9.0-512BD4?logo=dotnet)
![Python](https://img.shields.io/badge/Python-FastAPI-3776AB?logo=python)
![Node.js](https://img.shields.io/badge/Node.js-Express-339933?logo=node.js)

---

## 📋 Table des Matières

- [Description du Projet](#-description-du-projet)
- [Architecture](#-architecture)
- [Technologies Utilisées](#-technologies-utilisées)
- [Microservices](#-microservices)
- [Prérequis](#-prérequis)
- [Installation et Démarrage](#-installation-et-démarrage)
- [Ports et Endpoints](#-ports-et-endpoints)
- [Utilisation](#-utilisation)
- [Authentification](#-authentification)
- [Base de Données](#-base-de-données)
- [Structure du Projet](#-structure-du-projet)

---

## 📖 Description du Projet

**Hôpital-SOA** est une application de gestion hospitalière complète construite selon les principes de l'**Architecture Orientée Services (SOA)**. Le système permet de gérer efficacement les différents aspects d'un établissement de santé :

- 🔐 **Authentification et autorisation** des utilisateurs (patients, médecins, administrateurs)
- 📅 **Gestion des rendez-vous** médicaux
- 👥 **Gestion des dossiers patients**
- 💊 **Gestion de la pharmacie** et des médicaments
- 🧾 **Facturation** des services médicaux via SOAP

Ce projet démontre l'intégration de multiples technologies et protocoles de communication (REST, SOAP) au sein d'une architecture microservices moderne.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT (Navigateur)                             │
└─────────────────────────────────────────────────────────────────────────────┘
                                        │
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         API GATEWAY (Spring Cloud)                          │
│                              Port: 8082                                      │
└─────────────────────────────────────────────────────────────────────────────┘
                                        │
           ┌────────────────────────────┼────────────────────────────┐
           │                            │                            │
           ▼                            ▼                            ▼
┌──────────────────┐      ┌──────────────────┐      ┌──────────────────┐
│   Auth Service   │      │ Rendezvous Svc   │      │ Facturisation    │
│  (Spring Boot)   │      │  (Spring Boot)   │      │     (.NET)       │
│   Port: 8083     │      │   Port: 8084     │      │   Port: 8081     │
│      REST        │      │      REST        │      │     SOAP         │
└────────┬─────────┘      └────────┬─────────┘      └────────┬─────────┘
         │                         │                         │
         ▼                         ▼                         ▼
┌──────────────────┐      ┌──────────────────┐      ┌──────────────────┐
│     MySQL        │      │     MySQL        │      │   SQL Server     │
│ hospital_auth    │      │hospital_rendezvous│      │   billingdb     │
└──────────────────┘      └──────────────────┘      └──────────────────┘
           
           ┌────────────────────────────┬────────────────────────────┐
           │                            │                            │
           ▼                            ▼                            
┌──────────────────┐      ┌──────────────────┐      
│ Patients Service │      │ Pharmacie Svc    │      
│   (Node.js)      │      │   (Python)       │      
│   Port: 3001     │      │   Port: 8000     │      
│      REST        │      │      REST        │      
└────────┬─────────┘      └────────┬─────────┘      
         │                         │                
         ▼                         ▼                
┌──────────────────┐      ┌──────────────────┐      
│     MySQL        │      │     MySQL        │      
│hospital_patients │      │hospital_pharmacie│      
└──────────────────┘      └──────────────────┘      
```

---

## 🛠️ Technologies Utilisées

### Backend

| Service | Technologie | Framework | Base de Données |
|---------|-------------|-----------|-----------------|
| **API Gateway** | Java 17 | Spring Cloud Gateway | - |
| **Auth Service** | Java 17 | Spring Boot 3.2 + Security | MySQL 8.0 |
| **Rendezvous Service** | Java 17 | Spring Boot 3.2 | MySQL 8.0 |
| **Facturation Service** | .NET 9.0 | ASP.NET Core + SoapCore | SQL Server 2022 |
| **Patients Service** | Node.js | Express + Sequelize | MySQL 8.0 |
| **Pharmacie Service** | Python 3.x | FastAPI + SQLAlchemy | MySQL 8.0 |

### Infrastructure

- **Conteneurisation** : Docker & Docker Compose
- **Authentification** : JWT (JSON Web Tokens)
- **Communication** : REST API + SOAP (WCF)

---

## 🔧 Microservices

### 1. 🛡️ Service d'Authentification (`auth-service`)
- **Port** : 8083
- **Technologie** : Spring Boot + Spring Security
- **Fonctionnalités** :
  - Inscription des utilisateurs
  - Connexion et génération de tokens JWT
  - Gestion des rôles (ADMIN, DOCTOR, PATIENT)
  - Validation et vérification des tokens

### 2. 📅 Service de Rendez-vous (`rendezvous-service`)
- **Port** : 8084
- **Technologie** : Spring Boot
- **Fonctionnalités** :
  - Création de rendez-vous médicaux
  - Consultation des rendez-vous par patient
  - Modification et annulation de rendez-vous
  - Gestion des statuts (SCHEDULED, COMPLETED, CANCELLED)

### 3. 🧾 Service de Facturation (`facturisation-service`)
- **Port** : 8081
- **Technologie** : .NET 9.0 avec SoapCore
- **Protocole** : SOAP/WCF
- **Fonctionnalités** :
  - Calcul des factures patients
  - Association des médicaments aux factures
  - Persistance dans SQL Server
  - Interface web pour la gestion des factures

### 4. 👥 Service Patients (`patients-service`)
- **Port** : 3001
- **Technologie** : Node.js + Express
- **Fonctionnalités** :
  - CRUD complet des dossiers patients
  - Recherche de patients
  - Interface utilisateur intégrée

### 5. 💊 Service Pharmacie (`pharmacie-service`)
- **Port** : 8000
- **Technologie** : Python + FastAPI
- **Fonctionnalités** :
  - Gestion du stock de médicaments
  - CRUD des médicaments (nom, quantité, prix)
  - API REST documentée automatiquement (Swagger)
  - Interface web de gestion

### 6. 🌐 API Gateway (`api-gateway`)
- **Port** : 8082
- **Technologie** : Spring Cloud Gateway
- **Fonctionnalités** :
  - Routage centralisé des requêtes
  - Gestion CORS
  - Point d'entrée unique pour tous les services

---

## ✅ Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- [Docker](https://www.docker.com/get-started) (version 20.x ou supérieure)
- [Docker Compose](https://docs.docker.com/compose/install/) (version 2.x ou supérieure)
- **6 Go de RAM** minimum (recommandé : 8 Go)
- **10 Go d'espace disque** disponible

---

## 🚀 Installation et Démarrage

### 1. Cloner le Projet

```bash
git clone https://github.com/votre-repo/hopital-SOA.git
cd hopital-SOA
```

### 2. Lancer l'Application

```bash
# Construire et démarrer tous les services
docker-compose up --build

# Ou en arrière-plan
docker-compose up -d --build
```

### 3. Vérifier le Statut des Services

```bash
docker-compose ps
```

### 4. Arrêter l'Application

```bash
docker-compose down

# Pour supprimer également les volumes (bases de données)
docker-compose down -v
```

---

## 🔌 Ports et Endpoints

| Service | Port | URL | Description |
|---------|------|-----|-------------|
| **API Gateway** | 8082 | `http://localhost:8082` | Point d'entrée principal |
| **Facturation (SOAP)** | 8081 | `http://localhost:8081/Service.asmx` | Service SOAP WSDL |
| **Auth Service** | 8083 | `http://localhost:8083` | Authentification REST |
| **Rendezvous** | 8084 | `http://localhost:8084/rendezvous` | Gestion des RDV |
| **Patients** | 3001 | `http://localhost:3001` | Gestion des patients |
| **Pharmacie** | 8000 | `http://localhost:8000` | Gestion pharmacie |
| **MySQL** | 3307 | `localhost:3307` | Base de données MySQL |
| **SQL Server** | 1433 | `localhost:1433` | Base de données SQL Server |

---

## 📘 Utilisation

### API d'Authentification

#### Inscription
```bash
POST http://localhost:8083/api/auth/register
Content-Type: application/json

{
  "username": "nouveau_patient",
  "email": "patient@example.com",
  "password": "motdepasse123",
  "role": "PATIENT"
}
```

#### Connexion
```bash
POST http://localhost:8083/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password"
}
```

### API Pharmacie (REST)

#### Lister les médicaments
```bash
GET http://localhost:8000/medicaments
```

#### Ajouter un médicament
```bash
POST http://localhost:8000/medicaments
Content-Type: application/json

{
  "nom_medicament": "Paracétamol",
  "quantite": 100,
  "prix": 5.50
}
```

### Service de Facturation (SOAP)

Accédez au WSDL : `http://localhost:8081/Service.asmx?wsdl`

Exemple de requête SOAP :
```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <CalculerFacture xmlns="http://tempuri.org/">
      <patientId>1</patientId>
      <montant>150.00</montant>
      <medicamentIds>
        <int>1</int>
        <int>2</int>
      </medicamentIds>
    </CalculerFacture>
  </soap:Body>
</soap:Envelope>
```

---

## 🔐 Authentification

Le système utilise **JWT (JSON Web Tokens)** pour l'authentification.

### Utilisateurs par Défaut

| Username | Email | Mot de passe | Rôle |
|----------|-------|--------------|------|
| `admin` | admin@hospital.com | `password` | ADMIN |
| `doctor` | doctor@hospital.com | `password` | DOCTOR |
| `patient` | patient@hospital.com | `password` | PATIENT |

### Utilisation du Token

Après connexion, incluez le token JWT dans l'en-tête de chaque requête :

```
Authorization: Bearer <votre_token_jwt>
```

---

## 🗄️ Base de Données

### MySQL (Port 3307)

| Base de Données | Description |
|-----------------|-------------|
| `hospital_auth` | Utilisateurs et authentification |
| `hospital_rendezvous` | Rendez-vous médicaux |
| `hospital_patients` | Dossiers patients |
| `hospital_pharmacie` | Stock de médicaments |

**Identifiants** :
- Utilisateur : `hospital_user`
- Mot de passe : `StrongPass123!`

### SQL Server (Port 1433)

| Base de Données | Description |
|-----------------|-------------|
| `billingdb` | Factures et facturation |

**Identifiants** :
- Utilisateur : `sa`
- Mot de passe : `YourStrong!Passw0rd`

---

## 📁 Structure du Projet

```
hopital-SOA/
├── 📁 services/
│   ├── 📁 api-gateway/           # Spring Cloud Gateway
│   │   └── 📁 api-gateway/
│   ├── 📁 auth-service/          # Service d'authentification (Spring Boot)
│   │   └── 📁 auth-service/
│   ├── 📁 facturisation-service/ # Service de facturation (.NET SOAP)
│   │   └── 📁 facturisationService/
│   ├── 📁 patients-service/      # Service patients (Node.js)
│   │   └── 📁 patients-service/
│   ├── 📁 pharmacie-service/     # Service pharmacie (Python FastAPI)
│   │   └── 📁 pharmacie-service/
│   └── 📁 rendezvous-service/    # Service rendez-vous (Spring Boot)
│       └── 📁 rendezvous-service/
├── 📁 documentation/             # Documentation du projet
├── 📄 docker-compose.yml         # Configuration Docker Compose
├── 📄 init-databases.sql         # Script d'initialisation des BDD
├── 📄 hopital-SOA.sln            # Solution Visual Studio
└── 📄 README.md                  # Ce fichier
```

---

## 🧪 Tests

### Test de connectivité des services

```bash
# Vérifier l'API Gateway
curl http://localhost:8082/actuator/health

# Vérifier le service de pharmacie
curl http://localhost:8000/medicaments

# Vérifier l'authentification
curl -X POST http://localhost:8083/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
```

---

## 🤝 Contribution

Les contributions sont les bienvenues ! Veuillez suivre ces étapes :

1. Forker le projet
2. Créer une branche pour votre fonctionnalité (`git checkout -b feature/NouvelleFeature`)
3. Commiter vos modifications (`git commit -m 'Ajout d'une nouvelle fonctionnalité'`)
4. Pousser vers la branche (`git push origin feature/NouvelleFeature`)
5. Ouvrir une Pull Request

---

## 📄 Licence

Ce projet est développé dans un cadre éducatif pour démontrer les concepts de l'Architecture Orientée Services (SOA).

---

## 👥 Auteurs

Projet réalisé dans le cadre d'un cours sur les **Architectures Orientées Services**.

---

<p align="center">
  <strong>🏥 Hôpital-SOA - Architecture Orientée Services en Action</strong>
</p>
