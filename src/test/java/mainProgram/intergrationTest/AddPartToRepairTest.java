package mainProgram.intergrationTest; // Project Organization

/* --- Imports --- */
import mainProgram.repository.JobPartRepository;
import mainProgram.table.JobPart;
import mainProgram.table.Product;
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

/* --- AddPartToRepairTest Class --- */
// Integration test that verifies the workflow of creating a repair,
// creating a part, and successfully linking that part to the repair.
// The test ensures correct REST API behavior and validates that the
// JobPart entry is stored correctly in the database.

// Run the full Spring Boot app for integration tests on a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Use application-test.properties for isolated test DB
public class AddPartToRepairTest {
    // Random Port used by the test server
    @LocalServerPort // Injects the random port used by the test server.
    private int port;

    // Client used to send HTTP requests during test
    @Autowired // Injects required Spring-managed components automatically
    private TestRestTemplate restTemplate;

    // Used to verify DB state after part assignment
    @Autowired // Injects required Spring-managed components automatically
    private JobPartRepository jobPartRepository;

    // Will store IDs created during the test
    private Integer jobId;
    private Integer partId;

    /* TEST 1 - Create a Repair, Create a Part, and Add the Part to the Repair */
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
        repair.put("date", "2025-10-28T13:45:00");      // Timestamp the API can parse
        repair.put("status", Map.of("id", 1)); // Reference existing JobStatus by ID

        // POST to /api/jobs/create -> Create the new repair entry
        ResponseEntity<Map> repairResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/jobs/create",  // URL for the endpoint that handles Job creation
                        repair,   // Job object we are trying to create in DB
                        Map.class // Response expected as JSON Map (contains `id`)
                );

        // Verify service was created successfully
        assertEquals(HttpStatus.OK, repairResp.getStatusCode()); // Check if API returned 200 OK
        jobId = (Integer) repairResp.getBody().get("id");        // Extract the job ID
        assertNotNull(jobId);                                    // Ensure ID exist, meaning insert was successful


        /* Step 2 - Create a Part (Product) */
        // Build a Product entity representing a physical part stored in inventory
        Product testpart = new Product(
                "4321", // Product Number
                "forlygte",           // Name
                "12345678",           // EAN
                "lys",                // Category
                400.00                // Price
        );

        // POST /api/products -> Insert Product into DB
        ResponseEntity<Map> productResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/products", // URL for the endpoint that handles Part creation
                        testpart, // Part (Product) object we are trying to create in DB
                        Map.class // Response expected as JSON Map (contains `id`)
                );

        // Validate part creation succeeded
        assertEquals(HttpStatus.OK, productResp.getStatusCode()); // Check if API returned 200 OK
        partId = (Integer) productResp.getBody().get("id");       // Extract the part ID
        assertNotNull(partId);                                    // Ensure ID exist, meaning insert was successful

        /* Step 3 - Assign the Part to the Repair */
        // Build payload specifying which repair to assign the part to
        Map<String, Object> item = new HashMap<>();
        item.put("repairId", jobId);
        item.put("productId", partId);
        item.put("quantity", 3);  // Customer needs 3 units of this part
        item.put("type", "part"); // API uses this to differentiate between parts and services

        // POST /api/repairs/addProduct -> Create JobPart row
        ResponseEntity<String> addResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/repairs/addProduct", // URL for the endpoint that handles JobPart creation
                        List.of(item), // JobPart object we are trying to create in DB
                        String.class   // Response expected as JSON Map (contains `id`)
                );

        // Validate assignment succeeded
        assertEquals(HttpStatus.OK, addResp.getStatusCode());  // Check if API returned 200 OK

        /* Step 4 - Validate the JobPart Entry in the Database */
        // Fetch all JobPart relations linked to this repair
        List<JobPart> parts = jobPartRepository.findByJobId(jobId);

        // Ensure a JobPart entry was created
        assertFalse(parts.isEmpty(), "No JobPart rows were created");

        // Use the first relation row for validation
        JobPart jp = parts.getFirst();

        // Validate the relationship data is stored correctly
        assertEquals(partId, jp.getProduct().getId()); // Ensure that the Part ID Matches the JobPart ID
        assertEquals(jobId, jp.getJob().getId());      // Ensure that the Job ID Matches the JobPart's Job ID
        assertEquals(3, jp.getQuantity());   // Ensure the quantity of the part is set to the expected 3 units in this example
    }
}

