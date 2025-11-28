package mainProgram.controller; // Project Organization

/* --- Imports --- */
import mainProgram.repository.ProductRepository;
import mainProgram.services.ProductService;
import mainProgram.table.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/* --- PartController --- */
// REST controller for handling product-related operations.
// Provides endpoints for creating, deleting, and (optionally) viewing products in the database.
@RestController
@RequestMapping("/api/products") // Base path for all API routes in this controller
public class ProductController {
    /// Attributes
    private final ProductRepository productRepository; // Injected repository used for database operations CRUD
    private final ProductService productService;       // Product Services (Service Layer)

    /// Constructor for Dependency Injection
    // Spring automatically provides an instance of ProductRepository at runtime.
    /** @param productRepository the repository handling CRUD operations for Product entities. **/
    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    /// Methods

    // Create Product - CREATE
    /** @param product the product object to create **/
    /** @return ResponseEntity containing the created product if successful, or a bad request response **/
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Calling the createProduct Service Method
        Product savedProduct = productService.createProduct(product);

        // Return the saved product as a JSON response with HTTP 200 OK status.
        return ResponseEntity.ok(savedProduct);
    }

    // Get Product by ID - READ
    /** @param id the unique id of the product to read **/
    /** @return ResponseEntity with HTTP 200 OK if Product was Found or 404 Not Found if the service does not exist **/
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        // Calling the getProduct Service Method
        Product product = productService.getProduct(id);

        // Return the product as a JSON response with HTTP 200 OK status if Product is found else 404 Not Found
        return (product != null) ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Delete Product by ID - DELETE
    /** @param id the unique id of the product to delete **/
    /** @return ResponseEntity with HTTP 204 No Content if deletion is successful,
     **         or HTTP 404 Not Found if the service does not exist **/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        // Calling the deleteProduct Service Method
        boolean deleted = productService.deleteProduct(id);

        // Return HTTP 204 No Content if Product is deleted else 404 Not Found
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Update Product by ID - UPDATE
    // The frontend sends a JSON body containing the fields to update (name, price, etc.)
    /** @param id the unique id of the product to update **/
    /** @param updates a Map containing field names as keys and new values as values **/
    /** @return ResponseEntity containing the updated service (HTTP 200 OK) if found and updated,
     **         or HTTP 404 Not Found if the product with the given ID does not exist **/
    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        // Calling the updateProduct Service Method
        Product updatedProduct = productService.updateProduct(id, updates);

        // Return HTTP 200 OK status if Product is updated else 404 Not Found
        return (updatedProduct != null) ? ResponseEntity.ok(updatedProduct) : ResponseEntity.notFound().build();
    }
}

