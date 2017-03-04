import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import todoReducer from '../../todo/reducer';
import appbarReducer from '../navbar/reducer';

const rootReducer = combineReducers({
  appbar: appbarReducer,
  todo: todoReducer,
  routing: routerReducer,
});

export default rootReducer;
