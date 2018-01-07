const createElement = html => {
  const template = document.createElement('div');
  template.innerHTML = html;
  return template.firstElementChild;
};

const removeElement = element => {
  element.parentNode.removeChild(element);
};

const removeChildrens = element => {
  while (element.firstChild) element.removeChild(element.firstChild);
};

export {
  createElement,
  removeElement,
  removeChildrens,
};