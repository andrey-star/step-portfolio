fetch('/login-status')
  .then((response) => response.json())
  .then((status) => {
    if (status.didSetUsername) {
      window.location.href = '/';
    }
  });
