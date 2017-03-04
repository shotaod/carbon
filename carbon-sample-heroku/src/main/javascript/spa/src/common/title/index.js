import React, { Component, PropTypes } from 'react';
import Divider from 'material-ui/Divider';

export default class Title extends Component {
  static propTypes = {
    title: PropTypes.string.isRequired,
  };

  style = {
    fontSize: '30px',
    marginBottom: '10px',
  };

  render() {
    return (<div>
      <h2 style={this.style}>{this.props.title}</h2>
      <Divider />
    </div>);
  }
}