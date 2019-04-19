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

function buildUI() {
    fetchEvents();
}
