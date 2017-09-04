import React, { Component } from 'react';
import PropTypes from 'prop-types';

import './Card.css';

export default class Card extends Component {
  render() {
    return (<div className="card">
      <div className="card-content">
        <div className="valign-wrapper">
          <span className="card-title truncate" style={{width: '75%', display: 'inline-block'}}>
            {this.props.title}
          </span>
          <div className="chip blue white-text right">
            {this.props.point}
          </div>
        </div>
        <p style={{height: '100px', width: '100%', overflow: 'auto'}}>
          {this.props.text}
        </p>
      </div>
      <div className="card-action">
        <button className="btn-flat blue-text"><i className="material-icons">delete</i></button>
      </div>
    </div>);
  }
}

Card.propTypes = {
  title: PropTypes.string.isRequired,
  text: PropTypes.string.isRequired,
  point: PropTypes.number.isRequired,
};
