package mainProgram.intergrationTest; // Project Organization

/* --- Imports --- */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* --- RepairDetailsIntegrationTest Class --- */
// Integration test that ensures the repair-details page correctly renders
// all information about a specific repair. The test creates a repair through
// the REST API, loads the details page using MockMvc, extracts displayed fields
// using Jsoup, and verifies that each field is shown with the correct value.

// This verifies the link between:
// (1) Database values → (2) Controller → (3) Thymeleaf view → (4) Rendered HTML.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Use isolated test database config
@AutoConfigureMockMvc   // Enable MockMvc for page rendering tests
public class RepairDetailsIntegrationTest {
    // MockMvc sends fake browser requests to test controllers and templates
    @Autowired  // MockMvc instance is automatically created and managed by Spring during tests
    MockMvc mockMvc;

    // Random Port used by the test server
    @LocalServerPort // Injects the random port used by the test server.
    private int port;

    // Client used for creating repairs directly through REST API during setup
    @Autowired // Injects required Spring-managed components automatically
    private TestRestTemplate restTemplate;

    /* TEST 1 - Create a Repair and Verify That All Details Render Correctly */
    @Test
    void repairDetailsRenderCorrectly() throws Exception {
        /* Step 1 - Create a Repair via REST API */
        // Build JSON payload representing a repair creation request
        Map<String, Object> repair = new HashMap<>();
        repair.put("title", "Test repair 1");
        repair.put("customer_name", "John Doe");
        repair.put("customer_phone", "12345678");
        repair.put("job_description", "Broken chain");
        repair.put("work_time_minutes", 30);
        repair.put("price_per_min", 5.0);
        repair.put("duration", 60);
        repair.put("date", "2025-10-28T13:45:00");     // ISO timestamp
        repair.put("status", Map.of("id", 1)); // Repair status reference (ID 1)

        // POST /api/jobs/create -> Insert repair into the database
        ResponseEntity<Map> repairResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/jobs/create", // URL for Job creation
                        repair,   // Repair object sent to the DB
                        Map.class // Response expected as JSON Map (contains `id`)
                );

        // Validate API successfully stored the repair
        assertEquals(HttpStatus.OK, repairResp.getStatusCode());  // Assert API returned 200 OK
        Integer jobId = (Integer) repairResp.getBody().get("id"); // Extract repair ID
        assertNotNull(jobId);                                     // Ensure DB assigned a valid ID

        /* Step 2 - Load the repair details page: /jobliste/{id} */
        // Use MockMvc to request the HTML details page for our new repair
        MvcResult result = mockMvc.perform(get("/jobliste/" + jobId))
                .andExpect(status().isOk()) // Ensure controller returns 200 OK
                .andReturn(); // Retrieve full response including HTML content

        // Extract raw HTML as string
        String html = result.getResponse().getContentAsString();

        // Parse HTML into a Jsoup document for DOM inspection
        Document doc = Jsoup.parse(html);

        /* Step 3 - Read the rendered values from their corresponding HTML fields */
        // Each value is selected using its element ID
        String title = doc.select("#job-title").text();
        String customer = doc.select("#job-customer-name").text();
        String phone = doc.select("#job-customer-phone").text();
        String totalCost = doc.select("#total-cost").text(); // Parse this to a double

        /* Step 4 - Convert the total price ("150 kr.") into a double for comparison */
        // Remove currency label and whitespace
        String cleaned = totalCost.replace("kr.", "").trim();

        // Replace comma with dot in case formatting uses "," as decimal separator
        cleaned = cleaned.replace(",", ".");

        // Convert cleaned string into a numeric value
        double value = Double.parseDouble(cleaned);

        /* Step 5 - Validate all rendered fields match expected values */
        assertEquals("Test repair 1", title);
        assertEquals("John Doe", customer);
        assertEquals("12345678", phone);
        assertEquals((5.00 * 30), value); // Work price = price_per_min * work_time_minutes
    }
}
