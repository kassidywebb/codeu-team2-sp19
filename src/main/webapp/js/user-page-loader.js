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
        const aboutMeContainer = document.getElementById('user-details');
        if (aboutMe == '') {
            aboutMe = 'This user has not entered any information yet.'
        }
        aboutMeContainer.innerText = '';
        aboutMeContainer.innerText = aboutMe;
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

/*builds an element that displays a event */
function buildEventDiv(event) {
    const bodyDiv = document.createElement('div');

    const container = document.createElement('div');
    container.classList.add("container");

    var image = new Image();
    image.src = event.imageUrl;
    image.classList.add("image");
    container.appendChild(image)

    const middle = document.createElement('div');
    middle.classList.add("middle");

    middle.appendChild(createChild('span', "author", event.user));
    middle.appendChild(document.createElement('br'));
    middle.appendChild(createChild('span', "date", event.date + " - " + event.time));

    const ref = document.createElement('a');
    ref.href = '/event.html?event=' + event.id;
    ref.appendChild(document.createTextNode(event.title));

    const hchild = document.createElement('p');
    hchild.classList.add("title");
    hchild.appendChild(ref);
    middle.appendChild(hchild);

    middle.appendChild(createChild('p', "text", event.details));

    const read = document.createElement('a');
    read.href = '/event.html?event=' + event.id;
    read.classList.add("button");
    read.appendChild(document.createTextNode("Read more"));
    middle.appendChild(read);

    container.appendChild(middle)

    bodyDiv.appendChild(createchildElement(createchildElement(container, 'div', "wrapper"), 'div', "card"));

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
    fetchAboutMe();
    fetchEvents();
}
