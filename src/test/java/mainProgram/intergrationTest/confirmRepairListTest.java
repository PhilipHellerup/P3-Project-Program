package mainProgram.intergrationTest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class confirmRepairListTest {
    private static final Logger log = LoggerFactory.getLogger(confirmRepairListTest.class);
    @Autowired
    MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // 5 test repairs are created using the method "createTestRepair", the title of each repair is set to a unique value
    @Test
    void allRepairsAreShownOnRepairList() throws Exception{
        List<String> controlRepairs = new ArrayList<>(); // Also add the titles to a control List to match with later
        for (int i = 0; i < 5; i++) {
            String title = "test" + (i+1);
            createTestRepair(title);
            controlRepairs.add(title);
        }

        // Get the html information for the repairList page
        MvcResult result = mockMvc.perform(get("/jobliste"))
                .andExpect(status().isOk())
                .andReturn();

        String html = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(html);

        // get all elements with the class job-title and add them to a list
        Elements repairTitles = doc.select(".job-title");
        List<String> titles = new ArrayList<>();

        for (Element el : repairTitles) {
            titles.add(el.text());
        }


        // Confirm that the length of the list is 5, corresponding to the 5 repair created
        assertEquals(5, titles.size());
        // Confirm that the list equals the control
        assertEquals(titles, controlRepairs);
        System.out.println("Titles from page:");
        System.out.println(titles);
        System.out.println("Control titles:");
        System.out.println(controlRepairs);

    }

    // Method to create a new test repair
    public void createTestRepair(String title){
        // Create a repair
        Map<String, Object> repair = new HashMap<>();
        repair.put("title", title);
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
        assertNotNull(repairResp.getBody().get("id"));
    }
}
