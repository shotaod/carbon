import React from 'react';
import { Provider } from 'react-redux';
import store from './index';

export default (reactApp) => <Provider store={store}>{reactApp}</Provider>;
