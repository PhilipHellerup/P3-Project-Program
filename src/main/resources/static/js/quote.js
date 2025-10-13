// Function to fetch quote of the day from the server and display it in the "output" div
function sendQuote() {
  fetch('/quote') // Call the /quote endpoint on the server
    .then((response) => response.text()) // Convert response to text
    .then((data) => {
      document.getElementById('output').innerText = data; // Display quote
    })
    .catch((error) => console.error('Error fetching quote:', error));
}
