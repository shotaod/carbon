import React, { Component } from 'react';

import connect from './store/connect';
import Board from './board/index';


const status = [
  {
    title: 'TODO',
    number: 1,
  },
  {
    title: 'DOING',
    number: 2,
  },
  {
    title: 'DONE',
    number: 3,
  },
  {
    title: 'NG',
    number: 4,
  },
];
const stories = [
  {
    title: 'Marketing Research',
    tasks: [
      {
        status: 1,
        title: 'mail',
        text: 'to XXX Company',
        point: 1,
      },
      {
        status: 1,
        title: 'making document',
        text: 'about this research',
        point: 10,
      },
      {
        status: 2,
        title: 'research YYY market',
        text: '',
        point: 5,
      },
      {
        status: 4,
        title: 'hearing',
        text: 'to John',
        point: 3,
      },
    ]
  },
];

class App extends Component {
  render() {
    return (
      <div>
        <Board statuses={status} stories={stories} />
      </div>
    );
  }
}

export default connect(App);
