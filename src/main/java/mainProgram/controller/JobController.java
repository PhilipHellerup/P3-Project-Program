package mainProgram.controller; // Project Organization

/* --- Imports --- */
import java.util.List;
import java.util.Map;
import jakarta.transaction.Transactional;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* --- JobController --- */
// REST Controller responsible for handling all incoming HTTP requests related to Jobs/Repairs.
// The Controller does NOT contain Business Logic
// Its responsibility is to:
// 1. Receive requests from the client (Front-End)
// 2. Forward the request data to the JobService (Service Layer)
// 3. Return the service result as an HTTP response
@RestController
@RequestMapping("/")
public class JobController {
    /// Attributes
    // The Service Layer that contains all business logic for job operations
    private final JobService jobService;

    /// Constructor
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    /// Methods

    // Get All Jobs - GET
    /** @return List of all Job records in the database **/
    @GetMapping("api/jobs")
    public List<Job> getJobs() {
        // Delegate the request to the Service Layer
        return jobService.getAllJobs();
    }

    // Create a New Job - POST
    // Handles POST requests to create a new Job Record
    // Expects a JSON body containing job data
    /** @param body a map containing all fields needed to create a Job **/
    /** @return ResponseEntity containing the created Job or an error message **/
    @PostMapping("/api/jobs/create")
    public ResponseEntity<Job> createJob(@RequestBody Map<String, Object> body) {
        // Forward creation logic to the Service Layer
        return jobService.createJob(body);
    }

    // Update a Job - UPDATE
    // Handles PUT requests to update an existing Job Record
    /** @param id the ID of the job to update **/
    /** @param job the update job object sent from the front-end **/
    /** @return ResponseEntity with the updated job or a not found error **/
    @PutMapping("api/jobs/{id}/update")
    public ResponseEntity<Job> updateJob(@PathVariable Integer id, @RequestBody Job job) {
        // Service layer handles validation and updating
        return jobService.updateJob(id, job);
    }

    // Update Job Description Only - UPDATE
    // Special endpoint that updates ONLY the job description field.
    // Useful for partial updates without modifying the entire object.
    /** @param id the ID of the job to update **/
    /** @param job contains only the new job_description field **/
    /** @return ResponseEntity with the updated job or not found **/
    @PutMapping("api/jobs/{id}/description")
    public ResponseEntity<Job> updateJobDesc(@PathVariable Integer id, @RequestBody Job job) {
        // Extract only the "description" and update it in the service layer
        return jobService.updateJobDescription(id, job.getJob_description());
    }

    // Add Products (Parts or Services) to a Job/Repair - POST
    // Adds products/services to a repair.
    // The request body contains a list of maps describing each item.
    /** @param dataList list of part/service assignment objects **/
    /** @return ResponseEntity with a success or error message **/
    @PostMapping("/api/repairs/addProduct")
    public ResponseEntity<String> addProductsToRepair(@RequestBody List<Map<String, Object>> dataList) {
        // Forward the request data to the Service Layer, which performs validation,
        // checks job existence, and handles linking parts/services to the repair.
        return jobService.addProductsToRepair(dataList);
    }

    // Remove Products (Parts or Services) from a Job/Repair - POST
    // Removes one or more parts/services items from a repair.
    // Marked as @Transactional to ensure consistency if multiple deletes occur.
    // Using POST as we are removing multiple items and not one predictable resource path.
    /** @param dataList list of part/service items to remove **/
    /** @return ResponseEntity with a status message **/
    @PostMapping("/api/repairs/removeProduct")
    @Transactional
    public ResponseEntity<String> removeProductFromRepair(@RequestBody List<Map<String, Object>> dataList) {
        // Delegate removal logic to the Service Layer, which ensures each provided item
        // is matched and removed safely with transactional context.
        return jobService.removeProductsFromRepair(dataList);
    }

    // Delete a Job - DELETE
    // Deletes a job from the database.
    // Marked as @Transactional to ensure consistency if multiple deletes occur.
    /** @param id ID of the job to delete **/
    /** @return HTTP 204 No Content if successful or 404 if not found **/
    @DeleteMapping("/api/jobs/{id}")
    @Transactional
    public ResponseEntity<Void> deleteJob(@PathVariable Integer id) {
        // Pass the delete request to the Service Layer, which validates the job ID
        // and performs the deletion. If the job does not exist, a 404 response is returned.
        return jobService.deleteJob(id);
    }
}
