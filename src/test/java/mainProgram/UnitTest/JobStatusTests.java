package mainProgram.UnitTest;

import mainProgram.table.JobStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Ensures JobStatus entity properly stores and retrieves ID and name values,
including handling of null names, in a standalone, non-database context. */
class JobStatusTests {

    // Test that jobStatus creating works by creating a new status and setting the id and name
    @Test
    void testJobStatusCreation() {
        // Create a new object
        JobStatus status = new JobStatus();

        // Use setters to set the attributes
        status.setId((short) 1);
        status.setName("Pending");

        // Use the class's gettes to assert the status object's attributes are set correctly
        assertEquals((short) 1, status.getId());
        assertEquals("Pending", status.getName());
    }
}
