package mainProgram.intergrationTest; // Project Organization

/* --- Imports --- */
import mainProgram.repository.JobRepository;
import mainProgram.repository.JobServiceRepository;
import mainProgram.table.Job;
import mainProgram.table.JobServices;
import mainProgram.table.Services;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/* --- AddRepairTest Class --- */
// Integration test that verifies the creation of a Job (Repair)
// through the REST API and ensures it is correctly stored in the database
// along with assigned services

// Run the full Spring Boot app for integration tests on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Uses "application-test.properties" for test DB and test-specific settings.
public class AddRepairTest {
    // Random Port used by the test server
    @LocalServerPort // Injects the random port used by the test server.
    private int port;

    // Client used to send HTTP requests during test
    @Autowired // Injects required Spring-managed components automatically
    private TestRestTemplate restTemplate;

    // Allows DB lookup for validation of job creation
    @Autowired // Injects required Spring-managed components automatically
    private JobRepository jobRepository;

    // Used to validate join-table entries
    @Autowired // Injects required Spring-managed components automatically
    private JobServiceRepository jobServiceRepository;

    // Will store the created service ID for linking later
    private Integer serviceId;

    /* TEST 1 - Create a Job Correctly & Assigning a Service */
    @Test // Marks the method as a test to be executed by JUnit.
    void createsJobCorrectly() {
        /* Step 1 - Create JSON Payload for Job */
        // Payload simulating what front-end would send when creating a repair
        Map<String, Object> repair = new HashMap<>();
        repair.put("title", "Test repair 1");
        repair.put("customer_name", "John Doe");
        repair.put("customer_phone", "12345678");
        repair.put("job_description", "Broken chain");
        repair.put("work_time_minutes", 30);
        repair.put("price_per_min", 5.0);
        repair.put("duration", 60);
        repair.put("date", "2025-10-28T13:45:00"); // ISO timestamp to match the API format

        // Add job status as an object (matches entity relationship)
        // Add job status manually, only ID needed since Job references the JobStatus entity
        // id = 1 (notDelivered)
        repair.put("status", Map.of("id", 1));

        /* Step 2 - Create a Service in DB First */
        // A job can reference services, so a service must exist before being assigned to job(s)
        Services testpart = new Services("fuld service", 200.00, 60 );

        // POST to /api/services -> Create Service + Insert into DB (ServiceController)
        ResponseEntity<Map> productResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/services", // URL for the endpoint that handles service creation
                        testpart, // Service object we are trying to create in DB
                        Map.class // Response expected as JSON Map (contains `id`)
                );

        // Verify service was created successfully
        assertEquals(HttpStatus.OK, productResp.getStatusCode()); // Check if API returned 200 OK
        serviceId = (Integer) productResp.getBody().get("id");    // Extract the service ID
        assertNotNull(serviceId);                                 // Ensure ID exist, meaning insert was successful

        // Add create service with quantity to repair payload
        // Single service: id=1, quantity=2
        repair.put(
                "services",
                List.of(
                        Map.of(
                                "id", serviceId, // Use returned ID
                                "quantity", 2        // Example: Customer ordered 2 units of this service
                        )
                )
        );

        /* Step 3 - Send a POST Request to Create a Job */
        // Build Headers for JSON request, ensures Server interprets data correctly as JSON
        HttpHeaders headers = new HttpHeaders();

        // Must specify JSON content-type for Spring to parse it
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Endpoint for creating a job (JobController)
        String url = "http://localhost:" + port + "/api/jobs/create";

        // POST /api/jobs/create -> Create Job
        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        url, // URL we are sending the POST request to
                        new HttpEntity<>(repair, headers), // HttpEntity combines Payload + Headers together
                        Map.class); // We expect back a JSON body containing fields including `id`

        // Basic validation of response
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Assert if the status code is good
        assertNotNull(response.getBody());                     // Assert if the response body is not `null`
        assertTrue(response.getBody().containsKey("id"));      // Assert if the response body contains an id-field

        // Read the returned job ID & Assert if it is not `null`
        Integer jobId = (Integer) response.getBody().get("id");
        assertNotNull(jobId);

        /* Step 4 - Validate Job is stored in DB */
        // Fetch newly inserted job
        Job saved = jobRepository.findById(jobId).orElse(null);

        // Assert if the job is not `null`
        assertNotNull(saved, "Job was not inserted into database");

        // Validate that the fields/attributed in the job was saved correctly
        assertEquals("Test repair 1", saved.getTitle());
        assertEquals("John Doe", saved.getCustomer_name());
        assertEquals(30, saved.getWork_time_minutes());
        assertEquals(5.0, saved.getPrice_per_minute());

        /* Step 5 - Validate join table JobServices */
        // Ensures the relationship remains (Job <-> Service) by checking if the JobService table
        // in the DB is not empty
        List<JobServices> jobServices = jobServiceRepository.findByJobId(jobId);
        assertFalse(jobServices.isEmpty(), "No JobServices rows created");

        // Get the first relation row (should be our added service)
        JobServices js = jobServices.getFirst();

        // Validate relationship data
        assertEquals(1, js.getService().getId()); // Assert that the service we added has id 1 as it is the first service added
        assertEquals("Test repair 1", js.getJob().getTitle());   // Assert the title of the job is correct
        assertEquals("fuld service", js.getService().getName()); // Assert the name of the service is correct
        assertEquals(2, js.getQuantity()); // Assert the quantity of the service is correct (we chose an example of quantity = 2)

        // Print Successful Message of Creating a Repair and assigning a Service
        System.out.println("Repair created and service assigned successfully");
    }
}
