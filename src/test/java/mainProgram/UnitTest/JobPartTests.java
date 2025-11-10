package mainProgram.UnitTest;

import mainProgram.table.Job;
import mainProgram.table.JobPart;
import mainProgram.table.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobPartTests {
    /* Verifies JobPart logic such as constructor initialization, quantity adjustments, and setter
   correctness, focusing purely on in-memory object behavior without persistence. */

    private Job testJob;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testJob = new Job();
        testProduct = new Product("P001", "Test Product", "1234567890123", "Electronics", 99.99);
    }

    @Test
    void testJobPartCreation() {
        JobPart jobPart = new JobPart(testJob, testProduct, 5);

        assertEquals(testJob, jobPart.getJob());
        assertEquals(testProduct, jobPart.getProduct());
        assertEquals(5, jobPart.getQuantity());
    }

    @Test
    void testAddQuantity() {
        JobPart jobPart = new JobPart(testJob, testProduct, 5);

        jobPart.addQuantity(3);

        assertEquals(8, jobPart.getQuantity());
    }

    @Test
    void testAddNegativeQuantity() {
        JobPart jobPart = new JobPart(testJob, testProduct, 10);

        jobPart.addQuantity(-3);

        assertEquals(7, jobPart.getQuantity());
    }

    @Test
    void testJobPartSetters() {
        JobPart jobPart = new JobPart();

        jobPart.setJob(testJob);
        jobPart.setProduct(testProduct);
        jobPart.setQuantity(10);

        assertEquals(testJob, jobPart.getJob());
        assertEquals(testProduct, jobPart.getProduct());
        assertEquals(10, jobPart.getQuantity());
    }

    @Test
    void testJobPartNoArgConstructor() {
        JobPart jobPart = new JobPart();
        assertNotNull(jobPart);
    }
}