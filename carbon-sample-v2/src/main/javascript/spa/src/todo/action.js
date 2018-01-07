import { createActions } from 'redux-actions';
import { getJson, postJson, deleteJson } from '../util/ApiUtil';

export const Action = {
  REQUEST_FETCH_TODO: 'REQUEST_FETCH_TODO',
  FINISH_FETCH_TODO: 'FINISH_FETCH_TODO',
  REQUEST_ADD_TODO: 'REQUEST_ADD_TODO',
  FINISH_ADD_TODO: 'FINISH_ADD_TODO',
  REQUEST_DROP_TODO: 'REQUEST_DROP_TODO',
  FINISH_DROP_TODO: 'FINISH_DROP_TODO',
};

const {
  requestFetchTodo,
  finishFetchTodo,
  requestAddTodo,
  finishAddTodo,
  requestDropTodo,
  finishDropTodo,
} = createActions(
  Action.REQUEST_FETCH_TODO,
  Action.FINISH_FETCH_TODO,
  Action.REQUEST_ADD_TODO,
  Action.FINISH_ADD_TODO,
  Action.REQUEST_DROP_TODO,
  Action.FINISH_DROP_TODO,
);

export function fetchTodos() {
  return (dispatch, getStatus) => {
    const status = getStatus();
    const {isFetching} = status.todo;
    if (isFetching) {
      return Promise.resolve();
    }

    dispatch(requestFetchTodo());
    return getJson("/todos", {})
      .then(data => dispatch(finishFetchTodo({data: data.data})))
      .catch((err) => {
        const {message} = err;
        return dispatch(finishFetchTodo(new Error(message)));
      });
  };
}

export function addTodo(text) {
  return (dispatch, getStatus) => {
    const status = getStatus();
    const {isFetching} = status.todo;
    if (isFetching) {
      return Promise.resolve();
    }

    dispatch(requestAddTodo());
    return postJson('/todos', {}, {text})
      .then(data => {
        const {id} = data;
        dispatch(finishAddTodo({id, text}));
      })
      .catch((err) => {
        const {message} = err;
        return dispatch(finishAddTodo(new Error(message)));
      });
  };
}

export function dropTodo(id) {
  return (dispatch, getStatus) => {
    const status = getStatus();
    const {isFetching} = status.todo;
    if (isFetching) {
      return Promise.resolve();
    }

    dispatch(requestDropTodo());
    return deleteJson(`/todos/${id}`)
      .then(() => dispatch(finishDropTodo({id})))
      .catch((err) => {
        const {message} = err;
        return dispatch(finishDropTodo(new Error(message)));
      });
  };
}
