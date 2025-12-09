package mainProgram.intergrationTest; // Project Organization

/* --- Imports --- */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* --- confirmRepairListTest Class --- */
// Integration test that ensures all existing repairs are rendered and visible
// on the repair list page (/jobliste). The test creates 5 repairs with unique
// titles, loads the generated HTML, parses it with Jsoup, and confirms that
// the expected titles are displayed on the page.

// Uses MockMvc for rendering the HTML of controller-driven pages
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Uses application-test.properties for isolated test DB
@AutoConfigureMockMvc   // Auto-configures MockMvc for HTML page testing
public class confirmRepairListTest {
    // Inject MockMvc to issue requests to MVC controllers without a real browser
    @Autowired // MockMvc instance is automatically created and managed by Spring during tests
    MockMvc mockMvc;

    // Random Port used by the test server
    @LocalServerPort // Injects the random port used by the test server.
    private int port;

    // Client used for creating repairs directly through REST API during setup
    @Autowired // Injects required Spring-managed components automatically
    private TestRestTemplate restTemplate;

    /* TEST 1 - Create 5 Repairs and Confirm They Appear on the Repair List Page */
    @Test
    void allRepairsAreShownOnRepairList() throws Exception{
        // Store expected titles in a control list for comparison
        List<String> controlRepairs = new ArrayList<>();

        // Create 5 repairs with unique titles: test1, test2, test3, test4, test5
        for (int i = 0; i < 5; i++) {
            String title = "test" + (i+1);
            createTestRepair(title);   // Create repair using Helper Method
            controlRepairs.add(title); // Store expected title
        }

        /* Step 1 - Retrieve the HTML of /jobliste */
        // Request the repair list page using MockMvc
        MvcResult result = mockMvc.perform(get("/jobliste"))
                .andExpect(status().isOk()) // Ensure controller returns 200 OK
                .andReturn(); // Retrieve full response including HTML content

        // Extract HTML body as a string
        String html = result.getResponse().getContentAsString();

        // Parse HTML using Jsoup for DOM inspection
        Document doc = Jsoup.parse(html);

        /* Step 2 - Extract all repair titles from elements with class "job-title" */
        // Select Job Title DOM elements
        Elements repairTitles = doc.select(".job-title");

        // List for storing job titles
        List<String> titles = new ArrayList<>();

        // Convert each HTML element into a string title
        for (Element el : repairTitles) {
            // Add job title to list
            titles.add(el.text());
        }

        /* Step 3 - Validate that all 5 expected titles appear on the page */
        // Check that 5 titles were found (we created exactly 5 repairs)
        assertEquals(5, titles.size());

        // Ensure that the titles extracted from the HTML match the control list
        assertEquals(titles, controlRepairs);

        // Print output for debugging (useful when the test fails)
        System.out.println("Titles from page:");
        System.out.println(titles);
        System.out.println("Control titles:");
        System.out.println(controlRepairs);
    }

    /* Helper Method - Create a new test repair through the REST API */
    // Used by the test to quickly generate new repairs with unique titles
    public void createTestRepair(String title){
        // Create payload for repair creation
        Map<String, Object> repair = new HashMap<>();
        repair.put("title", title);
        repair.put("customer_name", "John Doe");
        repair.put("customer_phone", "12345678");
        repair.put("job_description", "Broken chain");
        repair.put("work_time_minutes", 30);
        repair.put("price_per_min", 5.0);
        repair.put("duration", 60);
        repair.put("date", "2025-10-28T13:45:00");      // Valid timestamp for API parsing
        repair.put("status", Map.of("id", 1)); // Status reference by ID

        // POST /api/jobs/create -> Insert repair into the database
        ResponseEntity<Map> repairResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/jobs/create", // URL for Job creation
                        repair,   // Repair object sent to the DB
                        Map.class // Response expected as JSON Map (contains `id`)
                );

        // Validate repair creation
        assertEquals(HttpStatus.OK, repairResp.getStatusCode()); // Assert API returned 200 OK
        assertNotNull(repairResp.getBody().get("id"));           // Ensure Repair/Job ID is not null
    }
}
