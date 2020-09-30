(function() {
  const fillForm = (data) => {
    // collect user information
    document.getElementById("updateUsername").value = data.username;
    document.getElementById("updatePassword").value = data.password;
    document.getElementById("first").value = data.firstName;
    document.getElementById("last").value = data.lastName;
  }

  /**
   * Call function to update a user's information
   */
  const find = () => {
    const path = `https://${HOST}/user/find/${username}`;

    fetch(path)
    .then(response => response.json())
    .then(data => {
      fillForm(data);
    });
  };

  /**
   * Call function to update a user's information
   */
  const update = () => {
    // collect user information
    let password = document.getElementById("updatePassword").value;
    let firstName = document.getElementById("first").value;
    let lastName = document.getElementById("last").value;
    let primaryLanguage = document.getElementById("language").value;
    const user = {username, password, firstName, lastName, primaryLanguage};

    const path = `https://${HOST}/user/update`;

    // posting the user to db
    fetch(path, {
      method: 'POST',
      body: JSON.stringify(user),
      headers: {
        'Accept': 'application/json',
        'content-type': 'application/json'
      }
    }).then(res => {
      // if error, print alert message
      if (res.ok) {
        document.querySelector('.update').style.display = 'none';
        document.getElementById('updateUser').reset();
      }
    })
  };

  const updateBtn = document.getElementById('update');
  updateBtn.addEventListener('click', () => {
    updateBtn.nextElementSibling.style.display = 'flex';
    find();
  })

  const updateUser = document.getElementById('updateBtn');
  updateUser.addEventListener('click', () => {
    update();
  })

  /**
   * Call function to update a user's information
   */
  const deleteUser = () => {
    const path = `https://${HOST}/user/delete/${username}`;

    // posting the user to db
    fetch(path, {
      method: 'DELETE',
      body: JSON.stringify(username),
    })
    .then(res => res.text()) // or res.json()
    .then(res => console.log(res));

    window.location.href = window.location.href;
  };

  const deleteBtn = document.getElementById('deleteUser');
  deleteBtn.addEventListener('click', () => {
    deleteUser();
  })
})();