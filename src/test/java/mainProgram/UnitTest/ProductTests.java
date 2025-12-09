package mainProgram.UnitTest;

import mainProgram.table.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/* Tests basic Product entity functionality including constructors, getters, and setters,
ensuring correct field assignment and handling of null values without database interaction. */
class ProductTests {

    @Test
    // Test the creating of a product using the constructor
    void testProductCreation() {
        // Create a nwe product with set attributes
        Product product = new Product("P001", "Test Product", "1234567890123", "Electronics", 99.99);

        // Assert that all attributes are set correctly using the class's getter methods
        assertEquals("P001", product.getProductNumber());
        assertEquals("Test Product", product.getName());
        assertEquals("1234567890123", product.getEAN());
        assertEquals("Electronics", product.getType());
        assertEquals(99.99, product.getPrice());
    }

    @Test
    // Tests that the noArgConstructor in the product class works, but calling it to create a new product and then asserting if the product is not null
    void testProductNoArgConstructor() {
        Product product = new Product();
        assertNotNull(product);
    }

    @Test
    // Tests that the setters and getters in the product class works
    void testProductSetters() {
        // Create a new "empty" product and use the setters to assign values to the attributes
        Product product = new Product();
        product.setProductNumber("P002");
        product.setName("Updated Product");
        product.setEAN("9876543210987");
        product.setType("Tools");
        product.setPrice(149.99);

        // Assert that all attributes are set correctly using the class's getter methods
        assertEquals("P002", product.getProductNumber());
        assertEquals("Updated Product", product.getName());
        assertEquals("9876543210987", product.getEAN());
        assertEquals("Tools", product.getType());
        assertEquals(149.99, product.getPrice());
    }

    @Test
    // Test that a product can be created with no values and make sure that they are still asserted to null
    void testProductWithNullValues() {
        // New job is created without specifying any values
        Product product = new Product();

        // Assert that the job was successfully created and that all values assert to null
        assertNull(product.getProductNumber());
        assertNull(product.getName());
        assertNull(product.getEAN());
        assertNull(product.getType());
        assertNull(product.getPrice());
    }
}
