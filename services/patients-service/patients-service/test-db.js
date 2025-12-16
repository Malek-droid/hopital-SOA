require('dotenv').config();
const { sequelize } = require('./models');

(async () => {
  try {
    await sequelize.authenticate();
    console.log('DB OK');
    process.exit(0);
  } catch (e) {
    console.error('DB ERR', e);
    process.exit(1);
  }
})();
