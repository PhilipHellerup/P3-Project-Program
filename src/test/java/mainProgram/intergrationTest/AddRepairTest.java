package mainProgram.intergrationTest;

import mainProgram.repository.JobRepository;
import mainProgram.repository.JobServiceRepository;
import mainProgram.table.Job;
import mainProgram.table.JobServices;
import mainProgram.table.Product;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AddRepairTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobServiceRepository jobServiceRepository;

    private Integer serviceId;

    @Test
    void createsJobCorrectly() {

        // Create JSON payload for job
        Map<String, Object> repair = new HashMap<>();
        repair.put("title", "Test repair 1");
        repair.put("customer_name", "John Doe");
        repair.put("customer_phone", "12345678");
        repair.put("job_description", "Broken chain");
        repair.put("work_time_minutes", 30);
        repair.put("price_per_min", 5.0);
        repair.put("duration", 60);
        repair.put("date", "2025-10-28T13:45:00");

        // add a status object
        repair.put("status", Map.of("id", 1));

        // Create a service to add
        Services testpart = new Services("fuld service", 200.00, 60 );

        ResponseEntity<Map> productResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/services",
                        testpart,
                        Map.class
                );

        assertEquals(HttpStatus.OK, productResp.getStatusCode());
        serviceId = (Integer) productResp.getBody().get("id");
        assertNotNull(serviceId);

        // Single service: id=1, quantity=2
        repair.put(
                "services",
                List.of(
                        Map.of(
                                "id", serviceId,
                                "quantity", 2
                        )
                )
        );

        // Send POST request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://localhost:" + port + "/api/jobs/create";

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, new HttpEntity<>(repair, headers), Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("id"));

        Integer jobId = (Integer) response.getBody().get("id");
        assertNotNull(jobId);


        // Validate Job in DB
        Job saved = jobRepository.findById(jobId).orElse(null);
        assertNotNull(saved, "Job was not inserted into database");

        assertEquals("Test repair 1", saved.getTitle());
        assertEquals("John Doe", saved.getCustomer_name());
        assertEquals(30, saved.getWork_time_minutes());
        assertEquals(5.0, saved.getPrice_per_minute());


        // Validate Service join-table
        List<JobServices> jobServices = jobServiceRepository.findByJobId(jobId);
        assertFalse(jobServices.isEmpty(), "No JobServices rows created");

        JobServices js = jobServices.getFirst();

        assertEquals(1, js.getService().getId());
        assertEquals("Test repair 1", js.getJob().getTitle());
        assertEquals("fuld service", js.getService().getName());
        assertEquals(2, js.getQuantity());

        System.out.println("Repair created and service assigned successfully");
    }
}
