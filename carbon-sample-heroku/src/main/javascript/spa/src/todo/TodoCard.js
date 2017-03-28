import React, { Component, PropTypes } from 'react';
import { Card, CardActions, CardText, CardTitle } from 'material-ui/Card';
import Checkbox from 'material-ui/Checkbox';
import FlatButton from 'material-ui/FlatButton';

export default class TodoCard extends Component {
  static propTypes = {
    text: PropTypes.string.isRequired,
    handleToggle: PropTypes.func.isRequired,
    handleDelete: PropTypes.func.isRequired,
  };

  render() {
    return (<Card>
      <CardTitle title="TODO" />
      <CardText
        style={{ display: 'flex', alignItems: 'center' }}
      >
        <Checkbox
          style={{ width: 'auto' }}
        />
        <span>{this.props.text}</span>
      </CardText>
      <CardActions>
        <FlatButton label="Done" onTouchTap={this.props.handleToggle} />
        <FlatButton label="Delete" onTouchTap={this.props.handleDelete} />
      </CardActions>
    </Card>);
  }
}
