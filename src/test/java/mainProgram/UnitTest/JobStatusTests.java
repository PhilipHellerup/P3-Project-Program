package mainProgram.UnitTest;

import mainProgram.table.JobStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobStatusTests {
    /* Ensures JobStatus entity properly stores and retrieves ID and name values,
   including handling of null names, in a standalone, non-database context. */

    @Test
    void testJobStatusCreation() {
        JobStatus status = new JobStatus();
        status.setId((short) 1);
        status.setName("Pending");

        assertEquals((short) 1, status.getId());
        assertEquals("Pending", status.getName());
    }

    @Test
    void testJobStatusSetters() {
        JobStatus status = new JobStatus();

        status.setId((short) 2);
        status.setName("In Progress");

        assertEquals((short) 2, status.getId());
        assertEquals("In Progress", status.getName());
    }

    @Test
    void testJobStatusWithNullName() {
        JobStatus status = new JobStatus();
        status.setId((short) 1);
        status.setName(null);

        assertEquals((short) 1, status.getId());
        assertNull(status.getName());
    }
}
