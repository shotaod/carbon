import React, { Component, PropTypes } from 'react';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import Popover from 'material-ui/Popover';

export default class MenuPopComponent extends Component {

  static propTypes = {
    open: PropTypes.bool.isRequired,
    handleClose: PropTypes.func.isRequired,
    anchorEl: PropTypes.object,
  };

  popOverConfig = {
    targetOrigin: {
      horizontal: 'left',
      vertical: 'top',
    },
    anchorOrigin: {
      horizontal:'left',
      vertical:'bottom',
    },
  };

  getAnchor() {
    if (this.props.anchorEl) return this.props.anchorEl;
  }

  render() {
    return (<Popover
      anchorEl={this.getAnchor()}
      open={this.props.open}
      anchorOrigin={this.popOverConfig.anchorOrigin}
      targetOrigin={this.popOverConfig.targetOrigin}
      onRequestClose={this.props.handleClose}
    >
      <Menu>
        <MenuItem primaryText="DailyReport"/>
        <MenuItem primaryText="" />
      </Menu>
    </Popover>);
  }
}
