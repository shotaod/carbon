import {Action} from './action';

export default (state = {
  isFetching: false,
  data: [],
}, action) => {
  switch (action.type) {
    case Action.REQUEST_ADD_TODO:
      return Object.assign({}, state, {isFetching: true});

    case Action.FINISH_ADD_TODO: {
      const {payload, error} = action;
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
        data: [...state.data, payload],
        error: null,
      });
    }

    case Action.REQUEST_DROP_TODO:
      return Object.assign({}, state, {isFetching: true});

    case Action.FINISH_DROP_TODO: {
      const {payload, error} = action;
      if (error) {
        return Object.assign({}, state, {
          isFetching: false,
          error: {
            message: payload.message,
          },
        });
      }
      const {id} = payload;
      return Object.assign({}, state, {
        isFetching: false,
        data: state.data.filter(data => data.id !== id),
        error: null,
      });
    }

    case Action.REQUEST_FETCH_TODO:
      return Object.assign({}, state, {isFetching: true});

    case Action.FINISH_FETCH_TODO: {
      const {payload, error} = action;
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
        data: payload.data,
        error: null,
      });
    }

    default:
      return state;
  }
};
