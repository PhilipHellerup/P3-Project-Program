package mainProgram.services; // Project Organization

/* --- Imports --- */
import mainProgram.repository.ProductRepository;
import mainProgram.table.Product;
import org.springframework.stereotype.Service;
import java.util.List;

//#TODO MAKE COMMENTS!
/* --- ProductService Class --- */
@Service
public class ProductService implements BaseSearchService<Product> {
    // Attributes
    private final ProductRepository productRepository;

    // Constructor
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Methods
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
