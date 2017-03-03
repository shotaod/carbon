import React, { Component } from 'react';
import { connect } from 'react-redux';
import { browserHistory } from 'react-router';

class NotFoundContainer extends Component {

  static transfer(props) {
    if (!props.auth.isLoggedIn) {
      browserHistory.push('/login');
    }
  }

  componentWillMount() {
    NotFoundContainer.transfer(this.props);
  }

  componentWillUpdate(nextProps) {
    NotFoundContainer.transfer(nextProps);
  }

  render() {
    return (<div>
      <h2>
        ページが見つかりません。
      </h2>
    </div>);
  }
}

const mapStateToProps = state => state;
export default connect(mapStateToProps)(NotFoundContainer);
