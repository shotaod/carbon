import { createActions } from 'redux-actions';
import { keyValueIdentity } from '../util/ActionUtil';
import { postJson } from '../util/ApiUtil'

export const Action = {
  REQUEST_ADD_TODO: 'REQUEST_ADD_TODO',
  FINISH_ADD_TODO: 'FINISH_ADD_TODO',
};

const {
  requestAddTodo,
  finishAddTodo,
} = createActions({
  [Action.FINISH_ADD_TODO]: keyValueIdentity,
},
  Action.REQUEST_ADD_TODO,
);

export function addTodo(text) {
  return (dispatch, getStatus) => {
    const status = getStatus();
    const { isFetching } = status.todo;
    if (isFetching) {
      return Promise.resolve();
    }

    dispatch(requestAddTodo());
    return postJson('/todos', { token: 'hogehgoe' }, { text })
      .then(data => dispatch(finishAddTodo(data)))
      .catch((err) => {
        const { message } = err;
        return dispatch(finishAddTodo(new Error(message)));
      });
  };
}
