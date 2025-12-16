require('dotenv').config();
const express = require('express');
const cors = require('cors');
const path = require('path');
const app = express();

const sequelize = require('./models'); // Import Sequelize
require('./models/patient'); // Import du modèle pour que Sequelize le connaisse

const patientsRouter = require('./routes/patients');

app.use(cors());
app.use(express.json({ strict: false }));
app.use(express.urlencoded({ extended: true }));
app.use(express.static('public'));
app.use('/patients', patientsRouter);

// Serve the interface.html at root
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'interface.html'));
});

// Test simple pour vérifier que le serveur fonctionne
app.get('/test', (req, res) => {
  res.json({ ok: true, message: 'Server is working!' });
});

// Global Error Handler
app.use((err, req, res, next) => {
  console.error('Unhandled Error:', err);
  if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
    console.error('Failed Body:', err.body);
    return res.status(400).json({ error: 'Invalid JSON payload' });
  }
  res.status(err.status || 500).json({ error: err.message });
});

const PORT = process.env.PORT || 3001;

app.listen(PORT, async () => {
  try {
    await sequelize.authenticate();
    console.log('Database connected');

    await sequelize.sync({ alter: true });
    console.log('Tables synchronisées');

    console.log(`Patients service listening on port ${PORT}`);
  } catch (err) {
    console.error('Database error:', err);
  }
});
