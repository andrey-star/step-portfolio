function addRandomQuote() {
  const quotes = [
    `Now. Say my name. Heisenberg. You're god damn right`,
    'I am the danger.',
    `And on that terrible dissapointment I'm afraid it's time to end.`,
    'There’s a woman lying dead. Perfectly sound analysis but I was hoping you’d go deeper.',
    `You're treading on some mighty thin ice here.`,
  ];

  // Pick a random quote.
  const quote = quotes[Math.floor(Math.random() * quotes.length)];

  // Add it to the page.
  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;
}

function fetchComments() {
  const url = `/data?comment-limit=${commentLimitSelector.value}&comment-order=${commentOrderSelector.value}`;
  fetch(url)
    .then(response => response.json())
    .then(comments => {
      const commentContainer = document.getElementById('comments-container');
      commentContainer.innerHTML = '';
      for (let comment of comments) {
        const para = document.createElement('p');
        para.classList.add('border-bottom');
        para.appendChild(document.createTextNode(comment.text));
        para.id = comment.key;
        para.onclick = function () {
          deleteComment(para.id);
        };
        commentContainer.appendChild(para);
      }
    });
}

function submitComment() {
  submitFormUrlEncoded('/data', commentForm)
    .then(() => {
      for (let i = 0; i < commentForm.length; i++) {
        if (commentForm[i].name === 'user-comment') {
          commentForm[i].value = '';
        }
      }
      fetchComments();
    });

  // No redirect
  return false;
}

function submitFormUrlEncoded(url, form) {
  let formBody = [];
  for (let i = 0; i < form.length; i++) {
    const name = form[i].name;
    const value = form[i].value;
    if (name) {
      formBody.push(`${name}=${value}`);
    }
  }
  formBody = formBody.join('&');

  let fetchOptions = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: formBody
  };
  return fetch(url, fetchOptions);
}

function deleteComment(id) {
  const url = `/delete-data?comment-key=${id}`;
  fetch(url, { method: 'POST' })
    .then(() => fetchComments());
}

function deleteAllComments() {
  if (confirm('Are you sure you want to delete all comments?')) {
    const url = '/delete-data';
    fetch(url, { method: 'POST' })
      .then(() => fetchComments());
  }
}

let commentForm;
let commentLimitSelector;
let commentOrderSelector;

window.onload = function () {
  commentLimitSelector = document.getElementById('comment-limit-selector');
  commentLimitSelector.onchange = fetchComments;

  commentOrderSelector = document.getElementById('comment-order-selector');
  commentOrderSelector.onchange = fetchComments;

  commentForm = document.getElementById('comment-form');
  commentForm.onsubmit = submitComment;

  fetchComments();
}