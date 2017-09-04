import React from 'react';
import Board from '../../src/board';

const status = [
  'TODO',
  'DOING',
  'DONE',
  'NG',
];

export default story => {
  story.add('board', () => (<Board statusList={status} />));
}
