import React, { Component } from 'react';
import PropTypes from 'prop-types';

import './Card.css';

export default class Card extends Component {
  render() {
    return (<div className="card">
      <div className="card-content" style={{ padding: '10px' }}>
        <span className="card-title truncate" style={{ fontSize: '12px', textDecorationLine: 'underline' }}>
          {this.props.title}
        </span>
        <p style={{ width: '100%', overflow: 'auto' }}>
          {this.props.text}
        </p>
        <div className="row" style={{ margin: '0' }}>
          <div className="chip blue white-text right">
            {this.props.point}
          </div>
        </div>
      </div>
    </div>);
  }
}

export const propTypes = Card.propTypes = {
  status: PropTypes.number.isRequired,
  title: PropTypes.string.isRequired,
  text: PropTypes.string.isRequired,
  point: PropTypes.number.isRequired,
};

