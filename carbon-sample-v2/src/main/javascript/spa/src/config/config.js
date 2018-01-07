const env = process.env.NODE_ENV;
if (env === 'production') {
  module.exports = require('./config.production');
} else {
  module.exports = require('./config.development');
}
