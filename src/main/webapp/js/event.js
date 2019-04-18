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
                ///Giving the value Id to the form Comment
                document.getElementById("id").value = idEvent;
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
            document.getElementById('host').innerText = event.host;
            document.getElementById('date').innerText = event.date;
            document.getElementById('time').innerText = event.time;
            document.getElementById('timestamp').innerText = timeConverter(event.timestamp);
            document.getElementById('location').innerText = event.location;
            document.getElementById('details').innerText = event.details;
            document.getElementById('imageurl').innerText = event.imageurl;

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

function showComments() {
    const url = '/CommentServlet?event=' + idEvent;
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((comments) => {
            const eventContainer = document.getElementById('comment-box');
            if (comments.length == 0) {
                eventContainer.innerHTML = '<p>There are no comments yet.</p>';
            } else {
                eventContainer.remove();
            }
            comments.forEach((comment) => {
                const commentDiv = buildCommentDiv(comment);
                const content = document.getElementById('comments');
                content.appendChild(commentDiv);
                const br = document.createElement('br');
                content.appendChild(br);

            });
        });
}
///Building one comment
function buildCommentDiv(comment) {
    const bodyDiv = document.createElement('div');
    bodyDiv.classList.add("comment");

    const commentinfo = document.createElement('div');
    commentinfo.classList.add("comment-info");
    commentinfo.appendChild(document.createTextNode(timeConverter(comment.timestamp)));

    bodyDiv.appendChild(commentinfo);

    bodyDiv.appendChild(document.createElement('hr'));

    const commentuser = document.createElement('div');
    commentuser.classList.add("comment-user");
    commentuser.appendChild(document.createTextNode(comment.text));

    bodyDiv.appendChild(commentuser);

    return bodyDiv;
}
///Convert Timestamp to Normal Date
function timeConverter(UNIX_timestamp) {
    var a = new Date(UNIX_timestamp * 1000);
    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    var year = a.getFullYear();
    var month = months[a.getMonth()];
    var date = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    var time = date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
    return time;
}

function buildUI() {
    showEvent();
    showComments();
}
