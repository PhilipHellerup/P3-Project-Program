package mainProgram.UnitTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import mainProgram.repository.*;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import mainProgram.table.JobPart;
import mainProgram.table.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JobServiceTests {

    /* Unit tests JobService logic using mocked repositories to verify method behavior,
   interaction with dependencies, and correct handling of valid, empty, and missing data. */

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobPartRepository jobPartRepository;

    @InjectMocks
    private JobService jobService;

    private Job testJob;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testJob = new Job();
        testJob.setId(1);
        testJob.setTitle("Test Job");

        testProduct = new Product("P001", "Test Product", "1234567890123", "Electronics", 99.99);
    }

    @Test
    void testGetJobById_Success() {
        when(jobRepository.findById(1)).thenReturn(Optional.of(testJob));

        Job result = jobService.getJobById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Job", result.getTitle());
        verify(jobRepository, times(1)).findById(1);
    }

    @Test
    void testGetJobById_NotFound() {
        when(jobRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> jobService.getJobById(999));
    }

    @Test
    void testSearch_WithKeyword() {
        List<Job> expectedJobs = Arrays.asList(testJob);
        when(jobRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(expectedJobs);

        List<Job> result = jobService.search("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Job", result.get(0).getTitle());
        verify(jobRepository, times(1)).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    void testSearch_EmptyKeyword() {
        List<Job> result = jobService.search("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jobRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    @Test
    void testSearch_NullKeyword() {
        List<Job> result = jobService.search(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jobRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    @Test
    void testGetPartsForJob() {
        JobPart jobPart = new JobPart(testJob, testProduct, 5);
        List<JobPart> expectedParts = Arrays.asList(jobPart);

        when(jobPartRepository.findByJobId(1)).thenReturn(expectedParts);

        List<JobPart> result = jobService.getPartsForJob(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getQuantity());
        verify(jobPartRepository, times(1)).findByJobId(1);
    }
}
