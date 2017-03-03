import { StyleSheet, css } from 'aphrodite';

export const cssClass = (style) => {
  const styles = StyleSheet.create({ css: style });
  return css(styles.css);
};

export default {
  cssClass,
};
