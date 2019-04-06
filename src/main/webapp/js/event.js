// Get ?event=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const idEvent = urlParams.get('event');

if (!idEvent) {
    window.location.replace('/');
}
// Check if is logged in
function showEvent() {
    fetch('/login-status')
        .then((response) => {
            return response.json();
        })
        .then((loginStatus) => {
            if (loginStatus.isLoggedIn) {
                fetchEvent();
            }
        });
}
// Fetch the individual Event
function fetchEvent() {
    const url = '/IndividualEvent?event=' + idEvent;
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((event) => {
            if (event.user != null) {
                document.getElementById('event-title').innerText = event.title;
                document.getElementById('username').innerText = event.user;
                document.getElementById('date').innerText = event.date;
                document.getElementById('time').innerText = event.time;
                document.getElementById('timestamp').innerText = event.timestamp;
                document.getElementById('location').innerText = event.location;
                document.getElementById('details').innerText = event.details;
                document.getElementById('imageurl').innerText = event.imageurl;
            }
        });
}

function buildUI() {
    showEvent();
}