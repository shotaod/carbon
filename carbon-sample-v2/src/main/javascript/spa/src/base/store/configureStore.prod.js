import { createStore, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';
import rootReducer from '../reducer';
import localStorage from '../middleware/localStorage';

const enhancer = compose(
  localStorage,
  applyMiddleware(thunk),
);

const configureStore = initialState => createStore(
  rootReducer,
  initialState,
  enhancer,
);

export default configureStore;
