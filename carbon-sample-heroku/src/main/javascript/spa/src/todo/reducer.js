import { Action } from './action';

export default (state = {
  isFetching: false,
  data: { count: 0 },
}, action) => {
  switch (action.type) {
    case Action.REQUEST_FETCH_SAMPLE:
      return Object.assign({}, state, { isFetching: true });

    case Action.FINISH_FETCH_SAMPLE: {
      const { payload, error } = action;
      if (error) {
        return Object.assign({}, state, {
          isFetching: false,
          error: {
            message: payload.message,
          },
        });
      }
      const before = state.data.count;
      return Object.assign({}, state, {
        isFetching: false,
        data: { count: before + 1 },
        error: null,
      });
    }

    default:
      return state;
  }
};
