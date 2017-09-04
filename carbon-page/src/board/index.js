/*react*/
import React, { Component } from 'react';
import PropTypes from 'prop-types';

/*other*/
import Dragula from 'react-dragula';
import 'react-dragula/dist/dragula.min.css';

/*component*/
import Card from '../task/Card';

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
StoryStatusBox.propTypes = {
  className: PropTypes.string,
};

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
    return this.props.statusList
      .map((status, i) => (<StoryStatusBox className={this.uniqueName}>
        <Card title={`number_${i}`} text="hogehoge" point={10}/>
      </StoryStatusBox>));
  }

  render() {
    const minWidth = getMinWidth(this.props.statusList.length);
    return (<div className="row card-panel" style={{ minWidth }}>
      <span>Marketing Search</span>
      <div style={styles.storyRow} ref={this.dragDecorator.bind(this)}>
        {this.renderContent()}
      </div>
    </div>);
  }
}

Story.propTypes = {
  statusList: PropTypes.arrayOf(PropTypes.shape()).isRequired,
};

export default class Board extends Component {

  renderHeader() {
    const minWidth = getMinWidth(this.props.statusList.length);
    return (<div className="row card-panel" style={{ minWidth }}>
      <div style={styles.statusRow}>
        {this.props.statusList.map(status => (<div className="card-panel" style={{ minWidth: boxWidth }}>{status}</div>))}
      </div>
    </div>)
  }

  renderCreateStoryForm() {
    return (<div className="row">
      <button className="waves-effect waves-light btn">
        <i className="material-icons left">cloud</i>
        new story
      </button>
      <div className="card-panel" style={{ width: boxWidth }}>
        <div className="row">
          <div className="input-field col s12">
            <input placeholder="Placeholder" id="first_name" type="text" />
          </div>
          <div className="input-field col s12">
            <input placeholder="Placeholder" id="first_name" type="text" />
          </div>
        </div>
      </div>
    </div>);
  }

  renderStory() {
    return (<Story {...this.props}  />);
  }

  render() {
    return (<div style={{ display: 'table', padding: '20px' }}>
      {this.renderHeader()}
      {this.renderCreateStoryForm()}
      {this.renderStory()}
    </div>);
  }
}

Board.propTypes = {
  statusList: PropTypes.arrayOf(PropTypes.shape()).isRequired,
};
