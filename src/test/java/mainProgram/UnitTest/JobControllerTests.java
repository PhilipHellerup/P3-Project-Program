package mainProgram.UnitTest;

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

@DisplayName("JobController Tests")
class JobControllerTests {

    /*
     * We test ONLY the controller layer here.
     * JobService is mocked to ensure we verify controller behaviour in isolation,
     * without testing underlying business logic or repository interactions.
     */

    private MockMvc mockMvc;
    private JobService jobService;
    private JobController jobController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        jobService = mock(JobService.class);
        jobController = new JobController(jobService);
        mockMvc = MockMvcBuilders.standaloneSetup(jobController)
                .build();
    }

    // Creates a Job object reused in tests
    private Job sampleJob() {
        Job job = new Job();
        job.setId(1);
        job.setTitle("Screen Replacement");
        job.setJob_description("Cracked iPhone screen");
        return job;
    }

    // ----------------------------------TESTS----------------------------------------- //
    @Nested
    @DisplayName("GET /api/jobs - Get All Jobs")
    class GetAllJobs {

        @Test
        void shouldReturnListOfJobs() throws Exception {
            /*
             * Verifies controller returns the service output as JSON.
             * We expect HTTP 200 and list size == 2.
             */
            List<Job> jobs = List.of(sampleJob(), sampleJob());
            when(jobService.getAllJobs()).thenReturn(jobs);

            mockMvc.perform(get("/api/jobs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].title").value("Screen Replacement"));

            verify(jobService).getAllJobs();
        }
    }
    // --------------------------------------------------------------------------- //
    @Nested
    @DisplayName("POST /api/jobs/create - Create Job")
    class CreateJob {

        @Test
        void shouldCreateJobAndReturn201WithBody() throws Exception {
            /*
             * Verifies that controller forwards parsed JSON to the service,
             * receives the created Job, and returns it as HTTP 200 JSON.
             * Uses ArgumentCaptor to assert correct body data mapping.
             */
            String requestJson = """
                    {
                        "title": "Battery Replacement",
                        "date": "2025-12-01T14:30:00",
                        "status": { "id": 2 },
                        "duration": 45,
                        "price_per_min": 1.5
                    }
                    """;

            Job created = sampleJob();
            created.setId(99);
            created.setTitle("Battery Replacement");

            when(jobService.createJob(anyMap()))
                    .thenReturn(ResponseEntity.ok(created));

            mockMvc.perform(post("/api/jobs/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(99))
                    .andExpect(jsonPath("$.title").value("Battery Replacement"));

            ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
            verify(jobService).createJob(captor.capture());
            assert captor.getValue().get("title").equals("Battery Replacement");
        }

        @Test
        void shouldReturn400WhenServiceReturnsBadRequest() throws Exception {
            /*
             * If service rejects creation and returns a Bad Request ResponseEntity,
             * controller should relay the exact same status.
             */
            when(jobService.createJob(anyMap()))
                    .thenReturn(ResponseEntity.badRequest().body(null));

            mockMvc.perform(post("/api/jobs/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());

            verify(jobService).createJob(anyMap());
        }
    }
    // --------------------------------------------------------------------------- //
    @Nested
    @DisplayName("PUT /api/jobs/{id}/update - Full Update")
    class UpdateJob {

        @Test
        void shouldUpdateAndReturnUpdatedJob() throws Exception {
            /*
             * Verifies a successful update returns HTTP 200 + updated JSON body.
             */
            Job updated = sampleJob();
            updated.setTitle("Updated Title");

            when(jobService.updateJob(eq(5), any()))
                    .thenReturn(ResponseEntity.ok(updated));

            mockMvc.perform(put("/api/jobs/{id}/update", 5)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updated)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Updated Title"));

            verify(jobService).updateJob(eq(5), any());
        }

        @Test
        void shouldReturn404WhenJobNotFound() throws Exception {
            /*
             * If service returns ResponseEntity.notFound(),
             * controller must also return 404.
             */
            when(jobService.updateJob(eq(999), any()))
                    .thenReturn(ResponseEntity.notFound().build());

            mockMvc.perform(put("/api/jobs/{id}/update", 999)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isNotFound());

            verify(jobService).updateJob(eq(999), any());
        }
        // --------------------------------------------------------------- //
        @Nested
        @DisplayName("PUT /api/jobs/{id}/description - Partial Description Update")
        class UpdateJobDescription {

            @Test
            void shouldUpdateOnlyDescription() throws Exception {
                /*
                 * Controller extracts only description from payload and calls jobService.updateJobDescription().
                 * If successful -> 200 OK + updated JSON is returned.
                 */
                Job jobWithNewDesc = new Job();
                jobWithNewDesc.setJob_description("New detailed description here");

                Job returnedJob = sampleJob();
                returnedJob.setJob_description("New detailed description here");

                when(jobService.updateJobDescription(eq(3), eq("New detailed description here")))
                        .thenReturn(ResponseEntity.ok(returnedJob));

                mockMvc.perform(put("/api/jobs/{id}/description", 3)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"job_description\": \"New detailed description here\"}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.job_description").value("New detailed description here"));

                verify(jobService).updateJobDescription(eq(3), eq("New detailed description here"));
            }
        }
        // --------------------------------------------------------------- //
        @Nested
        @DisplayName("POST /api/repairs/addProduct - Add Products to Repair")
        class AddProductsToRepair {

            private final String validAddPayload = """
                    [
                        { "repairId": 7, "productId": 12, "quantity": 1, "type": "part" },
                        { "repairId": 7, "productId": 8,  "quantity": 2, "type": "service" }
                    ]
                    """;

            @Test
            void shouldAddProductsAndReturnSuccessMessage() throws Exception {
                /*
                 * Successful add request -> expect HTTP 200 + confirmation message.
                 */
                when(jobService.addProductsToRepair(anyList()))
                        .thenReturn(ResponseEntity.ok("Products added successfully"));

                mockMvc.perform(post("/api/repairs/addProduct")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validAddPayload))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Products added successfully"));

                verify(jobService).addProductsToRepair(anyList());
            }

            @Test
            void shouldReturn400OnInvalidPayload() throws Exception {
                /*
                 * If service flags payload invalid -> controller must return 400.
                 */
                when(jobService.addProductsToRepair(anyList()))
                        .thenReturn(ResponseEntity.badRequest().body("Invalid data"));

                mockMvc.perform(post("/api/repairs/addProduct")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[]"))
                        .andExpect(status().isBadRequest());

                verify(jobService).addProductsToRepair(anyList());
            }
        }
        // --------------------------------------------------------------- //
        @Nested
        @DisplayName("POST /api/repairs/removeProduct - Remove Products")
        class RemoveProductsFromRepair {

            private final String removePayload = """
                    [
                        { "repairId": 7, "productId": 12, "type": "part" },
                        { "repairId": 7, "productId": 8,  "type": "service" }
                    ]
                    """;

            @Test
            void shouldRemoveProductsAndReturnSuccess() throws Exception {
                /*
                 * Successful removal -> expect HTTP 200.
                 */
                when(jobService.removeProductsFromRepair(anyList()))
                        .thenReturn(ResponseEntity.ok("Products removed"));

                mockMvc.perform(post("/api/repairs/removeProduct")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(removePayload))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Products removed"));

                verify(jobService).removeProductsFromRepair(anyList());
            }
        }
        // --------------------------------------------------------------- //
        @Nested
        @DisplayName("DELETE /api/jobs/{id} - Delete Job")
        class DeleteJob {

            @Test
            void shouldReturn204WhenJobDeleted() throws Exception {
                /*
                 * Expected behaviour:
                 * service returns noContent() -> controller returns HTTP 204 No Content.
                 */
                when(jobService.deleteJob(42))
                        .thenReturn(ResponseEntity.noContent().build());

                mockMvc.perform(delete("/api/jobs/{id}", 42))
                        .andExpect(status().isNoContent());

                verify(jobService).deleteJob(42);
            }

            @Test
            void shouldReturn404WhenJobNotFound() throws Exception {
                /*
                 * If service reports job missing -> controller returns HTTP 404 Not Found.
                 */
                when(jobService.deleteJob(999))
                        .thenReturn(ResponseEntity.notFound().build());

                mockMvc.perform(delete("/api/jobs/{id}", 999))
                        .andExpect(status().isNotFound());

                verify(jobService).deleteJob(999);
            }
        }
    }
}