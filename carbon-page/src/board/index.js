/*react*/
import React, { Component } from 'react';
import PropTypes from 'prop-types';

/*other*/
import Dragula from 'react-dragula';
import 'react-dragula/dist/dragula.min.css';
import _ from 'lodash';

/*component*/
import Card, { propTypes as cardProp} from '../task/Card';

import './index.css';


const styles = {
  statusRow: {
    display: 'flex',
    justifyContent: 'space-around',
  },
  storyContainer: {
    border: '1px solid #c0c0c0',
  },
  storyRow: {
    display: 'flex',
    justifyContent: 'space-around',
  },
  onDrag: {
    position: 'absolute',
    cursor: 'grabbing',
    padding: '0 10px',
    transition: 'opacity 0.4s ease-in-out',
  }
};


const boxWidth = 250;
const boxMinMargin = 10;
const arrangement = 100;
const getMinWidth = size => (boxWidth + boxMinMargin) * size + arrangement;
class StoryStatusBox extends Component {
  render() {
    return (<div className={this.props.className + ' card-panel grey lighten-5'} style={{ width: boxWidth }}>
      {this.props.children}
    </div>);
  }
}

class Story extends Component {
  static counter = 0;
  static getClassName = () => `dnd_${Story.counter}`;
  dragDecorator = (ref) => {
    if (!ref) return;
    const drake = Dragula([...document.getElementsByClassName(this.uniqueName)], {
      revertOnSpill: false,
      direction: 'horizontal',
    });
    drake
      .on('drag', el => el.classList.add('onGrabbing'));
    drake
      .on('dragend', el => el.classList.remove('onGrabbing'));
  };
  uniqueName = Story.getClassName();
  renderContent() {
    const { statuses, tasks } = this.props;
    return statuses
      .map(status => {
        const targetTasks = tasks.filter(task => task.status === status.number);
        const cards = targetTasks.map(task => <Card {...task} />);
        return (<StoryStatusBox className={this.uniqueName}>
          {cards}
        </StoryStatusBox>);
      });
  }

  render() {
    const minWidth = getMinWidth(this.props.statuses.length);
    return (<div className="row card-panel" style={{ minWidth }}>
      <span>{this.props.title}</span>
      <div style={styles.storyRow} ref={this.dragDecorator.bind(this)}>
        {this.renderContent()}
      </div>
    </div>);
  }
}

class NewStoryForm extends Component {

  state = {
    display: 'none',
  };

  handleClickOpenButton = e => {
    e.preventDefault();
    const display = this.state.display === 'block' ? 'none': 'block';
    this.setState({ display });
  };

  render() {
    return (<div className="row">
      <button className="waves-effect waves-light btn" onClick={this.handleClickOpenButton.bind(this)}>
        <i className="material-icons left">lightbulb_outline</i>
        new story
      </button>
      <div className="card-panel" style={{ width: '300px', display: this.state.display }}>
        <div className="row valign-wrapper">
          <div className="input-field col s8">
            <input placeholder="story name..." id="first_name" type="text" />
          </div>
          <button className="waves-effect waves-light btn col s4">
            <i className="material-icons">edit</i>
          </button>
        </div>
      </div>
    </div>);
  }
}

export default class Board extends Component {

  renderHeader() {
    return (
      <div style={styles.statusRow}>
        {this.props.statuses.map(({ title }) => (<div className="card-panel" style={{ minWidth: boxWidth }}>{title}</div>))}
      </div>
    );
  }

  renderStory() {
    const { statuses } = this.props;
    return this.props.stories.map(story => <Story {...Object.assign(story, { statuses })}/>);
  }

  render() {
    return (<div style={{ display: 'table', padding: '20px' }}>
      {this.renderHeader()}
      <NewStoryForm />
      {this.renderStory()}
    </div>);
  }
}

const statusesProp = PropTypes.arrayOf(PropTypes.shape({
  title: PropTypes.string,
  number: PropTypes.number,
}));

Story.propTypes = {
  title: PropTypes.string,
  statuses: statusesProp,
  tasks: PropTypes.arrayOf(cardProp),
};

Board.propTypes =  {
  statuses: statusesProp,
  stories: PropTypes.arrayOf(PropTypes.shape(Story.propTypes)),
};
