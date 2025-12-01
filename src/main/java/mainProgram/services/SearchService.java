package mainProgram.services; // Project Organization

/* --- Imports --- */
import mainProgram.table.Product;
import mainProgram.table.Services;
import org.springframework.stereotype.Service;
import java.util.*;

/* --- SearchService Class --- */
// Handles the business logic for service-related operations.
// The controller delegates work, keeping business rules out of the controller layer.
@Service
public class SearchService {
    /// Attributes
    private final ProductService productService; // Handles Product/Part search logic
    private final ServiceService serviceService; // Handles Service search logic

    // Constructor
    public SearchService(ProductService productService, ServiceService serviceService) {
        this.productService = productService;
        this.serviceService = serviceService;
    }

    // Performs a keyword search across both Products/Parts and Services.
    // Returns results in a unified data structure.
    /** @param keyword search term, if null/blank, returns ALL items **/
    /** @return List of Maps representing both Products and Services **/
    public List<Map<String, Object>> searchAll(String keyword) {
        // Delegate database queries to each service layer
        List<Product> products = productService.search(keyword);
        List<Services> services = serviceService.search(keyword);

        // List that will hold combined search results
        List<Map<String, Object>> results = new ArrayList<>();

        // Convert Product/Part entities into a unified response map
        for (Product p : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("price", p.getPrice());
            map.put("EAN", p.getEAN());
            map.put("type", "part");  // Identifies this as a product/part
            results.add(map);
        }

        // Convert Service objects into the same unified response map
        for (Services s : services) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("name", s.getName());
            map.put("price", s.getPrice());
            map.put("duration", s.getDuration());
            map.put("type", "service"); // Identifies this as a service
            results.add(map);
        }

        // Final combined search results
        return results;
    }
}