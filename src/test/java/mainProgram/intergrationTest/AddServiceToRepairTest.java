package mainProgram.intergrationTest; // Project Organization

/* --- Imports --- */
import mainProgram.repository.JobServiceRepository;
import mainProgram.table.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/* --- AddServiceToRepairTest Class --- */
// Integration test that verifies the workflow of creating a repair,
// creating a service, and successfully linking that service to the repair.
// The test ensures that the REST API works as expected and that the
// JobServices entry is stored correctly in the database.

// Run the full Spring Boot app for integration tests on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Use application-test.properties for isolated test DB
public class AddServiceToRepairTest {
    // Random Port used by the test server
    @LocalServerPort // Injects the random port used by the test server.
    private int port;

    // Client used to send HTTP requests during test execution
    @Autowired // Injects required Spring-managed components automatically
    private TestRestTemplate restTemplate;

    // Used to verify DB state after service assignment
    @Autowired // Injects required Spring-managed components automatically
    private JobServiceRepository jobServiceRepository;

    // Will store IDs created during the test
    private Integer jobId;
    private Integer serviceId;

    /* TEST 1 - Create a Repair, Create a Service, and Add the Service to the Repair */
    @Test
    void addPartToRepair() {
        /* Step 1 - Create a Repair */
        // Build JSON payload representing a repair creation request
        Map<String, Object> repair = new HashMap<>();
        repair.put("title", "Test repair 1");
        repair.put("customer_name", "John Doe");
        repair.put("customer_phone", "12345678");
        repair.put("job_description", "Broken chain");
        repair.put("work_time_minutes", 30);
        repair.put("price_per_min", 5.0);
        repair.put("duration", 60);
        repair.put("date", "2025-10-28T13:45:00");
        repair.put("status", Map.of("id", 1));

        // POST /api/jobs/create -> Create the new repair entry
        ResponseEntity<Map> repairResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/jobs/create", // URL for Job creation
                        repair,   // Repair object sent to the DB
                        Map.class // Response expected as JSON Map (contains `id`)
                );

        // Validate that repair creation was successful
        assertEquals(HttpStatus.OK, repairResp.getStatusCode()); // Assert API returned 200 OK
        jobId = (Integer) repairResp.getBody().get("id");        // Extract the Job ID assigned by DB
        assertNotNull(jobId);                                    // Ensure Job ID is not null

        /* Step 2 - Create a Service */
        // Build a Services entity representing a repair service
        Services testpart = new Services(
                "fuld service", // Name of the service
                200.00,                // Price
                60                     // Duration in minutes
        );

        // POST /api/services -> Insert service into DB
        ResponseEntity<Map> productResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/services", // URL for Service creation
                        testpart,  // Service object to insert
                        Map.class  // Response expected as JSON containing `id`
                );

        // Validate service creation succeeded
        assertEquals(HttpStatus.OK, productResp.getStatusCode()); // Ensure API responded with 200 OK
        serviceId = (Integer) productResp.getBody().get("id");    // Extract Service ID
        assertNotNull(serviceId);                                 // Ensure Service ID exists

        /* Step 3 - Assign the Service to the Repair */
        // Build payload that links the new service to the newly created repair
        Map<String, Object> item = new HashMap<>();
        item.put("repairId", jobId);
        item.put("productId", serviceId);
        item.put("quantity", 1);     // Service is added once
        item.put("type", "service"); // API uses this field to differentiate parts from services

        // Validate assignment succeeded
        ResponseEntity<String> addResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/repairs/addProduct",
                        List.of(item),
                        String.class
                );

        // Validate assignment succeeded
        assertEquals(HttpStatus.OK, addResp.getStatusCode()); // API should return 200 OK

        /* Step 4 - Validate the JobServices Entry in the Database */
        // Fetch all JobServices entries linked to this repair
        List<JobServices> services = jobServiceRepository.findByJobId(jobId);

        // Ensure a JobServices entry was created
        assertFalse(services.isEmpty(), "No JobServices rows were created");

        // Retrieve the first JobServices entry for validation
        JobServices js = services.getFirst();

        // Validate stored relationship data
        assertEquals(serviceId, js.getService().getId()); // Service ID should match the created service
        assertEquals(jobId, js.getJob().getId());         // Job ID should match the created repair
        assertEquals(1, js.getQuantity());      // Quantity should match the expected value (1)
    }
}

