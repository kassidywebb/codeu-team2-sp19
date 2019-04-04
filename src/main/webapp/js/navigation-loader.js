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

/**
 * Adds a login or logout link to the page, depending on whether the user is
 * already logged in.
 */
function addLoginOrLogoutLinkToNavigation() {
    const navigationElement = document.getElementById('navigation');
    if (!navigationElement) {
        console.warn('Navigation element not found!');
        return;
    }

    fetch('/login-status')
        .then((response) => {
            return response.json();
        })
        .then((loginStatus) => {
            if (loginStatus.isLoggedIn) {
                /**Creation of the menu and each element of the navigation bar **/
                navigationElement.appendChild(createListItemUl(createLink(
                    '/user-page.html?user=' + loginStatus.username, createListItem('Profile'))));

                navigationElement.appendChild(createListItemUl(createLink(
                    '/new-event.html', createListItem('Create new Event'))));

                navigationElement.appendChild(createListItemUl(createLink(
                    '/feed.html', createListItem('Messages'))));

                navigationElement.appendChild(createListItemUl(createLink(
                    '/', createListItem('Calendar'))));

                navigationElement.appendChild(createListItemUl(createLink(
                    '/logout', createListItem('Logout'))));
            } else {

                navigationElement.appendChild(createListItemUl(
                    createLink('/login', createListItem('Login'))));
            }
        });
}

/**
 * Creates an li element.
 * @param {Element} Text
 * @return {Element} li element
 */
function createListItem(Text) {
    const listItemElement = document.createElement('li');
    listItemElement.appendChild(document.createTextNode(Text));
    return listItemElement;
}

/* Creates an ul element.*/
function createListItemUl(childElement) {
    const listItemElement = document.createElement('ul');
    listItemElement.appendChild(childElement);
    return listItemElement;
}

/**
 * Creates an anchor element.
 * @param {string} url
 * @return {Element} Anchor element
 */
function createLink(url, childElement) {
    const linkElement = document.createElement('a');
    linkElement.appendChild(childElement);
    linkElement.href = url;
    return linkElement;
}