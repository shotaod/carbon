import React, { Component, PropTypes } from 'react';
import NavigationBar from '../navbar/index';
import MainContent from '../maincontent/index';

export default class App extends Component {
  static propTypes = {
    children: PropTypes.element,
  };

  render() {
    return (
      <div>
        <NavigationBar />
        <MainContent>
          { this.props.children }
        </MainContent>
      </div>
    );
  }
}
