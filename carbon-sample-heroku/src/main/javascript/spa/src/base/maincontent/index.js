import React, { Component, PropTypes } from 'react';

const style = {
  padding: '20px 10px',
};
export default class MainContent extends Component {
  static propTypes = {
    children: PropTypes.element.isRequired,
  };

  render() {
    return (<main style={style}>
      {this.props.children}
    </main>)
  }
}
