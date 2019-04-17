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
/*If user logged in show me the form new event*/
function showEventFormIfLoggedIn() {
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
  fetch('/image-upload-url-event')
      .then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const eventForm = document.getElementById('event-form');
        eventForm.action = imageUploadUrl;
        eventForm.classList.remove('hidden');

        const privateOption = document.getElementById('new-event');
        privateOption.classList.remove('hidden');
      });
}

function buildUI() {
    showEventFormIfLoggedIn();
}
