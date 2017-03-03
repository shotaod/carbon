import path from 'path';

const resourceRoot = process.env.STORYBOOK_PATH_ENV || '/';
const imgPath = 'img';

export const imgSrc = (...item) => path.join(resourceRoot, imgPath, ...item);

export default {
  imgSrc,
};
