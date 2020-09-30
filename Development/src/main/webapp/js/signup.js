(function() {
  /**
   * Call function to sign up a new user
   */
  const signUp = () => {
    // collect user information
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let firstName = document.getElementById("first").value;
    let lastName = document.getElementById("last").value;
    let primaryLanguage = document.getElementById("language").value;
    const user = {username, password, firstName, lastName, primaryLanguage};

    let host = document.location.host;
    let pathname = document.location.pathname;
    let path = `https://${host}/user/signup`;

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
      if (!res.ok) {
        const lang = document.querySelector('.button');
        const alert = document.createElement('p');
        alert.className = 'alert';
        alert.textContent = 'This username is already taken. Please, choose a different one.';
        lang.parentNode.insertBefore(alert, lang);
      } else {
        // otherwise, print success message
        const form = document.querySelector('.form');
        form.innerHTML = `
          <h1>You account was created.</h1>
          <p class="text-with-link">You can now <a href="index.html">sign in</a>!</p>
        `;
      }
    })
  };

  const signupBtn = document.getElementById('signup');
  signupBtn.addEventListener('click', () => {
    signUp();
  })
})();