package mainProgram.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.transaction.Transactional;
import mainProgram.repository.JobRepository;
import mainProgram.repository.JobServiceRepository;
import mainProgram.repository.JobStatusRepository;
import mainProgram.repository.ServiceRepository;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import mainProgram.table.JobServices;
import mainProgram.table.JobStatus;
import mainProgram.table.Services;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing job-related operations.
 * Provides endpoints for creating, reading, and updating job records.
 */
@Controller
@RequestMapping("/")
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);
    private final JobRepository jobRepository;
    private final JobStatusRepository statusRepository;
    private final JobService jobService;
    private final ServiceRepository serviceRepository;
    private final JobServiceRepository jobServiceRepository;

    /**
     * Constructor for dependency injection.
     *
     * @param jobRepository    the repository for job database operations
     * @param statusRepository the repository for job status database operations
     */
    public JobController(JobRepository jobRepository, JobStatusRepository statusRepository, JobService jobService, ServiceRepository serviceRepository, JobServiceRepository jobServiceRepository) {
        this.jobRepository = jobRepository;
        this.statusRepository = statusRepository;
        this.jobService = jobService;
        this.serviceRepository = serviceRepository;
        this.jobServiceRepository = jobServiceRepository;
    }

    /**
     * Retrieves all jobs from the database.
     *
     * @return a list of all jobs
     */
    @GetMapping("api/jobs")
    @ResponseBody
    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    @PostMapping("/api/jobs/create")
    @ResponseBody
    public ResponseEntity<Job> createJob(@RequestBody Map<String, Object> body) {
        try {
            // --- Extract main job data ---
            String title = (String) body.get("title");
            String dateString = (String) body.get("date");
            Map<String, Object> statusMap = (Map<String, Object>) body.get("status");

            if (title == null || dateString == null || statusMap == null) {
                return ResponseEntity.badRequest().build();
            }

            Integer statusId = (Integer) statusMap.get("id");
            if (statusId == null) {
                return ResponseEntity.badRequest().build();
            }

            JobStatus status = statusRepository.findById(statusId.shortValue())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid status_id"));

            // --- Create Job entity ---
            Job job = new Job();
            job.setTitle(title);
            job.setCustomer_name((String) body.get("customer_name"));
            job.setCustomer_phone((String) body.get("customer_phone"));
            job.setJob_description((String) body.get("job_description"));
            job.setDate(LocalDateTime.parse(dateString));
            job.setStatus(status);

            Job savedJob = jobRepository.save(job);

            // --- Attach services if provided ---
            List<Map<String, Object>> services = (List<Map<String, Object>>) body.get("services");
            if (services != null) {
                for (Map<String, Object> s : services) {
                    Integer serviceId = (Integer) s.get("id");
                    Integer quantity = (Integer) s.getOrDefault("quantity", 1);

                    if (serviceId != null) {
                        Services service = serviceRepository.findById(serviceId)
                                .orElseThrow(() -> new RuntimeException("Service not found"));
                        jobServiceRepository.save(new JobServices(savedJob, service, quantity));
                    }
                }
            }

            return ResponseEntity.ok(savedJob);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Updates only the job description for a specific job.
     * This is a partial update endpoint focused on the description field.
     *
     * @param id  the ID of the job to update
     * @param job the job object containing the new description
     * @return ResponseEntity containing the updated job if found, or a not found response
     */
    @PutMapping("api/jobs/{id}/description")
    @ResponseBody
    public ResponseEntity<Job> updateJobDesc(@PathVariable Integer id, @RequestBody Job job) {
        return jobRepository
                .findById(id)
                .map((existing) -> {
                    // Update only the description field
                    existing.setJob_description(job.getJob_description());

                    Job updated = jobRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // todo: Aad logic so that if the "link" already exists, a new should not be added but the amount should be updated.
    @PostMapping("/api/repairs/addProduct")
    public ResponseEntity<String> addProductsToRepair(@RequestBody List<Map<String, Object>> dataList) {
        for (Map<String, Object> data : dataList) {
            Integer repairId = (Integer) data.get("repairId");
            Integer productId = (Integer) data.get("productId");
            Integer quantity = (Integer) data.getOrDefault("quantity", 1);
            String productType = (String) data.get("type");

            if (Objects.equals(productType, "service")) {
                jobService.addServiceToRepair(repairId, productId, quantity);
            } else if (Objects.equals(productType, "part")) {
                jobService.addPartToRepair(repairId, productId, quantity);
            } else {
                throw new Error("undefined product type");
            }

        }

        return ResponseEntity.ok("Products added to repair successfully");
    }

    @PostMapping("/api/repairs/removeProduct")
    @Transactional
    public ResponseEntity<String> removeProductFromRepair(@RequestBody List<Map<String, Object>> dataList) {
        for (Map<String, Object> data : dataList) {
            Integer repairId = (Integer) data.get("repairId");
            Integer productId = (Integer) data.get("productId");
            String productType = (String) data.get("type");


            if (Objects.equals(productType, "service")) {
                jobService.removeServiceFromRepair(repairId, productId);
            } else if (Objects.equals(productType, "part")) {
                jobService.removePartFromRepair(repairId, productId);
            } else {
                throw new Error("undefined product type");
            }
        }

        return ResponseEntity.ok("Product(s) removed from repair successfully");
    }
}
