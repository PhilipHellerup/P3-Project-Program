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

    /// Create Product - CREATE
    public Product createProduct(Product product) {
        // Save the product to the database using the repository and its .save() method
        return productRepository.save(product);
    }

    // Read Product - READ
    public Product getProduct(int id) {
        // Get the product from the database via its ID
        return productRepository.findById(id).orElse(null);
    }

    // Delete Product - DELETE
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
    public Product updateProduct(int id, Map<String, Object> updates) {
        // Find product by id
        Product product = productRepository.findById(id).orElse(null);

        // Check if product has been found
        if (product == null) {
            return null; // Product not found
        }

        // Iterate over each field in the updates map
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

    // TODO: COMMENTS ON THIS METHOD (SEARCH)!
    @Override
    public List<Product> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            // Return all products if no keyword provided
            return productRepository.findAll();
        }
        // Search only by name (case-insensitive, partial match)
        return productRepository.search(keyword);
    }
}
