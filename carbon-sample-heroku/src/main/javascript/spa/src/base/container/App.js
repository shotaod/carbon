import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import AppBarContainer from '../../common/appbar/index';
import { logout } from '../../auth/action';

const defaultWrapperStyle = {
  width: '100%',
  height: '100%',
};

const mainStyle = {
  backgroundColor: '#fff',
  float: 'left',
};

class App extends Component {
  static propTypes = {
    children: PropTypes.element,
    auth: PropTypes.shape({
      isLoggedIn: PropTypes.bool.isRequired,
    }),
    handleLogout: PropTypes.func.isRequired,
  };

  render() {
    return (
      <div style={defaultWrapperStyle}>
        <AppBarContainer />
        <main style={mainStyle}>
          { this.props.children }
        </main>
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  const { auth } = state;
  return { auth };
};

const mapDispatchToProps = (dispatch) => {
  const handleLogout = () => dispatch(logout());
  return { handleLogout };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(App);
