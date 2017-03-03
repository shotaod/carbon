import { createAction } from 'redux-actions';

export const Action = {
  TOGGLE_MENU: 'TOGGLE_MENU',
};

const toggleMenu= createAction(Action.TOGGLE_MENU);

export function toggleMenuAction() {
  return dispatch => dispatch(toggleMenu());
}
