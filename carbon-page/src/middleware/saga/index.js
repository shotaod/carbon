import createSagaMiddleware from 'redux-saga'

const sagaMiddleware = createSagaMiddleware();
sagaMiddleware.run();

export default sagaMiddleware;
