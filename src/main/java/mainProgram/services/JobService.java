package mainProgram.services; // Project Organization

/* --- Imports --- */
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import mainProgram.repository.*;
import mainProgram.table.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import mainProgram.repository.*;
import mainProgram.table.*;

/* --- JobService Class --- */
// Service Layer containing all business logic for Jobs/Repairs.
// Handles creation, updating, searching, and linking products/services to Jobs.
@Service
public class JobService implements BaseSearchService<Job> {
    /// Attributes: Repositories for all related tables
    private final JobRepository jobRepository;
    private final JobPartRepository jobPartRepository;
    private final JobServiceRepository jobServiceRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final JobStatusRepository statusRepository;

    /// Constructor for Dependency Injection
    public JobService(JobRepository jobRepository, JobPartRepository jobPartRepository, JobServiceRepository jobServiceRepository, ProductRepository productRepository, ServiceRepository serviceRepository, JobStatusRepository statusRepository) {
        this.jobRepository = jobRepository;
        this.jobPartRepository = jobPartRepository;
        this.jobServiceRepository = jobServiceRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.statusRepository = statusRepository;
    }

    /// Methods: CRUD Operations + Search

    // Get All Jobs - GET
    /** @return Every job in the system **/
    public List<Job> getAllJobs() {
        // Return every job in the system
        return jobRepository.findAll();
    }

    // Create Job - CREATE
    // Handles creation of a job and optionally attaches service items
    /** @param body a map containing all fields needed to create a Job **/
    /** @return ResponseEntity containing the created Job or an error message **/
    public ResponseEntity<Job> createJob(Map<String, Object> body) {
        try {
            /* --- Extract Required Main Job Data --- */
            // Validate essential fields from the incoming request
            String title = (String) body.get("title");                                // Job title
            String dateString = (String) body.get("date");                            // Job date as ISO string
            Map<String, Object> statusMap = (Map<String, Object>) body.get("status"); // Job status

            // Return 400 Bad Request if required fields are missing
            if (title == null || dateString == null || statusMap == null) {
                return ResponseEntity.badRequest().build();
            }

            // Convert status object to actual JobStatus entity from Database
            Integer statusId = (Integer) statusMap.get("id");
            JobStatus status = statusRepository.findById(statusId.shortValue())
                    .orElseThrow(() -> new RuntimeException("Invalid status ID"));

            // The Default duration is set to 60 minutes if no durations are provided or 0
            Integer duration = (body.get("duration") == null || (Integer) body.get("duration") == 0) ? 60 : (Integer) body.get("duration");

            /* --- Create Job Entity --- */
            Job job = new Job();
            job.setTitle(title);                                                         // Set Job title
            job.setCustomer_name((String) body.get("customer_name"));                    // Customer
            job.setCustomer_phone((String) body.get("customer_phone"));                  // Customer name
            job.setJob_description((String) body.get("job_description"));                // Job description
            job.setWork_time_minutes((Integer) body.get("work_time_minutes"));           // Work time
            job.setPrice_per_minute(((Number) body.get("price_per_min")).doubleValue()); // Price/min
            job.setDuration(duration);                                                   // Duration in minutes
            job.setDate(LocalDateTime.parse(dateString));                                // Convert ISO string to LocalDateTime
            job.setStatus(status);                                                       // Set JobStatus entity

            // Save the job to the database
            Job savedJob = jobRepository.save(job);

            /* --- Attaching Services if Provided --- */
            // List of services
            List<Map<String, Object>> services = (List<Map<String, Object>>) body.get("services");

            // Checking if services are provided
            if (services != null) {
                // For each service in the list of services do:
                for (Map<String, Object> s : services) {
                    // Get Service ID
                    Integer serviceId = (Integer) s.get("id");

                    // Default quantity = 1
                    Integer quantity = (Integer) s.getOrDefault("quantity", 1);

                    // Fetch service entity, throw error if not found
                    Services service = serviceRepository.findById(serviceId)
                            .orElseThrow(() -> new RuntimeException("Service not found"));

                    // Save JobServices linking the job to a service with quantity
                    jobServiceRepository.save(new JobServices(savedJob, service, quantity));
                }
            }

            // Return the saved job as the response
            return ResponseEntity.ok(savedJob);
        }
        // Catch Exception if any
        catch (Exception e) {
            // Log error for debugging
            e.printStackTrace();

            // Return 500 Internal Server Error
            return ResponseEntity.internalServerError().build();
        }
    }

