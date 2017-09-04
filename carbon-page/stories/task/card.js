import React, { Component } from 'react';
import Card from '../../src/task/Card';

export default story => {
  story.add('Card', () => <div className="row">
    <div className="col s3">
      <Card title="testtest" text="storybook render properly" point={10} />
    </div>
    <div className="col s3">
      <Card title="testtest" text="storybook render properly" point={10} />
    </div>
    <div className="col s3">
      <Card title="testtest" text="storybook render properly" point={10} />
    </div>
    <div className="col s3">
      <Card title="testtest" text="storybook render properly" point={10} />
    </div>
  </div>);
};
