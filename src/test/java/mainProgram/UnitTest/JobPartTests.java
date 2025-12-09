package mainProgram.UnitTest;

import mainProgram.table.Job;
import mainProgram.table.JobPart;
import mainProgram.table.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Verifies JobPart logic such as constructor initialization, quantity adjustments, and setter
correctness, focusing purely on in-memory object behavior without persistence. */
class JobPartTests {

    // Create a variable to hold a test job and a test product
    private Job testJob;
    private Product testProduct;

    // Before each test, create a new test job and a new test repair
    @BeforeEach
    void setUp() {
        testJob = new Job();
        testProduct = new Product("P001", "Test Product", "1234567890123", "Electronics", 99.99);
    }

    // Test that jobParts are created successfully using the constructor
    @Test
    void testJobPartCreation() {
        // Create a new instance of jobPart using the test job and test product
        JobPart jobPart = new JobPart(testJob, testProduct, 5);

        // Check that the new jobPart instance was created successfully
        assertEquals(testJob, jobPart.getJob());
        assertEquals(testProduct, jobPart.getProduct());
        assertEquals(5, jobPart.getQuantity());
    }

    // Test that the jobPart addQuantity() methods works
    @Test
    void testAddQuantity() {
        // Create a new instance of jobPart using the test job and test product
        JobPart jobPart = new JobPart(testJob, testProduct, 5);

        // Call the addQuantity() method inside jobPart to increase the quantity by 3
        jobPart.addQuantity(3);

        // Check that the quantity is set correctly to 8 (5 + 3)
        assertEquals(8, jobPart.getQuantity());
    }

    // Test that we can add negative quantity
    @Test
    void testAddNegativeQuantity() {
        // Create a new instance of jobPart using the test job and test product
        JobPart jobPart = new JobPart(testJob, testProduct, 10);

        // Call the addQuantity() method inside jobPart to decrease the quantity by 3
        jobPart.addQuantity(-3);

        // Check that the quantity is set correctly to 7 (10 + (-3))
        assertEquals(7, jobPart.getQuantity());
    }

    // Test the setters for jobPart
    @Test
    void testJobPartSetters() {
        // Create an empty instance of jobPart
        JobPart jobPart = new JobPart();

        // Use the setter methods to set attributes for the jobPart object
        jobPart.setJob(testJob);
        jobPart.setProduct(testProduct);
        jobPart.setQuantity(10);

        // Check that the attributes are set correctly using the jobPart getter methods
        assertEquals(testJob, jobPart.getJob());
        assertEquals(testProduct, jobPart.getProduct());
        assertEquals(10, jobPart.getQuantity());
    }

    // Test that the noArgConstructor works
    @Test
    void testJobPartNoArgConstructor() {
        // Create an new jobPart instance without any parameters
        JobPart jobPart = new JobPart();

        // Check that the object is created successfully
        assertNotNull(jobPart);
    }
}