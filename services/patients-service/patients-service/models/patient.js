const { DataTypes } = require('sequelize');
const sequelize = require('./index'); // Import direct de Sequelize

// Définition du modèle Patient
const Patient = sequelize.define('Patient', {
  id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true
  },
  nom: {
    type: DataTypes.STRING,
    allowNull: false
  },
  prenom: {
    type: DataTypes.STRING,
    allowNull: false
  },
  date_naissance: {
    type: DataTypes.DATEONLY,
    allowNull: true
  },
  email: {
    type: DataTypes.STRING,
    allowNull: true,
    unique: true
  },
  telephone: {
    type: DataTypes.STRING,
    allowNull: true
  }
});

module.exports = Patient;
