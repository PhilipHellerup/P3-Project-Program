package mainProgram.UnitTest; // Project Organization

/* --- Imports --- */
import com.fasterxml.jackson.databind.ObjectMapper;
import mainProgram.controller.JobController;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* --- JobControllerTests Class --- */
// Unit test suite that verifies JobController behaviour in complete isolation.
// The JobService is fully mocked so these tests ensure:
//   - Controller receives & parses incoming HTTP JSON correctly
//   - Controller forwards arguments to JobService with correct structure
//   - Controller returns exactly what the JobService provides
//   - Controller produces correct HTTP status codes and JSON responses
@DisplayName("JobController Tests") // Name of test
class JobControllerTests {
    // INTRO:
    // We test ONLY the controller layer here.
    // JobService is mocked to ensure we verify controller behaviour in isolation,
    // without testing underlying business logic or repository interactions.

    /* --- Test Infrastructure --- */
    // MockMvc simulates HTTP requests directly against the controller layer.
    private MockMvc mockMvc;

    // Mocked JobService, no real business logic or DB calls are executed.
    private JobService jobService;

    // The controller under test, created manually and injected with the mock service.
    private JobController jobController;

    // Utility mapper -> Converts Java objects to JSON strings
    private final ObjectMapper objectMapper = new ObjectMapper();

    /* --- Test Setup --- */
    // Initializes a clean test environment before each test.
    // - Creates a new mocked JobService
    // - Creates a JobController with that mock
    // - Configures MockMvc to test the controller standalone
    @BeforeEach // Before each test (@test) run this in this class
    void setUp() {
        // Create a mock JobService
        jobService = mock(JobService.class);

        // Inject mock service into controller
        jobController = new JobController(jobService);

        // Setup MockMvc in standalone mode -> No full Spring context required
        mockMvc = MockMvcBuilders.standaloneSetup(jobController)
                .build();
    }

    /* --- Helper Method --- */
    // Creates a simple Job instance with preset values for reuse across tests.
    private Job sampleJob() {
        Job job = new Job();
        job.setId(1);
        job.setTitle("Screen Replacement");
        job.setJob_description("Cracked iPhone screen");
        return job;
    }

    // ========================================================================== //
    // --- GET /api/jobs - Get All Jobs ---------------------------------------- //
    // ========================================================================== //
    @Nested // Groups related test inside this class. Allows for this test class to contain tests specifically for this endpoint.
    @DisplayName("GET /api/jobs - Get All Jobs") // Name of test
    class GetAllJobs {
        // Verifies that the GET /api/jobs endpoint:
        // - Returns HTTP 200 OK
        // - Returns a JSON array with two Job objects
        // - Calls jobService.getAllJobs() exactly once
        @Test
        void shouldReturnListOfJobs() throws Exception {
            // Prepare mocked service return value:
            // Here we simulate what the JobService "would" return if the database contained two jobs.
            // The controller should return these as JSON.
            List<Job> jobs = List.of(sampleJob(), sampleJob());

            // This tells Mockito:
            // "When jobService.getAllJobs() is called during the test,
            // return the `jobs` list above instead of doing real logic."
            when(jobService.getAllJobs()).thenReturn(jobs);

            // Perform GET /api/jobs and validate the response
            mockMvc.perform(get("/api/jobs"))
                    .andExpect(status().isOk()) // HTTP 200 means the request succeeded
                    .andExpect(jsonPath("$", hasSize(2))) // The root JSON array should contain exactly 2 items
                    .andExpect(jsonPath("$[0].id").value(1)) // First job in the array should have id = 1
                    .andExpect(jsonPath("$[0].title").value("Screen Replacement")); // First job title should be "Screen Replacement"

            // Verify that the controller actually called the mocked service method.
            // Ensures that controller → service communication is correct.
            verify(jobService).getAllJobs();
        }
    }