    // Update Job - UPDATE
    // Handles updating an existing Job entity with new values.
    // Only non-null and non-blank fields are updated.
    /** @param id the ID of the job to update **/
    /** @param job the Job object containing the updated values **/
    /** @return ResponseEntity containing the updated Job, or 404 if not found **/
    public ResponseEntity<Job> updateJob(Integer id, Job job) {
        // Fetch existing job from the database by ID
        Job existing = jobRepository.findById(id).orElse(null);

        // Return a 404 Not Found if the job does not exist
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        /* --- Update Fields if Provided --- */
        // Update the title if not null or blank
        if (job.getTitle() != null && !job.getTitle().isBlank()) {
            existing.setTitle(job.getTitle());
        }

        // Update the customer name if provided
        if (job.getCustomer_name() != null && !job.getCustomer_name().isBlank()) {
            existing.setCustomer_name(job.getCustomer_name());
        }

        // Update the customer phone number if provided
        if (job.getCustomer_phone() != null && !job.getCustomer_phone().isBlank()) {
            existing.setCustomer_phone(job.getCustomer_phone());
        }

        // Update the work time minutes if provided
        if (job.getWork_time_minutes() != null) {
            existing.setWork_time_minutes(job.getWork_time_minutes());
        }

        // Update the duration if provided
        if (job.getDuration() != null) {
            existing.setDuration(job.getDuration());
        }

        // Update the price per minute if provided
        if (job.getPrice_per_minute() != null) {
            existing.setPrice_per_minute(job.getPrice_per_minute());
        }

        // Update the date if provided
        if (job.getDate() != null) {
            existing.setDate(job.getDate());
        }

        // Update the job description if provided and not blank
        if (job.getJob_description() != null && !job.getJob_description().isBlank()) {
            existing.setJob_description(job.getJob_description());
        }

        // Update the status if provided and the status ID is valid
        if (job.getStatus() != null && job.getStatus().getId() != null) {
            existing.setStatus(job.getStatus());
        }

        // Save the updated job entity to the database
        Job updated = jobRepository.save(existing);

        // Return the updated job as the response
        return ResponseEntity.ok(updated);
    }

    // Update Job Description Only - UPDATE
    // Handles updating only the job_description field of a Job
    /** @param id the ID of the Job to update **/
    /** @param description the new job description **/
    /** @return ResponseEntity containing the updated Job or 404 if not found **/
    public ResponseEntity<Job> updateJobDescription(Integer id, String description) {
        /* --- Fetch Job from Database --- */
        // Try to find the Job by ID
        Job job = jobRepository.findById(id).orElse(null);

        // Return a 404 Not Found if the Job does not exist
        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        /* --- Update Job Description --- */
        // Set the new job description
        job.setJob_description(description);

        // Save the updated Job and return it in the response
        return ResponseEntity.ok(jobRepository.save(job));
    }

    // Add Products to Repair - POST
    // Handles adding parts or services to an existing repair
    /** @param dataList a list of maps containing repairId, productId, type ("part" or "service"), and optional quantity **/
    /** @return ResponseEntity with a success message or error for an invalid type **/
    public ResponseEntity<String> addProductsToRepair(List<Map<String, Object>> dataList) {
        // Iterate through each item in the input list
        for (Map<String, Object> data : dataList) {
            Integer repairId = (Integer) data.get("repairId");                              // ID of the repair
            Integer productId = (Integer) data.get("productId");                            // ID of the part/service
            Integer quantity = (Integer) data.getOrDefault("quantity", 1); // Quantity, Default = 1
            String type = (String) data.get("type");                                        // Type: "part" or "service"

            // Call the appropriate helper method based on type
            if ("service".equals(type)) {
                // Add a service
                addServiceToRepair(repairId, productId, quantity);
            }
            else if ("part".equals(type)) {
                // Add a part
                addPartToRepair(repairId, productId, quantity);
            }
            else {
                // Return 400 Bad Request for invalid type
                return ResponseEntity.badRequest().body("Invalid product type");
            }
        }

        // Return a success response if all items were added
        return ResponseEntity.ok("Products added to repair successfully");
    }

    // Remove Products From Repair - POST
    // Handles removing parts or services from an existing repair/job
    /** @param dataList a list of maps containing repairId, productId, and type ("part" or "service") **/
    /** @return ResponseEntity with a success message or an error for an invalid type **/
    public ResponseEntity<String> removeProductsFromRepair(List<Map<String, Object>> dataList) {
        // Iterate though each item in the input list
        for (Map<String, Object> data : dataList) {
            Integer repairId = (Integer) data.get("repairId");   // ID of the repair
            Integer productId = (Integer) data.get("productId"); // ID of the part/service
            String type = (String) data.get("type");             // Type: "part" or "service"

            // Call the appropriate helper method based on type
            if ("service".equals(type)) {
                // Remove a service
                removeServiceFromRepair(repairId, productId);
            }
            else if ("part".equals(type)) {
                // Remove a part
                removePartFromRepair(repairId, productId);
            }
            else {
                // Return 400 Bad Request for invalid type
                return ResponseEntity.badRequest().body("Invalid product type");
            }
        }

        // Return success response if all items were removed
        return ResponseEntity.ok("Product(s) removed successfully");
    }

