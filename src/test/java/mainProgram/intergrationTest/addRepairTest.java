package mainProgram.intergrationTest;

import mainProgram.repository.JobRepository;
import mainProgram.repository.JobServiceRepository;
import mainProgram.table.Job;
import mainProgram.table.JobServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class addRepairTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobServiceRepository jobServiceRepository;

    @Test
    void createsJobCorrectly() {
        // --------- Build JSON body like your frontend does ---------
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "Test repair 1");
        payload.put("customer_name", "John Doe");
        payload.put("customer_phone", "12345678");
        payload.put("job_description", "Broken chain");
        payload.put("work_time_minutes", 30);
        payload.put("price_per_min", 5.0);
        payload.put("duration", 60);
        payload.put("date", "2025-10-28T13:45:00");

        // Status object: {id: X}
        Map<String, Object> statusObj = new HashMap<>();
        statusObj.put("id", 1);
        payload.put("status", statusObj);

        // Services array: [{id: 1, quantity: 2
        // Added a test service with id=1 and name="Fuld Service"
        List<Map<String, Object>> services = new ArrayList<>();
        Map<String, Object> s1 = new HashMap<>();
        s1.put("id", 1);
        s1.put("quantity", 2);
        services.add(s1);

        payload.put("services", services);

        // --------- Prepare HTTP request ---------
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        String url = "http://localhost:" + port + "/api/jobs/create";

        // --------- Call the actual endpoint ---------
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        // --------- Validate HTTP Response ---------
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("id"));

        Integer createdId = (Integer) response.getBody().get("id");

        // --------- Validate Database ---------
        Optional<Job> repair = jobRepository.findById(createdId);
        assertTrue(repair.isPresent());

        Job job = repair.get();
        assertEquals("Test repair 1", job.getTitle());
        assertEquals("John Doe", job.getCustomer_name());
        assertEquals(30, job.getWork_time_minutes());
        assertEquals(5.0, job.getPrice_per_minute());
        System.out.println("Job added successfully to database table");


        // --------- Validate that the service was added to the repair --------
        List<JobServices> jobServices = jobServiceRepository.findByJobId(createdId);
        assertFalse(jobServices.isEmpty());

        assertEquals(1, jobServices.getFirst().getService().getId());
        assertEquals("Test repair 1", jobServices.getFirst().getJob().getTitle());
        assertEquals("Fuld Service", jobServices.getFirst().getService().getName());
        assertEquals(2, jobServices.getFirst().getQuantity());
        System.out.println("Service added successfully to join-table");
    }
}
