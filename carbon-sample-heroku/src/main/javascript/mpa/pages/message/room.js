import { createElement, removeChildrens } from '../commons/DomUtil';

// === dom element ==========
const messageMain = document.getElementById('message_main');
const startForm = document.getElementById('message_start_form');
const startButton = document.getElementById('message_start_button');
const usernameText = document.getElementById('message_username');
const roomIdText= document.getElementById('message_roomId');
const content = document.getElementById('message_content');
const form = document.getElementById('message_form');
const textarea = document.getElementById('message_text');

// === create element ==========
const createHeaderIcon = (username, roomId) => createElement(`
<div>
  <div class="chip">Name: ${username} @ room: ${roomId}</div>
  <a class="btn-floating btn-primary waves-effect waves-light" href="/message">
    <i class="material-icons">exit_to_app</i>
  </a>
</div>
`);

const createMessageCard = (sender, content) => createElement(`
<div class="col s12">
  <div class="card card-panel light-blue">
    <span class="card-title white-text">${sender}</span>
    <span class="white-text">${content}</span>
  </div>
</div>`);

// === ui event ==========
let connection;
startButton.onclick = () => {
  const username = usernameText.value;
  const roomId = roomIdText.value;
  startMessage(username, roomId);

  removeChildrens(startForm);
  startForm.appendChild(createHeaderIcon(username, roomId));
  messageMain.style.display = 'block';
};

const startMessage = (username, roomId) => {
  const socketProtocol = (document.location.protocol==='https:') ? 'wss:' : 'ws:';
  const socketUrl = `${socketProtocol}//${location.host}/message/socket/${username}/${roomId}`;
  connection = new WebSocket(socketUrl, ['soap']);
  connection.onmessage = data => {
    const message = JSON.parse(data.data);
    content.appendChild(createMessageCard(message.sender, message.content));
  };
};

form.onsubmit = event => {
  event.preventDefault();
  const message = textarea.value;
  connection.send(message);
  textarea.value = '';
};