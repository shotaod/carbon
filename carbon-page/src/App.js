import React, { Component } from 'react';
import Board from './board/index';
const status = [
  'TODO',
  'DOING',
  'DONE',
  'NG',
];

class App extends Component {
  render() {
    return (
      <div>
        <Board statusList={status} />
      </div>
    );
  }
}

export default App;
