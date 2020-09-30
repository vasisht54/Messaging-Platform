(function() {
  let ws;
  const main = document.getElementById('main');
  const BUCKET_NAME = 'cs5500-chat-app-prod';
  const BUCKET_REGION = 'us-east-2';
  const CHAT = document.getElementById('chat');
  const THREAD_CHAT = document.getElementById('threadChat');
  const HOST = document.location.host;
  let MESSAGE_TYPE;
  let username;
  let currentChat = "";
  let chatType = 'USER';
  let chatRecipient;
  let chatID;
  let messageID;
  let THREAD_ID;

  const onlineAlert = (username, content) => {
    const alert = document.createElement('div');
    alert.className = 'alert-user-is-online';

    alert.innerHTML = `<b>${username}</b> ${content}`;

    document.getElementById('main').append(alert);
    window.setTimeout(() => {
      document.getElementById('main').removeChild(alert);
    }, 5000);
  }

  const printMessage = (messageID, status, from, content, date) => {
    const message = `<div id="${messageID}"
          class="message-item ${from === username ? 'sent-by-user' : ''}"
          data-private="${status}"
        >
        <h5>${from}</h5>
        <div class="message-item-content">
          ${content}
         </div>
         <span class="date">${date}</span>
         <button class="message-icon message-thread">Start a thread</button>
         <button class="message-icon message-delete">Delete</button>
       </div>
    `;
    return message;
  }

  const connect = () => {
    getUsers();
    ws = new WebSocket('ws://' + HOST + '/prattle/chat/' + username);

    ws.onmessage = function(event) {
      let message = JSON.parse(event.data);
      const date = new Date(message.timeStamp);
      const time = date.toLocaleTimeString();
      let formattedDate = new Intl.DateTimeFormat('en', { year: 'numeric', month: 'short', day: '2-digit' }).format(new Date(date));
      const finalDate = formattedDate + ' - ' + time;

      if (message.content === 'DISCONNECTED') {
        MESSAGE_TYPE = 'DISCONNECT';
      }

      if (message.content === 'CONNECTED') {
        MESSAGE_TYPE = 'CONNECT';
      }

      switch (MESSAGE_TYPE) {
       case 'CONNECT':
           onlineAlert(message.from, ' is now online!', message.time_stamp);
           break;
        case 'MESSAGE':
          content = printMessage(messageID, message.status, message.from, message.content, finalDate);
          CHAT.innerHTML += `${content}`;
          break;
        case 'THREAD':
          content = printMessage(messageID, message.status, message.from, message.content, finalDate);
          THREAD_CHAT.innerHTML += `${content}`;
          break;
        case 'DISCONNECT':
          onlineAlert(message.from, ' disconnected!', message.time_stamp);
          break;
        default:
          break;
      }
    };
  }

  const signin = () => {
    username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    const user = {username, password};
    let path = `https://${HOST}/user/signin`;
    fetch(path, {
      method: 'POST',
      body: JSON.stringify(user),
      headers: {
        'Accept': 'application/json',
        'content-type': 'application/json'
      }
    }).then(res => {
      if (!res.ok) {
        const lang = document.querySelector('.button');
        const alert = document.createElement('p');
        alert.className = 'alert';
        alert.textContent = 'The username or password are incorrect.';
        lang.parentNode.insertBefore(alert, lang);
      }
      else {
        const signinEl = document.querySelector('.signin');
        signinEl.parentNode.removeChild(signinEl);
        MESSAGE_TYPE = 'CONNECT';
        connect();
      }
    });
  };

  const signBtn = document.getElementById('signin');
  signBtn.addEventListener('click', () => {
    signin();
  })

  const checkPrivate = (target) => {
    if (!target.checked) {
      target.setAttribute('checked', '');
    } else {
      target.removeAttribute('checked');
    }
  }

  const checkbox = document.getElementById('status');
  checkbox.addEventListener('click', () => {
    checkPrivate(checkbox);
  });

  const storeThread = async () => {
    let path = `https://${HOST}/message/${THREAD_ID}`;
    const content = document.getElementById('threadMsg').value;
    const status = `${document.getElementById('threadStatus').checked}`;

    const message = {
      'type': chatType,
      'status': status,
      'from': username,
      'content': content,
      'to': chatRecipient
    }
    const response = await fetch(path, {
      method: 'POST',
      body: JSON.stringify(message),
      headers: {
       'Accept': 'application/json',
       'content-type': 'application/json'
      }
    });
  }

  const sendThread = async () => {
    const content = document.getElementById('threadMsg').value;
    const status = `${document.getElementById('threadStatus').checked}`;

    if (content.length > 0) {
      const message = {
        'type': chatType,
        'status': status,
        'from': username,
        'content': content,
        'to': chatRecipient
      }
      storeThread();

      ws.send(JSON.stringify(message));
    }
  }

  const storeMessage = async () => {
    let path = `https://${HOST}/message/add/message`;
    const content = document.getElementById('msg').value;
    const status = `${document.getElementById('status').checked}`;
    const message = {
      'type': chatType,
      'status': status,
      'from': username,
      'content': content,
      'to': chatRecipient
    }

    const response = await fetch(path, {
      method: 'POST',
      body: JSON.stringify(message),
      headers: {
       'Accept': 'application/json',
       'content-type': 'application/json'
      }
    });
    const json = await response.json();

    return json.id;
  }

  const storeFile = async () => {
    let path = `https://${HOST}/message/add/message`;
    const files = document.getElementById('file').files;
    const status = `${document.getElementById('status').checked}`;
    const message = {
      'type': chatType,
      'status': status,
      'from': username,
      'content': files,
      'to': chatRecipient
    }

    const response = await fetch(path, {
      method: 'POST',
      body: JSON.stringify(message),
      headers: {
       'Accept': 'application/json',
       'content-type': 'application/json'
      }
    });
    const json = await response.json();

    return json.id;
  }

  const send = async () => {
    const content = document.getElementById('msg').value;
    const status = `${document.getElementById('status').checked}`;

    if (content.length > 0) {
      const message = {
        'type': chatType,
        'status': status,
        'from': username,
        'content': content,
        'to': chatRecipient
      }

      messageID = await storeMessage();

      ws.send(JSON.stringify(message));
    }
  }

  const createChat = () => {
    let path = `https://${HOST}/message/add/chat`;
    const value = chatID;
    const string = {value};
    fetch(path, {
      method: 'POST',
      body: JSON.stringify(string),
      headers: {
        'Accept': 'application/json',
        'content-type': 'application/json'
      }
    }).then(res => {
      if (res.ok) {
        console.log('Request was successful!');
      }
    });
  }

  const s3upload = async () => {
    const files = document.getElementById('file').files;

    if (files.length > 0) {
      const file = files[0];
      const fileName = file.name;
      const filePath = BUCKET_NAME + '-' + fileName;
      const fileUrl = 'https://' + BUCKET_REGION + '.amazonaws.com/' + BUCKET_NAME + '/' +  filePath;
      s3.upload({
        Key: filePath,
        Body: file,
        ACL: 'public-read'
      }, function(err, data) {
        if(err) {
          alert('error');
        }
        alert('Successfully Uploaded!');

        let json = JSON.stringify({
          'type': chatType,
          'from': username,
          'content': fileUrl,
          'to': chatRecipient
        });
      })
      messageID = await storeFile();
      ws.send(json);
    }
  };

  const logMessages = (data) => {
    data.forEach(item => {
      const message = JSON.parse(item);
      const messageID = message._id.$oid
      const date = new Date(message.time_stamp);
      const time = date.toLocaleTimeString();
      let formattedDate = new Intl.DateTimeFormat('en', { year: 'numeric', month: 'short', day: '2-digit' }).format(new Date(date));
      const finalDate = formattedDate + ' - ' + time;
      const content = printMessage(messageID, message.is_private, message.from, message.content, finalDate);
      CHAT.innerHTML += `${content}`;
    })

  }

  const getChatHistory = () => {
    const users = [username, chatRecipient];
    users.sort();
    chatID = users[0] + users[1];

    const path = `https://${HOST}/message/${chatID}`;
    switch (chatType) {
      case 'USER':
        users.sort();
        chatID.concat(users[0], users[1]);
        break;
      case 'GROUP':
        chatID = chatRecipient;
        break;
    }
    if (chatID != currentChat) {
      CHAT.innerHTML = '';
      fetch(path)
      .then(response => response.json())
      .then(data => {
        if (data) {
          logMessages(data);
        }
      });
    }
  }

  const printUsers = (users) => {
    const userList = document.getElementById('recipients');
    users.forEach(item => {
      if (item != username) {
        const user = `
          <button id="${item}" class="user-item">${item}</button>
        `;
        user.innerHTML =  user;

        userList.innerHTML += user;
      }
    })

  }

  const getUsers = () => {
    const path = `https://${HOST}/app/users`;

    fetch(path)
    .then(response => response.json())
    .then(data => {
      printUsers(data);
    });
  }

  const userChat = document.getElementById('userChat');
  const groupChat = document.getElementById('groupChat');
    userChat.addEventListener('click', (e) => {
      e.preventDefault();

      if (chatType != 'USER') {
         chatType = 'USER';

        if (!userChat.classList.contains('is-active')) {
          chatType.parentNode.nextElementSibling.firstElementChild.classList.remove('is-active');
          userChat.classList.add('is-active');
        }

        getUsers();
      }
  })

  const thread = document.querySelector('.thread-list');
  const deleteMessage = (target) => {
    const path = `https://${HOST}/message/delete`;
    const value = target.id;
    const string = {value};

    fetch(path, {
      method: 'POST',
      body: JSON.stringify(string),
      headers: {
        'Accept': 'application/json',
        'content-type': 'application/json'
      }
    }).then(res => {
      if (res.ok) {
        console.log('Message deleted!');
      }
    });
  }

  const createThread = (id) => {
    THREAD_ID = id;
    let path = `https://${HOST}/message/add/thread`;
    const value = id;
    const string = {value};
    fetch(path, {
      method: 'POST',
      body: JSON.stringify(string),
      headers: {
        'Accept': 'application/json',
        'content-type': 'application/json'
      }
    }).then(res => {
      if (res.ok) {
        console.log('Thread started successfully!');
      }
    });
  }

  const openThread = (parent) => {
    thread.style.width = `440px`;
    thread.style.display = 'block';
    const threadChat = document.getElementById('threadChat');
    const clone = parent.cloneNode(true);
    threadChat.append(clone);
  }

  const closeThread = () => {
    thread.style.width = '0';
    thread.style.display = 'none';
    const threadChat = document.getElementById('threadChat');
    threadChat.innerHTML = '';
  }

  document.querySelector('.chat-log').addEventListener('click', (e) => {
    if (e.target.classList.contains('message-delete')) {
      const message = e.target.parentNode;
      deleteMessage(message);
      message.parentNode.removeChild(message);
    }

    if (e.target.classList.contains('message-thread')) {
      createThread(e.target.parentNode.id);
      openThread(e.target.parentNode);
    }
  })

  document.querySelector('.thread-close-btn').addEventListener('click', () => {
    const thread = document.querySelector('.thread-list');
    closeThread();
  })

  const recipient = document.getElementById('recipients');
  recipient.addEventListener('click', (e) => {
    if (e.target.classList.contains('user-item')) {
      closeThread();
    }
  })

  document.getElementById('recipients').addEventListener('click', (e) => {
    e.target.classList.add('is-active');
    chatRecipient = e.target.id;
    getChatHistory();
    createChat();
  })

  const sendMsg = document.getElementById('send');
  sendMsg.addEventListener('click', () => {
    MESSAGE_TYPE = 'MESSAGE';
    send();
    s3upload();
    document.getElementById('form').reset();
  })

  const threadMsg = document.getElementById('sendThread');
  threadMsg.addEventListener('click', () => {
    MESSAGE_TYPE = 'THREAD';
    sendThread();
    document.getElementById('formThread').reset();
  })
})();