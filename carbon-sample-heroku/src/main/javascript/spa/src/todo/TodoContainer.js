import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import TodoCard from './TodoCard';
import { fetchSample } from './action';
import { errorType } from '../util/PropUtil';

class TodoContainer extends Component {
  static propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    count: PropTypes.number.isRequired,
    error: errorType,
  };
  style = {
    title: {
      fontSize: '60px',
    },
    container: {
      padding: '20px',
    },
    textarea: {
      outline: 'none',
      width: '100%',
      border: 'none',
      borderBottom: '2px solid #ddd',
    },
    addButton: {
      padding: '10px 20px',
      backgroundColor: '#64b5f6',
      color: '#fff',
      borderRadius: '5px',
    }
  };

  renderError() {
    if (this.props.error) {
      return (<pre>
        {this.props.error.message}
      </pre>);
    }
  }

  handleSubmit(event) {
    event.preventDefault();
    const value = event.target['todoText'].value;
    this.props.handleSubmit(value);
  }

  render() {
    return (
      <div style={this.style.container}>
        {this.renderError()}
        <h2 style={this.style.title}>
          Todo Sample
        </h2>
        <br />
        <form onSubmit={event => this.handleSubmit(event).bind(this)}>
          <textarea style={this.style.textarea} id="todoText"/>
          <button style={this.style.addButton}>add</button>
        </form>
        <br />
        <ul>
          <TodoCard text="hogehoge"/>
        </ul>
      </div>
    );
  }
}


const mapStateToProps = (state) => {
  const { data, error } = state.sample;
  return { count: data.count, error };
};

const mapDispatchToProps = (dispatch) => {
  const handleSubmit = () => dispatch(fetchSample());
  return { handleSubmit };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(TodoContainer);
