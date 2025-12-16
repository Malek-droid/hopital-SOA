const { Sequelize } = require('sequelize');

// Connexion à la base de données
const sequelize = new Sequelize(
  process.env.DB_NAME || 'hospital_patients',
  process.env.DB_USER || 'hospital_user',
  process.env.DB_PASSWORD || 'StrongPass123!',
  {
    host: process.env.DB_HOST || 'localhost',
    dialect: 'mysql',
    logging: false
  }
);

module.exports = sequelize; // Export direct de l'objet Sequelize
