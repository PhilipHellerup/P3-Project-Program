package mainProgram.intergrationTest;

import mainProgram.repository.JobPartRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class getRepairDetailsTest {
    @Autowired
    MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Integer jobId;

    @Test
    void jobTitleAppearsInRenderedHtml() throws Exception {
        int id = createTestRepairGetId();
        MvcResult result = mockMvc.perform(get("/jobliste/" + id))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(html);

        // Check the element where the title is rendered
        String title = doc.select("#job-title").text();

        assertEquals("Test repair 1", title);
    }

    @Test
    void jobCustomerNameAppearsInRenderedHtml() throws Exception {
        int id = createTestRepairGetId();
        MvcResult result = mockMvc.perform(get("/jobliste/" + id))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(html);

        // Check the element where the customer name is rendered
        String name = doc.select("#job-customer-name").text();

        assertEquals("John Doe", name);
    }

    @Test
    void jobCustomerPhoneAppearsInRenderedHtml() throws Exception {
        int id = createTestRepairGetId();
        MvcResult result = mockMvc.perform(get("/jobliste/" + id))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(html);

        // Check the element where the phone number is rendered
        String phone = doc.select("#job-customer-phone").text();

        assertEquals("12345678", phone);
    }

    public int createTestRepairGetId(){
            // Create a repair
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

            ResponseEntity<Map> repairResp =
                    restTemplate.postForEntity(
                            "http://localhost:" + port + "/api/jobs/create",
                            repair,
                            Map.class
                    );

            assertEquals(HttpStatus.OK, repairResp.getStatusCode());
            jobId = (Integer) repairResp.getBody().get("id");
            assertNotNull(jobId);

            return jobId;
    }
}

