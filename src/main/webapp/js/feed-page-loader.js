/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
// This is the path of the variable of the user that is called in navigation-loader.js
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
    window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
    document.getElementById('page-title').innerText = parameterUsername;
    document.title = parameterUsername + " - Message Feed";
}

/** Fetches messages and add them to the page */
function fetchMessages() {
    const url = '/messages?user=' + parameterUsername;
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((messages) => {
            const messagesContainer = document.getElementById('message-container');
            if (messages.length == 0) {
                messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
            } else {
                messagesContainer.innerHTML = '';
            }
            messages.forEach((message) => {
                const messageDiv = buildMessageDiv(message);
                messagesContainer.appendChild(messageDiv);
            });
        });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
    const headerDiv = document.createElement('div');
    headerDiv.classList.add('message-header');
    headerDiv.appendChild(document.createTextNode(
        message.user + ' - ' + new Date(message.timestamp)));


    const bodyDiv = document.createElement('div');
    bodyDiv.classList.add('message-body');
    bodyDiv.innerHTML = handleBBCode(message.text);

    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message-div');
    messageDiv.appendChild(headerDiv);
    messageDiv.appendChild(bodyDiv);

    if (message.imageUrl != "") {
        bodyDiv.innerHTML += '<br/>';
        bodyDiv.innerHTML += '<img src="' + message.imageUrl + '" />';
    }

    return messageDiv;
}


/*Array of Bbcode tags in regular expression*/
var regexFind = new Array(
    new RegExp('\\[b](.*?)\\[/b]', 'gi'),
    new RegExp('\\[i](.*?)\\[/i]', 'gi'),
    new RegExp('\\[u](.*?)\\[/u]', 'gi'),
    new RegExp('\\[url](.*?)\\[/url]', 'gi')
);

/*Length of the array BbCode*/
var regexLen = regexFind.length;


/*Array of tags HTML to replace*/
var regexReplace = new Array(
    '<b>$1</b>',
    '<i>$1</i>',
    '<u>$1</u>',
    '<a href="$1">$1</a>'
);

/*Find the tags in Bbcode and replace by tags in HTML from the word input*/
function handleBBCode(input) {
    for (var i = 0; i < regexLen; i++) {
        input = input.replace(regexFind[i], regexReplace[i]);
    }
    return input;
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
   // setPageTitle();
   fetchMessages();
}
