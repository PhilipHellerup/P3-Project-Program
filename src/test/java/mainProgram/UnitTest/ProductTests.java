package mainProgram.UnitTest;

import static org.junit.jupiter.api.Assertions.*;

import mainProgram.table.Product;
import org.junit.jupiter.api.Test;

class ProductTests {

    @Test
    /* Tests basic Product entity functionality including constructors, getters, and setters,
   ensuring correct field assignment and handling of null values without database interaction. */
    void testProductCreation() {
        Product product = new Product("P001", "Test Product", "1234567890123", "Electronics", 99.99);

        assertEquals("P001", product.getProductNumber());
        assertEquals("Test Product", product.getName());
        assertEquals("1234567890123", product.getEAN());
        assertEquals("Electronics", product.getType());
        assertEquals(99.99, product.getPrice());
    }

    @Test
    void testProductNoArgConstructor() {
        Product product = new Product();
        assertNotNull(product);
    }

    @Test
    void testProductSetters() {
        Product product = new Product();
        product.setProductNumber("P002");
        product.setName("Updated Product");
        product.setEAN("9876543210987");
        product.setType("Tools");
        product.setPrice(149.99);

        assertEquals("P002", product.getProductNumber());
        assertEquals("Updated Product", product.getName());
        assertEquals("9876543210987", product.getEAN());
        assertEquals("Tools", product.getType());
        assertEquals(149.99, product.getPrice());
    }

    @Test
    void testProductWithNullValues() {
        Product product = new Product(null, null, null, null, null);

        assertNull(product.getProductNumber());
        assertNull(product.getName());
        assertNull(product.getEAN());
        assertNull(product.getType());
        assertNull(product.getPrice());
    }
}
