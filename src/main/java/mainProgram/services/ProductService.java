package mainProgram.services; // Project Organization

/* --- Imports --- */
import mainProgram.repository.ProductRepository;
import mainProgram.table.Product;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

/* --- ProductService Class --- */
@Service
public class ProductService implements BaseSearchService<Product> {
    /// Attributes
    private final ProductRepository productRepository;

    /// Constructor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /// Methods: CRUD Operations + Search

    // Create Product - CREATE
    /** @param product the product object to create **/
    /** @return ResponseEntity containing the created product if successful, or a bad request response **/
    public Product createProduct(Product product) {
        // Save the product to the database using the repository and its .save() method
        return productRepository.save(product);
    }

    // Read Product - READ
    /** @param id the unique id of the product to read **/
    /** @return ResponseEntity with HTTP 200 OK if Product was Found or 404 Not Found if the service does not exist **/
    public Product getProduct(int id) {
        // Get the product from the database via its ID
        return productRepository.findById(id).orElse(null);
    }

    // Delete Product - DELETE
    /** @param id the unique id of the product to delete **/
    /** @return ResponseEntity with HTTP 204 No Content if deletion is successful,
     **         or HTTP 404 Not Found if the service does not exist **/
    public boolean deleteProduct(int id) {
        // If Product does not exist return false
        if (!productRepository.existsById(id)) {
            return false;
        }
        // Else product does exist, thus return true and delete the product via its ID
        productRepository.deleteById(id);
        return true;
    }

    // Update Product - UPDATE
    // The frontend sends a JSON body containing the fields to update (name, price, etc.)
    /** @param id the unique id of the product to update **/
    /** @param updates a Map containing field names as keys and new values as values **/
    /** @return ResponseEntity containing the updated service (HTTP 200 OK) if found and updated,
     **         or HTTP 404 Not Found if the product with the given ID does not exist **/
    public Product updateProduct(int id, Map<String, Object> updates) {
        // Find product by id
        Product product = productRepository.findById(id).orElse(null);

        // Check if the product has been found
        if (product == null) {
            return null; // Product not found
        }

        // Iterate over each field in the `updates` map
        // The map comes from the frontend and contains key/value pairs of fields to update
        updates.forEach((field, value) -> {
            switch (field) {
                case "name" -> product.setName((String) value);
                case "EAN" -> product.setEAN((String) value);
                case "type" -> product.setType((String) value);
                case "price" -> {
                    // Handle price being either a number or string
                    // Frontend sends a numeric value (Double) after parsing EU/US formats
                    if (value instanceof Number num) {
                        product.setPrice(num.doubleValue()); // Store numeric value directly
                    } else {
                        // Safety fallback: Parse string as Double
                        product.setPrice(Double.parseDouble(value.toString()));
                    }
                }
                default -> System.out.println("Unknown field: " + field);
            }
        });

        // Save the new changes to the database
        return productRepository.save(product);
    }

    // Custom Search Method for Products - HELPER METHOD
    // This method allows searching for products by name, returning all products if no keyword is provided
    // Searches for jobs by title containing a keyword (case-sensitive)
    /** @param keyword the search string **/
    /** @return list of matching Job entities **/
    @Override
    public List<Product> search(String keyword) {
        // Check if the keyword is null or blank
        if (keyword == null || keyword.isBlank()) {
            // If no keyword is provided, return the full list of products
            return productRepository.findAll();
        }

        // Otherwise, perform a search using the repository
        // This assumes 'search' in the repository performs a case-insensitive partial match on product names
        return productRepository.search(keyword);
    }
}
