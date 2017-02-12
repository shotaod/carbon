/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;
/******/
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "/public/";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	
	var _DomUtil = __webpack_require__(1);
	
	// === dom element ==========
	var messageMain = document.getElementById('message_main');
	var startForm = document.getElementById('message_start_form');
	var startButton = document.getElementById('message_start_button');
	var usernameText = document.getElementById('message_username');
	var roomIdText = document.getElementById('message_roomId');
	var content = document.getElementById('message_content');
	var form = document.getElementById('message_form');
	var textarea = document.getElementById('message_text');
	
	// === create element ==========
	var createHeaderIcon = function createHeaderIcon(username, roomId) {
	  return (0, _DomUtil.createElement)('\n<div class="chip">Name: ' + username + ' @ room: ' + roomId + '</div>\n');
	};
	
	var createMessageCard = function createMessageCard(sender, content) {
	  return (0, _DomUtil.createElement)('\n<div class="col s4">\n  <div class="card blue-grey darken-1">\n    <div class="card-content white-text">\n        <span class="card-title">' + sender + '</span>\n        <p>' + content + '</p>\n    </div>\n  </div>\n</div>');
	};
	
	// === ui event ==========
	var connection = void 0;
	startButton.onclick = function () {
	  var username = usernameText.value;
	  var roomId = roomIdText.value;
	  startMessage(username, roomId);
	
	  (0, _DomUtil.removeChildrens)(startForm);
	  startForm.appendChild(createHeaderIcon(username, roomId));
	  messageMain.style.display = 'block';
	};
	
	var startMessage = function startMessage(username, roomId) {
	  var socketProtocol = document.location.protocol === 'https:' ? 'wss:' : 'ws:';
	  var socketUrl = socketProtocol + '://' + location.host + '/message/socket/' + username + '/' + roomId;
	  connection = new WebSocket(socketUrl, ['soap']);
	  connection.onmessage = function (data) {
	    var message = JSON.parse(data.data);
	    content.appendChild(createMessageCard(message.sender, message.content));
	  };
	};
	
	form.onsubmit = function (event) {
	  event.preventDefault();
	  var message = textarea.value;
	  connection.send(message);
	  textarea.value = '';
	};

/***/ },
/* 1 */
/***/ function(module, exports) {

	'use strict';
	
	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	var createElement = function createElement(html) {
	  var template = document.createElement('div');
	  template.innerHTML = html;
	  return template.firstElementChild;
	};
	
	var removeElement = function removeElement(element) {
	  element.parentNode.removeChild(element);
	};
	
	var removeChildrens = function removeChildrens(element) {
	  while (element.firstChild) {
	    element.removeChild(element.firstChild);
	  }
	};
	
	exports.createElement = createElement;
	exports.removeElement = removeElement;
	exports.removeChildrens = removeChildrens;

/***/ }
/******/ ]);
//# sourceMappingURL=room.js.map