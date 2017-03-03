/* eslint-disable global-require */
const env = process.env.NODE_ENV;
if (env === 'production') {
  module.exports = require('./config.production');
} else if (env === 'staging') {
  module.exports = require('./config.staging');
} else {
  module.exports = require('./config.development').default(env);
}
