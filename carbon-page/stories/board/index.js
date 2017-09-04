import React, { Component } from 'react';
import { storiesOf, action } from '@kadira/storybook';
import boardStory from './board';

const container = (story) => (<div style={{ margin: '20px' }}>{story()}</div>);
const story = storiesOf('Board', module).addDecorator(container);
boardStory(story);