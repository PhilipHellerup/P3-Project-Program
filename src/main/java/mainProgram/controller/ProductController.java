package mainProgram.controller; // Project Organization

import java.util.Map;
/* --- Imports --- */
import mainProgram.repository.ProductRepository;
import mainProgram.table.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* --- PartController --- */
// REST controller for handling product-related operations.
// Provides endpoints for creating, deleting, and (optionally) viewing products in the database.
@RestController
@RequestMapping("/api/products") // Base path for all API routes in this controller
public class ProductController {

    // Attributes
    private final ProductRepository productRepository; // Injected repository used for database operations CRUD

    // Constructor for Dependency Injection
    // Spring automatically provides an instance of ProductRepository at runtime.
    /** @param productRepository the repository handling CRUD operations for Product entities. **/
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Methods
    /** @param product the product object to create **/
    /** @return ResponseEntity containing the created product if successful, or a bad request response **/
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Save the product to the database using the repository and its .save() method.
        Product savedProduct = productRepository.save(product);

        // Return the saved product as a JSON response with HTTP 200 OK status.
        return ResponseEntity.ok(savedProduct);
    }

    // Deletes a specific product from the database based on its ID.
    // Triggered when a DELETE request is sent to "/api/products/{id}"-
    // Example: request: DELETE /api/products/5 will delete the product with id "5".
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        // Check if a product with the given ID exists before attempting deletion
        if (productRepository.existsById(id)) {
            // If the product exists, delete it from the database
            productRepository.deleteById(id);

            // Return HTTP 204: No Content (indicating success, but no response body needed)
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            // Return HTTP 404: Not Found (if the product does not exist in the database)
            return ResponseEntity.notFound().build(); // 404 if not found
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProduct(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        // Find the product in the database by ID
        return productRepository
            .findById(id)
            .map((product) -> {
                // Iterate over each field in the updates map and apply the changes
                updates.forEach((field, value) -> {
                    switch (field) {
                        case "productNumber" -> product.setProductNumber((String) value);
                        case "name" -> product.setName((String) value);
                        case "EAN" -> product.setEAN((String) value);
                        case "type" -> product.setType((String) value);
                        case "price" -> {
                            // Handle price being either a number or string
                            if (value instanceof Number num) {
                                product.setPrice(num.doubleValue());
                            } else {
                                product.setPrice(Double.parseDouble(value.toString()));
                            }
                        }
                        default -> System.out.println("Unknown field: " + field);
                    }
                });

                // Save the updated product in the database
                Product saved = productRepository.save(product);

                // Return HTTP 200 OK because product updated successfully
                return ResponseEntity.ok(saved);
            })
            // Product with given ID does not exist
            .orElseGet(() -> {
                // Return HTTP 404: Not Found = Product Not Found
                return ResponseEntity.notFound().build();
            });
    }
}
