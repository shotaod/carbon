import { PropTypes } from 'react';

export const errorType = PropTypes.shape({
  message: PropTypes.string,
});

export const pageType = PropTypes.shape({
  current: PropTypes.number.isRequired,
  total: PropTypes.number.isRequired,
  hasNext: PropTypes.bool,
  hasPrev: PropTypes.bool,
});

export default {
  errorType,
};
