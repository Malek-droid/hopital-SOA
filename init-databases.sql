-- Créer les bases de données
CREATE DATABASE IF NOT EXISTS hospital_auth;
CREATE DATABASE IF NOT EXISTS hospital_rendezvous;

GRANT ALL PRIVILEGES ON hospital_auth.* TO 'hospital_user'@'%';
GRANT ALL PRIVILEGES ON hospital_rendezvous.* TO 'hospital_user'@'%';
CREATE DATABASE IF NOT EXISTS hospital_patients;
CREATE DATABASE IF NOT EXISTS hospital_pharmacie;
GRANT ALL PRIVILEGES ON hospital_patients.* TO 'hospital_user'@'%';
GRANT ALL PRIVILEGES ON hospital_pharmacie.* TO 'hospital_user'@'%';
FLUSH PRIVILEGES;

-- Utiliser la base d'authentification
USE hospital_auth;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insérer des utilisateurs de test avec mots de passe cryptés
-- Mot de passe: 'password' crypté avec BCrypt
INSERT IGNORE INTO users (username, email, password, role) VALUES
('admin', 'admin@hospital.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVwUi.', 'ADMIN'),
('doctor', 'doctor@hospital.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVwUi.', 'DOCTOR'),
('patient', 'patient@hospital.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVwUi.', 'PATIENT'),
('yossra', 'yossra@gmail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVwUi.', 'DOCTOR');

-- Utiliser la base des rendez-vous
USE hospital_rendezvous;

-- Table des rendez-vous
CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(255) NOT NULL,
    doctor_id BIGINT NOT NULL,
    date DATETIME NOT NULL,
    type VARCHAR(100) NOT NULL,
    reason TEXT,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insérer quelques rendez-vous de test
INSERT IGNORE INTO appointments (patient_id, doctor_id, date, type, reason, status) VALUES
('PAT001', 1, '2024-12-01 10:00:00', 'CONSULTATION', 'Consultation générale', 'SCHEDULED'),
('PAT002', 1, '2024-12-02 14:30:00', 'URGENCE', 'Douleurs abdominales', 'SCHEDULED'),
('PAT001', 2, '2024-12-03 09:15:00', 'CONTROLE', 'Contrôle post-opératoire', 'SCHEDULED');