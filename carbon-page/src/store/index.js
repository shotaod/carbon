import { createStore, applyMiddleware } from 'redux'

import sagaMiddleware from '../middleware/saga';


const reducer = (state) => {
  return state;
};

const store = createStore(
  reducer,
  applyMiddleware(sagaMiddleware)
);

export default store;