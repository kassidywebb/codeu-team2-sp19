// Fetch messages and add them to the page.
function fetchEvents() {
    const url = '/home';
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
                const content = document.getElementById('content');
                content.appendChild(eventDiv);
            });
        });
}

/*builds an element that displays a event */
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

function buildUI() {
    fetchEvents();
}