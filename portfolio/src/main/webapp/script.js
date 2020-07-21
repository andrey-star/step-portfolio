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

// Image gallery
const imageSources = [
  'senya-gorgeous.jpg',
  'senya-superman.jpg',
  'senya-dissapointed.jpg'
];
let catImage;
let imageSelector;
let commentLimitSelector;
let commentForm;

function setPhoto() {
  for (let selector of imageSelector) {
    if (selector.checked) {
      catImage.src = 'images/' + imageSources[selector.value];
      break;
    }
  }
}

function fetchComments() {
  const url = `/data?comment-limit=${commentLimitSelector.value}`;
  fetch(url)
    .then(response => response.json())
    .then(comments => {
      const commentContainer = document.getElementById('comments-container');
      commentContainer.innerHTML = '';
      for (let comment of comments) {
        commentContainer.innerHTML += `<p>${comment.text}</p>\n`
      }
    });
}

function submitComment() {
  submitFormUrlEncoded('/data', commentForm)
    .then(() => {
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

function deleteComments() {
  if (confirm('Are you sure you want to delete all comments?')) {
    const url = '/delete-data';
    fetch(url, { method: 'POST' })
      .then(() => fetchComments());
  }
}

window.onload = function () {
  catImage = document.getElementById('cat-photo');

  imageSelector = document.getElementsByName('cat-photo-id');
  for (let selector of imageSelector) {
    selector.onchange = setPhoto;
  }
  commentLimitSelector = document.getElementById('comment-limit-selector');
  commentLimitSelector.onchange = fetchComments;

  commentForm = document.getElementById('comment-form');
  commentForm.onsubmit = submitComment;

  setPhoto();
  fetchComments();
}