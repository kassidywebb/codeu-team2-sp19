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
            document.getElementById('event-title').innerText = event.title;
            document.getElementById('username').innerText = event.user;
            document.getElementById('date').innerText = event.date;
            document.getElementById('time').innerText = event.time;
            document.getElementById('timestamp').innerText = event.timestamp;
            document.getElementById('location').innerText = event.location;
            document.getElementById('details').innerText = event.details;
            document.getElementById('imageurl').src = event.imageUrl;

            createMap(Number(event.lat), Number(event.lng), event.title);
            addLandmark();
        });
}

/** Creates a map that shows landmarks around Google. */
function createMap(lat, lng, title){

  const map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: lat, lng: lng},
    zoom: 15
  });

  addLandmark(map, lat, lng, title, title)
}

/** Adds a marker that shows an InfoWindow when clicked. */
function addLandmark(map, lat, lng, title, description){

  const marker = new google.maps.Marker({
    position: {lat: lat, lng: lng},
    map: map,
    title: title
  });

  var infoWindow = new google.maps.InfoWindow({
    content: description
  });
  marker.addListener('click', function() {
    infoWindow.open(map, marker);
  });
}

function buildUI() {
    showEvent();
}
