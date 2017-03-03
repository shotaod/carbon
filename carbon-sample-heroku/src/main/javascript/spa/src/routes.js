import React, { Component } from 'react';
import {Route} from 'react-router';
import App from './base/container/App';
import Todo from './todo/TodoContainer';
import { NotFoundContainer } from './error/index';

class RootRedirect extends Component {

}

export default <Route path="" component={App}>
  <Route path="/" />
  <Route path="/sample/restapi" component={Todo}/>
  <Route path="*" component={NotFoundContainer}/>
</Route>;