    // Add Part to Repair - HELPER METHOD
    // Adds a part to a repair, or increments quantity if already exists
    /** @param repairID ID of the repair **/
    /** @param partId ID of the part to add **/
    /** @param quantity quantity to add **/
    public void addPartToRepair(int repairId, int partId, int quantity) {
        // Fetch the repair entity, throw the exception if not found
        Job repair = jobRepository.findById(repairId)
                .orElseThrow(() -> new RuntimeException("Repair not found"));

        // Fetch the part/Product entity, throw exception if not found
        Product part = productRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        // Check if the part already exists for this job
        JobPart existing = jobPartRepository.findByJobId(repairId)
                .stream() // Convert the List<JobPart> into a Stream for functional operations
                .filter(x -> x.getProduct().getId() == partId) // Keep only the JobParts whose part ID matches the one being added
                .findFirst() // Return the first matching JobPart wrapped in Optional, if any exist
                .orElse(null); // If no match is found, return null instead of Optional

        // Check if the part already exists for this job
        if (existing != null) {
            // Increment quantity if the part already is linked to the Job
            existing.addQuantity(quantity);

            // Save part to job
            jobPartRepository.save(existing);
        }
        else {
            // Otherwise, create a new JobPart linking repair and part
            jobPartRepository.save(new JobPart(repair, part, quantity));
        }
    }

    // Add Service to Repair - HELPER METHOD
    // Adds a service to a repair/job, or increments quantity if already exists
    /** @param repairId ID of the repair **/
    /** @param serviceId ID of the service to add **/
    /** @param quantity quantity to add **/
    public void addServiceToRepair(int repairId, int serviceId, int quantity) {
        // Fetch the repair entity, throw the exception if not found
        Job repair = jobRepository.findById(repairId)
                .orElseThrow(() -> new RuntimeException("Repair not found"));

        // Fetch the service entity, throw exception if not found
        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Check if the service already exists for this job
        JobServices existing = jobServiceRepository.findByJobId(repairId)
                .stream() // Convert the List<JobServices> into a Stream for functional operations
                .filter(x -> x.getService().getId() == serviceId) // Keep only the JobServices whose service ID matches the one being added
                .findFirst() // Return the first matching JobServices wrapped in Optional, if any exist
                .orElse(null); // If no match is found, return null instead of Optional

        // Check if the service already exists for this job
        if (existing != null) {
            // Increment quantity if the service already is linked to the job
            existing.addQuantity(quantity);

            // Save service to job
            jobServiceRepository.save(existing);
        }
        else {
            // Otherwise, create a new JobServices linking repair and service
            jobServiceRepository.save(new JobServices(repair, service, quantity));
        }
    }

    // Remove Part From Repair - HELPER METHOD
    // Removes a part from a repair
    /** @param repairId ID of the repair **/
    /** @param partId ID of the part to remove **/
    public void removePartFromRepair(int repairId, int partId) {
        // Verify the repair exists
        if (!jobRepository.existsById(repairId)) {
            throw new RuntimeException("Repair not found: " + repairId);
        }

        // Verify the part exists
        if (!productRepository.existsById(partId)) {
            throw new RuntimeException("Part not found: " + partId);
        }

        // Delete the JobPart linking the job and the part
        jobPartRepository.deleteByJobIdAndProductId(repairId, partId);
    }

    // Remove Service From Repair - HELPER METHOD
    // Removes a service from a repair
    /** @param repairId ID of the repair **/
    /** @param serviceId ID of the service to remove **/
    public void removeServiceFromRepair(int repairId, int serviceId) {
        // Verify the repair exists
        if (!jobRepository.existsById(repairId)) {
            throw new RuntimeException("Repair not found: " + repairId);
        }

        // Verify the service exists
        if (!serviceRepository.existsById(serviceId)) {
            throw new RuntimeException("Service not found: " + serviceId);
        }

        // Delete the JobServices linking the job and the service
        jobServiceRepository.deleteByJobIdAndServiceId(repairId, serviceId);
    }

    // Delete Job - DELETE
    // Deletes a job along with all linked parts and services
    /** @param id ID of the job to delete **/
    /** @return ResponseEntity of teh job to delete **/
    public ResponseEntity<Void> deleteJob(int id) {
        // Return 404 if the job does not exist
        if (!jobRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Remove all associated parts
        jobPartRepository.deleteByJobId(id);

        // Remove all associated services
        jobServiceRepository.deleteByJobId(id);

        // Delete the job itself
        jobRepository.deleteById(id);

        // Return 204 No Content to indicate successful deletion
        return ResponseEntity.noContent().build();
    }

    // Custom Search Method for Job/Repair
    // Searches for jobs by title containing a keyword (case-sensitive)
    /** @param keyword the search string **/
    /** @return list of matching Job entities **/
    @Override
    public List<Job> search(String keyword) {
        // Return empty list if keyword is null or blank
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        // Perform case-insensitive search in job titles
        return jobRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Get Job by ID - HELPER METHOD
    // Retrieves a Job entity by its ID
    /** @param id the ID of the job to retrieve **/
    /** @return Job entity if found **/
    /** @throws RuntimeException if no job is found **/
    public Job getJobById(int id) {
        // Use repository to find the job by ID
        // Throws an exception if the job does not exist
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    // Get Parts Associated with a Job - HELPER METHOD
    // Retrieves all parts linked to a specific repair
    /** @param jobId  ID of the job **/
    /** @return list of JobPart entities associated with the job **/
    public List<JobPart> getPartsForJob(int jobId) {
        // Query repository for all JobPart entries for this job
        return jobPartRepository.findByJobId(jobId);
    }
}
