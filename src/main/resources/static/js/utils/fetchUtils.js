// Exported utility function to handle common HTTP response errors consistently across fetch requests
export async function handleFetchErrors(response) {
  // If the response is successful (status 200–299), simply return it
  if (response.ok) return response;

  // Default fallback error message (used if no specific case matches)
  let message = 'Ukendt fejl.';

  // Match specific HTTP status codes to user-friendly error messages
  switch (response.status) {
    // Bad Request = Client sent invalid data
    case 400:
      message = 'Ugyldige data sendt til serveren.';
      break;

    // Not Found = Resource does not exist
    case 404:
      message = 'Ressourcen blev ikke fundet.';
      break;

    // Conflict = Duplicate data detected
    case 409:
      message = 'Duplikatdata opdaget.';
      break;

    // Internal Server Error = Problem on server side
    case 500:
      message = 'Serverfejl opstod.';
      break;

    // Covers any other unexpected status code
    default:
      message = `Ukendt serverfejl (Status: ${response.status})`;
      break;
  }
  // Throw an Error object with the message and HTTP status code
  throw new Error(`${message} (Status: ${response.status})`);
}
