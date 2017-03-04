import React, { Component } from 'react';
import { Route, browserHistory } from 'react-router';
import App from './base/container/App';
import Todo from './todo/TodoContainer';
import { NotFoundContainer } from './error/index';

const rootRedirectTo = '/sample/rest';
class Redirect extends Component {
  transfer() {
    browserHistory.push(rootRedirectTo);
  }

  componentWillMount() {
    this.transfer();
  }

  componentWillUpdate() {
    this.transfer();
  }
}

export default <Route path="" component={App}>
  <Route path="" component={Redirect} />
  <Route path="/" component={Redirect} />
  <Route path="/sample/rest" component={Todo} />
  <Route path="*" component={NotFoundContainer} />
</Route>;
