import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import ActionNoteAdd from 'material-ui/svg-icons/action/note-add';
import Title from '../common/title';
import TodoCard from './TodoCard';
import { fetchTodos, addTodo, dropTodo } from './action';
import { errorType } from '../util/PropUtil';

class TodoContainer extends Component {
  static propTypes = {
    dispatchFetchTodos: PropTypes.func.isRequired,
    dispatchHandleSubmit: PropTypes.func.isRequired,
    dispatchHandleDrop: PropTypes.func.isRequired,
    todos: PropTypes.arrayOf(PropTypes.shape({
      id: PropTypes.number.isRequired,
      text: PropTypes.string.isRequired,
    })),
    error: errorType,
  };

  static defaultProps = {
    todos: [],
  };

  componentDidMount() {
    this.props.dispatchFetchTodos();
  }

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

  handleSubmit() {
    this.props.dispatchHandleSubmit(this.text);
  }

  renderTodos() {
    return this.props.todos.map(todo => {
      return (<TodoCard
        key={todo.id}
        text={todo.text}
        handleToggle={() => console.log('toggle')}
        handleDelete={() => this.props.dispatchHandleDrop(todo.id)}
      />)
    });
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
          {this.renderTodos()}
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
  const dispatchFetchTodos = () => dispatch(fetchTodos());
  const dispatchHandleSubmit = text => dispatch(addTodo(text));
  const dispatchHandleDrop = id => dispatch(dropTodo(id));
  return { dispatchFetchTodos, dispatchHandleSubmit, dispatchHandleDrop };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(TodoContainer);
