import { Action } from './action';

export default (state = {
  isFetching: false,
  data: [],
}, action) => {
  switch (action.type) {
    case Action.REQUEST_ADD_TODO:
      return Object.assign({}, state, { isFetching: true });

    case Action.FINISH_ADD_TODO: {
      const { payload, error } = action;
      if (error) {
        return Object.assign({}, state, {
          isFetching: false,
          error: {
            message: payload.message,
          },
        });
      }
      return Object.assign({}, state, {
        isFetching: false,
        data: [...state.data, payload.text],
        error: null,
      });
    }

    default:
      return state;
  }
};
