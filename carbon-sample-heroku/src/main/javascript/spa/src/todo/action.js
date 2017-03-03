import { createActions } from 'redux-actions';
import { keyValueIdentity } from '../util/ActionUtil';

export const Action = {
  REQUEST_FETCH_SAMPLE: 'REQUEST_FETCH_SAMPLE',
  FINISH_FETCH_SAMPLE: 'FINISH_FETCH_SAMPLE',
};

const {
  requestFetchSample,
  finishFetchSample,
} = createActions({
  [Action.FINISH_FETCH_SAMPLE]: keyValueIdentity,
},
  Action.REQUEST_FETCH_SAMPLE,
);

function countApiMock() {
  return new Promise((resolve) => {
    setTimeout(() => resolve(), 500);
  });
}

export function fetchSample() {
  return (dispatch, getStatus) => {
    const status = getStatus();
    const { isFetching } = status.sample;
    if (isFetching) {
      return Promise.resolve();
    }

    dispatch(requestFetchSample());
    return countApiMock()
      .then(() => dispatch(finishFetchSample('data', 'done')))
      .catch((err) => {
        const { message } = err;
        return dispatch(finishFetchSample(new Error(message)));
      });
  };
}