    // ========================================================================== //
    // --- POST /api/jobs/create - Create Job ----------------------------------- //
    // ========================================================================== //
    @Nested // Groups all tests for the "POST /api/jobs/create" endpoint into this class.
    @DisplayName("POST /api/jobs/create - Create Job") // Name of this test section
    class CreateJob {
        // Tests that a valid job creation request:
        // - Sends JSON to the controller
        // - Controller parses JSON into a Map<String, Object>
        // - Controller calls jobService.createJob() with that Map
        // - Controller returns the created Job as JSON with HTTP 200 OK
        @Test
        void shouldCreateJobAndReturn201WithBody() throws Exception {
            // JSON payload that simulates what a real client would send during job creation.
            String requestJson = """
                    {
                        "title": "Battery Replacement",
                        "date": "2025-12-01T14:30:00",
                        "status": { "id": 2 },
                        "duration": 45,
                        "price_per_min": 1.5
                    }
                    """;

            // Create a Job object representing the "created" result returned by our mock service.
            Job created = sampleJob();
            created.setId(99); // Simulated database-generated ID
            created.setTitle("Battery Replacement"); // Set a new title

            // Mock service behaviour:
            // When jobService.createJob() is called with any Map input,
            // return a ResponseEntity containing the created Job.
            when(jobService.createJob(anyMap()))
                    .thenReturn(ResponseEntity.ok(created));

            // Perform POST request with JSON and validate the response content.
            mockMvc.perform(post("/api/jobs/create")
                            .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                            .content(requestJson))                   // Attach our request JSON
                    .andExpect(status().isOk()) // Expect HTTP 200 OK
                    .andExpect(jsonPath("$.id").value(99)) // Returned job should match mock
                    .andExpect(jsonPath("$.title").value("Battery Replacement")); // Returned title should match mock

            // Capture the Map<String, Object> that the controller forwarded to the service.
            // Background:
            //   - The controller method receives JSON (@RequestBody) from the HTTP request.
            //   - Spring (via Jackson) automatically deserializes that JSON into a Map<String, Object>.
            //   - The controller passes that Map directly to jobService.createJob(...).
            //
            // ArgumentCaptor:
            //   - Allows us to "capture" the Map argument passed into the service method.
            //   - This lets the test verify that the controller parsed the JSON correctly
            //     and forwarded the expected fields without modification.
            ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);

            // Capture the actual Map that was passed to jobService.createJob(...)
            verify(jobService).createJob(captor.capture());

            // Validate that the "title" field from the JSON request
            // was correctly deserialized and included in the Map.
            assert captor.getValue().get("title").equals("Battery Replacement");
        }

