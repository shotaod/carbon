import { configure } from '@kadira/storybook';
import injectTapEventPlugin from 'react-tap-event-plugin';

import '../public/css/base.css';
import '../public/css/base+.css';

injectTapEventPlugin();

const loadStories = () => {
  require('../stories/board');
  require('../stories/task');
};

configure(loadStories, module);
