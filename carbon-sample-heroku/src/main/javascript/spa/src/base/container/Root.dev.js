import React, { PropTypes } from 'react';
import { Provider } from 'react-redux';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import { Router } from 'react-router';
import routes from '../../routes';
import DevTools from './DevTools';

const Root = ({ store, history }) => (
  <Provider store={store}>
    <MuiThemeProvider>
      <div>
        <Router history={history} routes={routes} />
        <DevTools />
      </div>
    </MuiThemeProvider>
  </Provider>
);

Root.propTypes = {
  store: PropTypes.object.isRequired,  // eslint-disable-line react/forbid-prop-types
  history: PropTypes.object.isRequired,  // eslint-disable-line react/forbid-prop-types
};

export default Root;