        // Ensures that if the JobService refuses to create the job (for example, due to validation errors),
        // the controller simply forwards the HTTP 400 Bad Request status without modifying it
        @Test
        void shouldReturn400WhenServiceReturnsBadRequest() throws Exception {
            // Mock service behaviour:
            // Simulate failure by returning ResponseEntity.badRequest()
            when(jobService.createJob(anyMap()))
                    .thenReturn(ResponseEntity.badRequest().body(null));

            // Perform POST with an empty JSON object (invalid input)
            mockMvc.perform(post("/api/jobs/create")
                            .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                            .content("{}"))                          // Attach empty request JSON
                    .andExpect(status().isBadRequest());  // Expect 400 Bad Request

            // Ensure the controller did call the service
            verify(jobService).createJob(anyMap());
        }
    }

    // ========================================================================== //
    // --- PUT /api/jobs/{id}/update - Full Update ------------------------------ //
    // ========================================================================== //
    @Nested // Groups all tests for the "PUT /api/jobs/{id}/update" endpoint
    @DisplayName("PUT /api/jobs/{id}/update - Full Update") // Name of this test section
    class UpdateJob {
        // Tests that a valid full update request:
        // - Sends JSON to the controller
        // - Controller calls jobService.updateJob(id, payload)
        // - Returns the updated Job as JSON with HTTP 200 OK
        @Test
        void shouldUpdateAndReturnUpdatedJob() throws Exception {
            // Mock updated job returned by the service
            Job updated = sampleJob();
            updated.setTitle("Updated Title"); // Update title

            // When the service is called with id = 5, return HTTP 200 + updated job
            when(jobService.updateJob(eq(5), any()))
                    .thenReturn(ResponseEntity.ok(updated));

            // Perform PUT /api/jobs/5/update and validate response
            mockMvc.perform(put("/api/jobs/{id}/update", 5)
                            .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                            .content(objectMapper.writeValueAsString(updated))) // Attach our request JSON
                    .andExpect(status().isOk()) // Expect HTTP 200 OK
                    .andExpect(jsonPath("$.title").value("Updated Title")); // JSON contains updated title

            // Ensure controller forwarded request to service with the correct ID
            verify(jobService).updateJob(eq(5), any());
        }

        // Ensures that if the JobService reports missing job (not found),
        // the controller forwards HTTP 404 Not Found without modification
        @Test
        void shouldReturn404WhenJobNotFound() throws Exception {
            // When service receives id = 999, return 404
            when(jobService.updateJob(eq(999), any()))
                    .thenReturn(ResponseEntity.notFound().build());

            // When service receives id = 999, return 404
            mockMvc.perform(put("/api/jobs/{id}/update", 999)
                            .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                            .content("{}")) // Empty body
                    .andExpect(status().isNotFound()); // Expect that the status is not found

            // Ensure that the controller forwarded the request to the service
            verify(jobService).updateJob(eq(999), any());
        }

        // ========================================================================== //
        // --- PUT /api/jobs/{id}/description - Partial Description Update ---------- //
        // ========================================================================== //
        @Nested // Groups all tests for the description update endpoint
        @DisplayName("PUT /api/jobs/{id}/description - Partial Description Update") // Name of this test section
        class UpdateJobDescription {
            // Tests that a partial update request:
            // - Sends only the description field
            // - Controller extracts description from JSON
            // - Calls jobService.updateJobDescription(id, newDescription)
            // - Returns updated Job JSON with HTTP 200 OK
            @Test
            void shouldUpdateOnlyDescription() throws Exception {
                // Prepare updated job returned by mock service
                Job returnedJob = sampleJob();
                returnedJob.setJob_description("New detailed description here"); // Set new job description

                // Service returns HTTP 200 + updated job when called with id = 3
                when(jobService.updateJobDescription(eq(3), eq("New detailed description here")))
                        .thenReturn(ResponseEntity.ok(returnedJob));

                // Perform PUT /api/jobs/3/description with new description payload
                mockMvc.perform(put("/api/jobs/{id}/description", 3)
                                .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                                .content("{\"job_description\": \"New detailed description here\"}")) // Job Description
                        .andExpect(status().isOk()) // Expect HTTP 200 OK
                        .andExpect(jsonPath("$.job_description").value("New detailed description here")); // JSON contains updated description

                // Ensure the controller forwarded correct arguments to the service
                verify(jobService).updateJobDescription(eq(3), eq("New detailed description here"));
            }
        }

        // ========================================================================== //
        // --- POST /api/repairs/addProduct - Add Products to Repair ---------------- //
        // ========================================================================== //
        @Nested  // Groups all tests for the add-product endpoint
        @DisplayName("POST /api/repairs/addProduct - Add Products to Repair") // Name of this test section
        class AddProductsToRepair {
            // Simulated user payload representing new repair items to add
            private final String validAddPayload = """
                    [
                        { "repairId": 7, "productId": 12, "quantity": 1, "type": "part" },
                        { "repairId": 7, "productId": 8,  "quantity": 2, "type": "service" }
                    ]
                    """;

            // Tests that a valid add request:
            // - Sends an array of items
            // - Controller forwards parsed list to jobService.addProductsToRepair()
            // - Returns HTTP 200 OK + success message
            @Test
            void shouldAddProductsAndReturnSuccessMessage() throws Exception {
                // Service returns HTTP 200 + confirmation text
                when(jobService.addProductsToRepair(anyList()))
                        .thenReturn(ResponseEntity.ok("Products added successfully"));

                // Perform POST and validate success message
                mockMvc.perform(post("/api/repairs/addProduct")
                                .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                                .content(validAddPayload)) // Attach Payload
                        .andExpect(status().isOk()) // Expect HTTP 200 OK
                        .andExpect(content().string("Products added successfully")); // Expect products added successfully

                // Ensure controller called service with a parsed list
                verify(jobService).addProductsToRepair(anyList());
            }

            // Tests that an invalid payload:
            // - Causes the service to return 400 Bad Request
            // - Controller forwards HTTP 400 without modification
            @Test
            void shouldReturn400OnInvalidPayload() throws Exception {
                // Service rejects payload
                when(jobService.addProductsToRepair(anyList()))
                        .thenReturn(ResponseEntity.badRequest().body("Invalid data"));

                // Perform POST with empty array and expect 400
                mockMvc.perform(post("/api/repairs/addProduct")
                                .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                                .content("[]")) // Empty array
                        .andExpect(status().isBadRequest()); // Expect 400 Bad Request

                // Ensure controller still forwarded request to service
                verify(jobService).addProductsToRepair(anyList());
            }
        }

        // ========================================================================== //
        // --- POST /api/repairs/removeProduct - Remove Products -------------------- //
        // ========================================================================== //
        @Nested // Groups all tests for the remove-product endpoint
        @DisplayName("POST /api/repairs/removeProduct - Remove Products") // Name of this test section
        class RemoveProductsFromRepair {
            // Simulated payload containing products/services to remove from a repair
            private final String removePayload = """
                    [
                        { "repairId": 7, "productId": 12, "type": "part" },
                        { "repairId": 7, "productId": 8,  "type": "service" }
                    ]
                    """;

            // Tests that a valid remove request:
            // - Sends an item list to the controller
            // - Controller forwards parsed list to jobService.removeProductsFromRepair()
            // - Returns HTTP 200 OK + confirmation message
            @Test
            void shouldRemoveProductsAndReturnSuccess() throws Exception {
                // Service returns HTTP 200 + success text
                when(jobService.removeProductsFromRepair(anyList()))
                        .thenReturn(ResponseEntity.ok("Products removed"));

                // Perform POST with the removal payload and expect success response
                mockMvc.perform(post("/api/repairs/removeProduct")
                                .contentType(MediaType.APPLICATION_JSON) // Controller expects JSON
                                .content(removePayload)) // Attach remove Payload
                        .andExpect(status().isOk()) // Expect HTTP 200 OK
                        .andExpect(content().string("Products removed")); // Expect Product Removed

                // Ensure controller forwarded request to service
                verify(jobService).removeProductsFromRepair(anyList());
            }
        }

        // ========================================================================== //
        // --- DELETE /api/jobs/{id} - Delete Job ---------------------------------- //
        // ========================================================================== //
        @Nested // Groups all tests for the DELETE /api/jobs/{id} endpoint
        @DisplayName("DELETE /api/jobs/{id} - Delete Job")  // Name of this test section
        class DeleteJob {
            // Tests that a successful delete:
            // - Calls jobService.deleteJob(id)
            // - Returns HTTP 204 No Content with no body
            @Test
            void shouldReturn204WhenJobDeleted() throws Exception {
                // Service reports successful deletion
                when(jobService.deleteJob(42))
                        .thenReturn(ResponseEntity.noContent().build());

                // Perform DELETE /api/jobs/42 and expect HTTP 204
                mockMvc.perform(delete("/api/jobs/{id}", 42))
                        .andExpect(status().isNoContent()); // Expect No Content as it got deleted

                // Ensure controller forwarded id=42 to service
                verify(jobService).deleteJob(42);
            }

            // Tests that deleting a missing job:
            // - Causes service to return 404
            // - Controller forwards HTTP 404 Not Found
            @Test
            void shouldReturn404WhenJobNotFound() throws Exception {
                // Service reports job not found
                when(jobService.deleteJob(999))
                        .thenReturn(ResponseEntity.notFound().build());

                // Perform DELETE /api/jobs/999 and expect 404
                mockMvc.perform(delete("/api/jobs/{id}", 999))
                        .andExpect(status().isNotFound());

                // Ensure controller forwarded id=999 to service
                verify(jobService).deleteJob(999);
            }
        }
    }
}