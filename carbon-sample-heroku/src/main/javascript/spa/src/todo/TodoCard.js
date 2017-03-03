import React, { Component, PropTypes } from 'react';

export default class TodoCard extends Component {
  static propTypes = {
    text: PropTypes.string.isRequired,
  };

  style = {
    container: {
      padding: '30px',
      border: '0.5px solid #EEE',
      borderRadius: '5px',
      boxShadow: '3px 3px 5px 0px #ddd',
    },
  };

  render() {
    return (<div style={this.style.container}>
      {this.props.text}
    </div>);
  }
}
