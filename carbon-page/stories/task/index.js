import React, { Component } from 'react';
import { storiesOf, action } from '@kadira/storybook';
import cardStory from './card'

const story = storiesOf('Task', module);
cardStory(story);