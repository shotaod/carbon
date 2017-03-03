import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import sampleReducer from '../../todo/reducer';
import appbarReducer from '../../common/appbar/reducer';

const rootReducer = combineReducers({
  appbar: appbarReducer,
  sample: sampleReducer,
  routing: routerReducer,
});

export default rootReducer;
