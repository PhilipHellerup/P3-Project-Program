package mainProgram.UnitTest;

import mainProgram.table.Job;
import mainProgram.table.JobStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/* Validates Job entity field assignment and retrieval, checks null defaults and
ensures getters/setters operate correctly without relying on Spring or database layers. */
class JobTests {

    @Test
    // test the setters and getters of the job class
    void testJobSettersAndGetters() {
        // Creating a new job
        Job job = new Job();
        JobStatus status = new JobStatus();
        status.setId((short) 1);
        status.setName("Pending");

        // Setting the dage
        LocalDateTime now = LocalDateTime.now();

        // Using setters to set the attributes of the new job object
        job.setTitle("Repair Phone");
        job.setCustomer_name("John Doe");
        job.setCustomer_phone("+1234567890");
        job.setJob_description("Screen replacement");
        job.setWork_time_minutes(120);
        job.setPrice_per_minute(1.5);
        job.setDuration(180);
        job.setDate(now);
        job.setStatus(status);

        // Asserts that all attributes where set correctly using the class's getter methods
        assertEquals("Repair Phone", job.getTitle());
        assertEquals("John Doe", job.getCustomer_name());
        assertEquals("+1234567890", job.getCustomer_phone());
        assertEquals("Screen replacement", job.getJob_description());
        assertEquals(120, job.getWork_time_minutes());
        assertEquals(1.5, job.getPrice_per_minute());
        assertEquals(180, job.getDuration());
        assertEquals(now, job.getDate());
        assertEquals(status, job.getStatus());
    }

    @Test
    // Test job creating using the no arg constructor
    void testJobCreation() {
        Job job = new Job();
        assertNotNull(job);
    }

    @Test
    // Test job creating with all null values
    void testJobWithNullValues() {
        // New job is created without specifying any values
        Job job = new Job();

        // Assert that the job was successfully created and that all values assert to null
        assertNull(job.getTitle());
        assertNull(job.getCustomer_name());
        assertNull(job.getCustomer_phone());
        assertNull(job.getJob_description());
        assertNull(job.getWork_time_minutes());
        assertNull(job.getPrice_per_minute());
        assertNull(job.getDuration());
        assertNull(job.getDate());
        assertNull(job.getStatus());
    }
}
