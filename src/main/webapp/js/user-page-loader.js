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

function fetchAboutMe() {
    const url = '/about?user=' + parameterUsername;
    fetch(url).then((response) => {
        return response.text();
    }).then((aboutMe) => {
        const aboutMeContainer = document.getElementById('about-me-container');
        if (aboutMe == '') {
            aboutMe = 'This user has not entered any information yet.'
        }
      //  aboutMeContainer.innerText = '';
        aboutMeContainer.innerText = aboutMe;
      document.getElementById('about-me-form').classList.remove('hidden');
    })
}

// Get ?user=XYZ parameter value
// This is the path of the variable of the user that is called in navigation-loader.js
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
    window.location.replace('/');
}
function showProfileFormIfLoggedIn() {
    fetch('/login-status')
        .then((response) => {
            return response.json();
        })
        .then((loginStatus) => {
            if (loginStatus.isLoggedIn) {
                fetchImageUploadUrlAndShowForm();
            }
        });
}


function fetchImageUploadUrlAndShowForm() {
  fetch('/image-upload-url-profile')
      .then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const profileForm = document.getElementById('profile-form');
        profileForm.action = imageUploadUrl;
        profileForm.classList.remove('hidden');
        document.getElementById('imageurl').src = user.profilePic;
      });
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
    document.getElementById('page-title').innerText = parameterUsername;
    document.title = parameterUsername + ' - User Page';
}

function fetchEvents() {
    const url = '/OrganizedEvents?user=' + parameterUsername;
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((events) => {
            const eventContainer = document.getElementById('event-container');
            if (events.length == 0) {
                eventContainer.innerHTML = '<p>There are no posts yet.</p>';
            } else {
                eventContainer.remove();
            }
            events.forEach((event) => {
                const eventDiv = buildEventDiv(event);
                const content = document.getElementById('organized');
                content.appendChild(eventDiv);
            });
        });
}

function buildEventDiv(event) {
    const bodyDiv = document.createElement('div');
    bodyDiv.classList.add("row");

    const content = document.createElement('div');
    content.classList.add("content");

    content.appendChild(createChild('span', "date", event.date + "  " + event.time));
    content.appendChild(document.createElement('br'));
    content.appendChild(createChild('span', "author", event.user));

    const ref = document.createElement('a');
    ref.href = '/event.html?event=' + event.id;
    ref.appendChild(document.createTextNode(event.title));

    const hchild = document.createElement('h1');
    hchild.classList.add("title");
    hchild.appendChild(ref);

    content.appendChild(hchild);

    content.appendChild(createChild('p', "text", event.details));

    const read = document.createElement('a');
    read.href = '/event.html?event=' + event.id;
    read.classList.add("button");
    read.appendChild(document.createTextNode("Read more"));

    content.appendChild(read);

    bodyDiv.appendChild(createchildElement(createchildElement(createchildElement(content, 'div', "data"), 'div', "wrapper"), 'div', "card"));

    return bodyDiv;
}
/**
 * Creates an li element.
 * @param {string} tag
 * @param {string} class
 * @param {Element} element
 * @return {Element} HTML element
 */
function createchildElement(childElement, tag, cssClass) {
    const item = document.createElement(tag);
    item.classList.add(cssClass);
    item.appendChild(childElement);
    return item;
}
/**
 * Creates an li element.
 * @param {string} tag
 * @param {string} class
 * @param {text} text
 * @return {Element} HTML element
 */
function createChild(tag, cssClass, text) {
    const item = document.createElement(tag);
    item.classList.add(cssClass);
    item.appendChild(document.createTextNode(text));
    return item;
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
    setPageTitle();
    showProfileFormIfLoggedIn();
    fetchAboutMe();
    fetchEvents();
}
