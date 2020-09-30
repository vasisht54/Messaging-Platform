(function () {
  'use strict';
  let ws;
  let username;
  let userObj;
  let path = `http://localhost:8080`;

  const connect = () => {
    username = document.getElementById("username").value;
    findUser(username).then((res) => {
      if (res) {
        userObj = res;
        alert(`Logged in as ${userObj.firstName}`);
        document.getElementById("username").setAttribute('disabled',
            'disabled');
        document.getElementById("password").setAttribute('disabled',
            'disabled');
        document.getElementById("connect").setAttribute('disabled', 'disabled');

      } else {
        alert("User not found!!")
      }
    });
    let host = document.location.host;

    ws = new WebSocket('wss://' + host + '/chat/' + username);

    ws.onmessage = function (event) {
      let log = document.getElementById('chat');
      const alert = JSON.parse(event.data);
      log.innerHTML += `<p>${alert.from} ${alert.content}</p>`;
    };
  };

  const userConnect = document.getElementById('connect');
  userConnect.addEventListener('click', () => {
    connect();
  });

  // Group controller code

  const findUser = async (name) => {
    const response = await fetch(`${path}/user/find/${name}`);
    let res = await response;
    if (!res.ok) {
      return false
    } else {
      return await res.json()
    }
  };

  // Events

  const groupClick = document.getElementById("createGroup");
  groupClick.addEventListener('click', () => {
    createGroup();
  });

  const addMemberClick = document.getElementById("addMemberClick");
  addMemberClick.addEventListener('click', () => {
    addMember();
  });

  const removeMemberClick = document.getElementById("removeMember");
  removeMemberClick.addEventListener('click', () => {
    removeMember();
  });

  const addModeratorClick = document.getElementById("addModerator");
  addModeratorClick.addEventListener('click', () => {
    addModerator();
  });

  const removeModeratorClick = document.getElementById("removeModerator");
  removeModeratorClick.addEventListener('click', () => {
    removeModerator();
  });

  const mergeGroupClick = document.getElementById("mergeGroup");
  mergeGroupClick.addEventListener('click', () => {
    mergeGroup();
  });

  const deleteGroupClick = document.getElementById("deleteGroup");
  deleteGroupClick.addEventListener('click', () => {
    deleteGroup();
  });

  // Services

  const createGroupService = async (group) => {
    let res = await fetch(`${path}/group/create`, {
      method: 'POST',
      body: JSON.stringify(group),
      headers: {
        "content-type": "application/json",
        'Accept': 'application/json'
      }
    });
    let response = await res;
    if (response.ok) {
      return await res.json()
    } else {
      return false
    }
  };

  const addMemberService = async (groupName, memberName, user) => {
    let res = await fetch(
        `${path}/group/add-member/${groupName}/member/${memberName}`, {
          method: 'PUT',
          body: JSON.stringify(user),
          headers: {
            'content-type': 'application/json'
          }
        });
    let response = await res;
    if (response.ok) {
      return await res.json()
    } else {
      return false
    }
  };

  const removeMemberService = async (groupName, memberName, user) => {
    let res = await fetch(
        `${path}/group/remove-member/${groupName}/member/${memberName}`, {
          method: 'DELETE',
          body: JSON.stringify(user),
          headers: {
            'content-type': 'application/json'
          }
        });
    let response = await res;
    if (response.ok) {
      return await res.json()
    } else {
      return false
    }
  };

  const addModeratorService = async (groupName, moderatorName, user) => {
    let res = await fetch(
        `${path}/group/add-moderator/${groupName}/moderator/${moderatorName}`, {
          method: 'PUT',
          body: JSON.stringify(user),
          headers: {
            'content-type': 'application/json'
          }
        });
    let response = await res;
    if (response.ok) {
      return await res.json()
    } else {
      return false
    }
  };

  const removeModeratorService = async (groupName, moderatorName, user) => {
    let res = await fetch(
        `${path}/group/remove-moderator/${groupName}/moderator/${moderatorName}`,
        {
          method: 'DELETE',
          body: JSON.stringify(user),
          headers: {
            'content-type': 'application/json'
          }
        });
    let response = await res;
    if (response.ok) {
      return await res.json()
    } else {
      return false
    }
  };

  const mergeGroupService = async (groupA, groupB, user) => {
    let res = await fetch(
        `${path}/group/merge/${groupA}/${groupB}`, {
          method: 'PUT',
          body: JSON.stringify(user),
          headers: {
            'content-type': 'application/json'
          }
        });
    let response = await res;
    if (response.ok) {
      return await res.json()
    } else {
      return false
    }
  };

  const deleteGroupService = async (groupName, user) => {
    let res = await fetch(`${path}/group/delete/${groupName}`, {
      method: 'DELETE',
      body: JSON.stringify(user),
      headers: {
        'content-type': 'application/json'
      }
    });
    let response = await res;
    if (response.ok) {
      return await res.json()
    } else {
      return false
    }
  };

  // functions

  const createGroup = () => {
    if (username !== undefined) {
      let groupName = document.getElementById("groupName").value;
      let groupDescription = document.getElementById("groupDescription").value;
      if (groupName !== "" && groupDescription !== "") {
        let listOfModerators = [];
        let listOfUsers = [];
        listOfModerators.push(userObj);
        listOfUsers.push(userObj);
        let group = {
          groupName,
          description: groupDescription,
          listOfModerators,
          listOfUsers
        };
        createGroupService(group).then(message => {
          if(message){
            alert("Group created successfully!!")
          }else{
           alert('Internal server error. Please try again later.')
          }
        })
      } else {
        alert("Group name and description are mandatory")
      }
    } else {
      alert("Sign in to continue")
    }
  };

  const addMember = () => {
    if (username !== undefined) {
      let memberName = document.getElementById("member").value;
      let groupName = document.getElementById("groupNameAddMember").value;
      if (groupName !== "" && memberName !== "") {
        addMemberService(groupName, memberName, userObj).then(res => {
          console.log(res);
          if (res) {
            alert(`Member successfully added to ${groupName}`)
          } else {
            alert(`Could not be added`)
          }
        })
      } else {
        alert("Group name and member name are mandatory")
      }
    } else {
      alert("Sign in to continue")
    }
  };

  const removeMember = () => {
    if (username !== undefined) {
      let memberName = document.getElementById("removeMemberName").value;
      let groupName = document.getElementById("groupNameRemoveMember").value;
      if (groupName !== "" && memberName !== "") {
        removeMemberService(groupName, memberName, userObj).then(res => {
          if (res) {
            alert(`Member successfully removed from ${groupName}`)
          } else {
            alert(`Could not be removed`)
          }
        })
      } else {
        alert("Group name and member name are mandatory")
      }
    } else {
      alert("Sign in to continue")
    }
  };

  const addModerator = () => {
    if (username !== undefined) {
      let moderatorName = document.getElementById("moderator").value;
      let groupName = document.getElementById("groupNameAddModerator").value;
      if (groupName !== "" && moderatorName !== "") {
        addModeratorService(groupName, moderatorName, userObj).then(res => {
          if (res) {
            alert(`Moderator successfully added to ${groupName}`)
          } else {
            alert(`Could not be added`)
          }
        })
      } else {
        alert("Group name and moderator name are mandatory")
      }
    } else {
      alert("Sign in to continue")
    }
  };

  const removeModerator = () => {
    if (username !== undefined) {
      let moderatorName = document.getElementById("deleteModeratorName").value;
      let groupName = document.getElementById("groupNameRemoveModerator").value;
      if (groupName !== "" && moderatorName !== "") {
        removeModeratorService(groupName, moderatorName, userObj).then(res => {
          if (res) {
            alert(`Moderator successfully removed from ${groupName}`)
          } else {
            alert(`Could not be added`)
          }
        })
      } else {
        alert("Group name and moderator name are mandatory")
      }
    } else {
      alert("Sign in to continue")
    }
  };

  const mergeGroup = () => {
    let groupA = document.getElementById("groupMergeA").value;
    let groupB = document.getElementById("groupMergeB").value;
    if (groupA !== "" && groupB !== ""){
      mergeGroupService(groupA, groupB, userObj).then(res => {
        if (res) {
          alert(`${groupB} successfully merged to ${groupA}!!`)
        } else {
          alert(`${groupB} could not be merged to ${groupA}`)
        }
      })
    }else{
      alert("Group names are mandatory")
    }
  };

  const deleteGroup = () => {
    if (username !== undefined) {
      let groupName = document.getElementById("deleteGroupName").value;
      if (groupName !== "") {
        deleteGroupService(groupName, userObj).then(res => {
          if (res) {
            alert(`${groupName} successfully deleted!!`)
          } else {
            alert(`${groupName} could not be deleted`)
          }
        })
      } else {
        alert("Group name is mandatory")
      }
    } else {
      alert("Sign in to continue")
    }
  }

})();
