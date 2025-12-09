package mainProgram.UnitTest;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


/* Unit tests JobService logic using mocked repositories to verify method behavior,
interaction with dependencies, and correct handling of valid, empty, and missing data. */
@ExtendWith(MockitoExtension.class)
class JobServiceTests {

    // Mock the job repository
    @Mock
    private JobRepository jobRepository;

    // Mock the job-part repository
    @Mock
    private JobPartRepository jobPartRepository;


    // Create an instance of jobService and inject the mocked job repository and job-part repository into it
    @InjectMocks
    private JobService jobService;

    // Create variable to hold a test job and test product
    private Job testJob;
    private Product testProduct;

    // This is run before each test and creates a new job with an id and a tile. This also created a test product
    @BeforeEach
    void setUp() {
        testJob = new Job();
        testJob.setId(1);
        testJob.setTitle("Test Job");

        testProduct = new Product("P001", "Test Product", "1234567890123", "Electronics", 99.99);
    }

    // A test to make sure that a job can be found by id using the jobService function to get job by id
    @Test
    void testGetJobById_Success() {
        // When jobRepository gets a call to find by id(1), then it should return "testJob" instead of during any database fetching
        when(jobRepository.findById(1)).thenReturn(Optional.of(testJob));

        // Test jobService's getJobById() method
        Job result = jobService.getJobById(1);

        // Check that the job was found and that it was the correct id and title
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Job", result.getTitle());

        // varify that jobRepository only got one call to return the test job
        verify(jobRepository, times(1)).findById(1);
    }

    // Test that jobService throws and exception when a job is not found using its getJobById() method
    @Test
    void testGetJobById_NotFound() {
        // // When jobRepository gets a call to find by id(), then it should return an empty object instead of during any database fetching
        when(jobRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> jobService.getJobById(999));
    }

    // Test that the jobService search() methods can search for jub using a keyword.
    @Test
    void testSearch_WithKeyword() {
        // Define a list of expected jobs to be returned by the search. This list only contains the test job
        List<Job> expectedJobs = Arrays.asList(testJob);

        // When the job repository gets a call the search for jobs with the keyword "Test" it should return the expected results,
        // which is a list containing only the test job
        when(jobRepository.searchJobs("Test")).thenReturn(expectedJobs);

        // Call the jobService.search() method with keyword "Test"
        List<Job> result = jobService.search("Test");

        // Check that the test job was found using the keyword "Test" in the search function
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Job", result.get(0).getTitle());

        // Ensure that the job repository was only called one time to do a search
        verify(jobRepository, times(1)).searchJobs("Test");
    }

    // Test search with an empty keyword. The job repository should never be called, because searching for an empty string should return an empty list "List.of()"
    @Test
    void testSearch_EmptyKeyword() {
        // Call the jobService search() method with an empty keyword
        List<Job> result = jobService.search("");

        // Check that to results where found
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Check that job repository was never called
        verify(jobRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    // Test search with a null keyword. The job repository should never be called, because searching for an empty should return an empty list "List.of()"
    @Test
    void testSearch_NullKeyword() {
        // Call the jobService search() method with a null keyword
        List<Job> result = jobService.search(null);

        // Check that to results where found
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Check that job repository was never called
        verify(jobRepository, never()).findByTitleContainingIgnoreCase(anyString());
    }

    // Test the method to return all parts associated with a job using the jobService getPartsForJob() method
    @Test
    void testGetPartsForJob() {
        // Create a new instance of jobPart with the test job and test product and a quantity of 5
        JobPart jobPart = new JobPart(testJob, testProduct, 5);

        // Define the list of expected parts to find for the test job. This should be the list only containing the above defined jobPart
        List<JobPart> expectedParts = Arrays.asList(jobPart);

        // When the jobPart repository is called to find parts by job id (1), it should return the expected List defined above
        when(jobPartRepository.findByJobId(1)).thenReturn(expectedParts);

        // Call the getPartsForJob() method with id 1
        List<JobPart> result = jobService.getPartsForJob(1);

        // Check that the testProduct was found to be associated with the test job and check that the id and quantities match
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getQuantity());

        // Check tha the jobPart repository only was called once during the test
        verify(jobPartRepository, times(1)).findByJobId(1);
    }
}