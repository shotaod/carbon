import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import createLogger from 'redux-logger';
import rootReducer from '../reducer';
import DevTools from '../container/DevTools';
import localStorage from '../middleware/localStorage';

const configureStore = (initialState) => {
  const logger = createLogger();

  const enhancer = compose(
    localStorage,
    applyMiddleware(thunk, logger),
    DevTools.instrument(),
  );
  const store = createStore(
    rootReducer,
    initialState,
    enhancer,
  );

  if (module.hot) {
    module.hot.accept('../reducer', () => {
      const nextRootReducer = require('../reducer').default; // eslint-disable-line global-require
      store.replaceReducer(nextRootReducer);
    });
  }

  return store;
};

export default configureStore;
