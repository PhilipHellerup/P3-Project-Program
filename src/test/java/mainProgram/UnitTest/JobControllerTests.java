package mainProgram.UnitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import mainProgram.controller.JobController;
import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import mainProgram.repository.JobServiceRepository;
import mainProgram.repository.JobStatusRepository;
import mainProgram.repository.ServiceRepository;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import mainProgram.table.JobServices;
import mainProgram.table.JobStatus;
import mainProgram.table.Services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JobControllerTests {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobStatusRepository statusRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private JobServiceRepository jobServiceRepository;
    @Mock
    private JobPartRepository jobPartRepository;
    @Mock
    private JobService jobService;

    // We'll inject mocks into the controller (constructor injection).
    private JobController controller;

    @BeforeEach
    void setup() {
        controller = new JobController(jobRepository, statusRepository, jobService, serviceRepository, jobServiceRepository, jobPartRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /**
     * Happy path: valid JSON -> job saved, default duration applied (duration==0 -> 60).
     */
    @Test
    void createJob_happyPath_returnsSavedJobWithDefaultDuration() throws Exception {
        String json = """
            {
              "title": "Fix Screen",
              "date": "2025-11-26T10:00:00",
              "status": { "id": 1 },
              "duration": 0,
              "price_per_min": 1.0,
              "services": [ { "id": 2, "quantity": 1 } ]
            }
            """;

        // prepare mocks
        JobStatus status = new JobStatus();
        status.setId((short) 1);
        when(statusRepository.findById((short) 1)).thenReturn(Optional.of(status));

        // when saving, emulate DB-generated id
        when(jobRepository.save(any(Job.class))).thenAnswer(inv -> {
            Job j = inv.getArgument(0);
            j.setId(100); // emulate persisted id
            return j;
        });

        Services svc = mock(Services.class);
        when(serviceRepository.findById(2)).thenReturn(Optional.of(svc));
        when(jobServiceRepository.save(any(JobServices.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/jobs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("Fix Screen"))
                .andExpect(jsonPath("$.duration").value(60)); // default applied

        // verify interactions
        verify(statusRepository, times(1)).findById((short) 1);
        verify(jobRepository, times(1)).save(any(Job.class));
        verify(jobServiceRepository, times(1)).save(any(JobServices.class));
    }

    /**
     * Missing required field (title) -> controller returns 400 Bad Request (current controller behavior).
     */
    @Test
    void createJob_missingTitle_returnsBadRequest() throws Exception {
        String json = """
            {
              "date": "2025-11-26T10:00:00",
              "status": { "id": 1 },
              "duration": 30
            }
            """;

        mockMvc.perform(post("/api/jobs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    /**
     * Invalid status id (not present) -> current controller throws and results in 500 Internal Server Error.
     * This test documents current behavior. If you later change controller to return 4xx, update test.
     */
    @Test
    void createJob_invalidStatusId_returnsInternalServerError() throws Exception {
        String json = """
            {
              "title": "Fix Screen",
              "date": "2025-11-26T10:00:00",
              "status": { "id": 999 },
              "duration": 30
            }
            """;

        when(statusRepository.findById((short) 999)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/jobs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError());

        verify(statusRepository, times(1)).findById((short) 999);
        verify(jobRepository, never()).save(any(Job.class));
    }
}
