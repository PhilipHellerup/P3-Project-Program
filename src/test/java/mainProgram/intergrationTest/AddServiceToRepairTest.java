package mainProgram.intergrationTest;

import jakarta.servlet.http.Part;
import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import mainProgram.repository.JobServiceRepository;
import mainProgram.services.JobService;
import mainProgram.table.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AddServiceToRepairTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JobServiceRepository jobServiceRepository;

    private Integer jobId;
    private Integer serviceId;


    @Test
    void addPartToRepair() {

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


        // Create a service
        Services testpart = new Services(
                "fuld service", 200.00, 60
        );

        ResponseEntity<Map> productResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/services",
                        testpart,
                        Map.class
                );

        assertEquals(HttpStatus.OK, productResp.getStatusCode());
        serviceId = (Integer) productResp.getBody().get("id");
        assertNotNull(serviceId);


        // Add the service to the repair
        Map<String, Object> item = new HashMap<>();
        item.put("repairId", jobId);
        item.put("productId", serviceId);
        item.put("quantity", 1);
        item.put("type", "service");

        ResponseEntity<String> addResp =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/api/repairs/addProduct",
                        List.of(item),
                        String.class
                );

        assertEquals(HttpStatus.OK, addResp.getStatusCode());


        // Verify DB result
        List<JobServices> services = jobServiceRepository.findByJobId(jobId);

        assertFalse(services.isEmpty());
        JobServices js = services.getFirst();

        assertEquals(serviceId, js.getService().getId());
        assertEquals(jobId, js.getJob().getId());
        assertEquals(1, js.getQuantity());
    }
}

