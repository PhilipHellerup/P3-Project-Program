package mainProgram.controller;

import java.util.List;
import java.util.Map;

import mainProgram.repository.JobRepository;
import mainProgram.repository.JobStatusRepository;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import mainProgram.table.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * Constructor for dependency injection.
     *
     * @param jobRepository    the repository for job database operations
     * @param statusRepository the repository for job status database operations
     */
    public JobController(JobRepository jobRepository, JobStatusRepository statusRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.statusRepository = statusRepository;
        this.jobService = jobService;
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

    /**
     * Creates a new job in the database.
     * Validates that required fields (date, title) are present and that the status exists.
     *
     * @param job the job object to create
     * @return ResponseEntity containing the created job if successful, or a bad request/error response
     * @throws IllegalArgumentException if the provided status_id is invalid
     */
    @PostMapping("api/jobs")
    @ResponseBody
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        // Validate required fields
        if (job.getDate() == null || job.getTitle() == null) return ResponseEntity.badRequest().build();
        // Ensure status is valid
        if (job.getStatus() == null || job.getStatus().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        // Verify the status exists in the database
        JobStatus status = statusRepository
                .findById(job.getStatus().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid status_id"));
        job.setStatus(status);
        Job saved = jobRepository.save(job);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates an existing job with new information.
     * All job fields can be updated including title, customer details, pricing, and status.
     *
     * @param id  the ID of the job to update
     * @param job the job object containing updated values
     * @return ResponseEntity containing the updated job if found, or a not found response
     * @throws IllegalArgumentException if the provided status_id is invalid
     */
    @PutMapping("api/jobs/{id}")
    @ResponseBody
    public ResponseEntity<Job> updateJob(@PathVariable Integer id, @RequestBody Job job) {
        return jobRepository
                .findById(id)
                .map((existing) -> {
                    // Update all job fields
                    existing.setTitle(job.getTitle());
                    existing.setCustomer_name(job.getCustomer_name());
                    existing.setCustomer_phone(job.getCustomer_phone());
                    existing.setJob_description(job.getJob_description());
                    existing.setWork_time_minutes(job.getWork_time_minutes());
                    existing.setDuration(job.getDuration());
                    existing.setPrice_per_minute(job.getPrice_per_minute());
                    existing.setDate(job.getDate());

                    // Update status if provided
                    if (job.getStatus() != null && job.getStatus().getId() != null) {
                        JobStatus status = statusRepository
                                .findById(job.getStatus().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Invalid status_id"));
                        existing.setStatus(status);
                    }

                    Job updated = jobRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
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

            jobService.addProductToRepair(repairId, productId, quantity);
        }

        return ResponseEntity.ok("Products added to repair successfully");
    }
}
