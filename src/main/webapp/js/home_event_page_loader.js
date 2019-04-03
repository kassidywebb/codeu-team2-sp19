// Fetch messages and add them to the page.
  function fetchEvents(){
    const url = '/home';
    fetch(url).then((response) => {
      return response.json();
    }).then((messages) => {
      const eventContainer = document.getElementById('event-container');
      if(messages.length == 0){
       eventContainer.innerHTML = '<p>There are no posts yet.</p>';
      }
      else{
       eventContainer.innerHTML = '';
      }
      events.forEach((event) => {
       const eventDiv = buildEventDiv(event);
       eventContainer.appendChild(eventDiv);
      });
    });
  }

/*builds an element that displays a message
  messageDiv gets content of username,time, and message
*/
  function buildEventDiv(event){
   const usernameDiv = document.createElement('div');
   usernameDiv.classList.add("left-align");
   usernameDiv.appendChild(document.createTextNode(event.user));

   const timeDiv = document.createElement('div');
   timeDiv.classList.add('right-align');
   timeDiv.appendChild(document.createTextNode(new Date(event.timestamp)));

   const headerDiv = document.createElement('div');
   headerDiv.classList.add('event-header');
   headerDiv.appendChild(usernameDiv);
   headerDiv.appendChild(timeDiv);

   const bodyDiv = document.createElement('div');
   bodyDiv.classList.add('message-body');
   /* Changed how bodyDiv get's the message text in feed js. It previously
      did not upload pictures to the feed.html using appendChild, so I copied
      how the div was implemented in user-page-loader.js when taking in the
      text using innerHTML
    */
   bodyDiv.innerHTML = message.text;
   //bodyDiv.appendChild(document.createTextNode(message.text));

   const eventDiv = document.createElement('div');
   eventDiv.classList.add("event-div");
   eventDiv.appendChild(headerDiv);
   eventDiv.appendChild(bodyDiv);

   return eventDiv;
  }

  // Fetch data and populate the UI of the page.
  function buildUI(){
   fetchMessages();
  }
