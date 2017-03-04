import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import ActionNoteAdd from 'material-ui/svg-icons/action/note-add';
import Title from '../common/title';
import TodoCard from './TodoCard';
import { addTodo } from './action';
import { errorType } from '../util/PropUtil';

class TodoContainer extends Component {
  static propTypes = {
    handleSubmit: PropTypes.func.isRequired,
    todos: PropTypes.arrayOf(PropTypes.string.isRequired),
    error: errorType,
  };

  renderError() {
    if (this.props.error) {
      return (<pre>
        {this.props.error.message}
      </pre>);
    }
  }

  handleTextChange(event) {
    this.text = event.target.value;
  }

  handleSubmit(event) {
    event.preventDefault();
    this.props.handleSubmit(this.text);
  }

  render() {
    return (
      <div>
        {this.renderError()}
        <Title title="TODO Sample"/>
        <br />
        <TextField
          hintText="add your todo"
          multiLine={true}
          rows={2}
          rowsMax={4}
          onChange={this.handleTextChange.bind(this)}
        />
        <RaisedButton
          onTouchTap={this.handleSubmit.bind(this)}
          label="add"
          primary={true}
          icon={<ActionNoteAdd />}
        />
        <ul>
          <TodoCard text="hogehoge"/>
        </ul>
      </div>
    );
  }
}


const mapStateToProps = (state) => {
  const { data, error } = state.todo;
  return { todos: data, error };
};

const mapDispatchToProps = (dispatch) => {
  const handleSubmit = (text) => dispatch(addTodo(text));
  return { handleSubmit };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(TodoContainer);
