import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import NavigationMenu from 'material-ui/svg-icons/navigation/menu';
import MenuPopComponent from './MenuPopComponent';
import { toggleMenuAction } from './action';

class NavigationBar extends Component {

  static propTypes = {
    appbar: PropTypes.shape({
      open: PropTypes.bool.isRequired,
    }),
    toggleMenu: PropTypes.func.isRequired,
  };

  handleMenuIconTapped(event) {
    event.preventDefault();
    if (!this.menuAnchor) {
      this.menuAnchor = event.currentTarget;
    }
    this.props.toggleMenu();
  }

  handleClose() {
    this.props.toggleMenu();
  }

  renderMenuButton() {
    return (<IconButton
      onTouchTap={this.handleMenuIconTapped.bind(this)}
    >
      <NavigationMenu />
    </IconButton>);
  }

  render() {
    return (<div>
      <AppBar
        ref={bar => this.node = { bar }}
        title="Carbon"
        iconElementLeft={this.renderMenuButton()}
      />
      <MenuPopComponent
        anchorEl={this.menuAnchor}
        open={this.props.appbar.open}
        handleClose={this.handleClose.bind(this)}
      />
    </div>);
  }
}

const mapStateToProps = (state) => {
  const { appbar } = state;
  return { appbar };
};

const mapDispatchToProps = (dispatch) => {
  const toggleMenu = (el) => dispatch(toggleMenuAction(el));
  return { toggleMenu };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(NavigationBar);
