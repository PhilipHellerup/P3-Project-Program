package mainProgram.controller; // Project Organization

/* --- Imports --- */
import java.util.List;
import java.util.Map;
import mainProgram.services.JobService;
import mainProgram.services.ProductService;
import mainProgram.services.SearchService;
import mainProgram.services.ServiceService;
import mainProgram.table.Job;
import mainProgram.table.Product;
import mainProgram.table.Services;
import org.springframework.web.bind.annotation.*;

/* --- SearchController --- */
// Central controller for search operations across Jobs, Products (Parts),
// and Services. Supports autocomplete, search bars, and combined search results.
@RestController
@RequestMapping("/api/search") // Base path for all API routes in this controller
@CrossOrigin(origins = "*")       // Allows requests from ANY domain (front-end compatibility)
public class SearchController {
    /// Attributes
    private final JobService jobService;         // Handles Job/Repair search logic
    private final ProductService productService; // Handles Product/Part search logic
    private final ServiceService serviceService; // Handles Service search logic
    private final SearchService searchService;   // Handles Combined search logic (Parts & Services)

    /// Constructor
    public SearchController(JobService jobService, ProductService productService, ServiceService serviceService, SearchService searchService) {
        this.jobService = jobService;
        this.productService = productService;
        this.serviceService = serviceService;
        this.searchService = searchService;
    }

    /// Methods

    // Search Jobs/Repairs
    /** @param keyword the search keyword used to match job titles **/
    /** @return a list of Jobs whose titles contain the keyword **/
    @GetMapping("/job")
    public List<Job> searchRepair(@RequestParam String keyword) {
        // Calling JobService search method
        return jobService.search(keyword);
    }

    // Search only Products/Parts
    /** @param keyword the search keyword used to match product/part names **/
    /** @return a list of products/parts whose names contain the keyword **/
    @GetMapping("/parts")
    public List<Product> searchProducts(@RequestParam String keyword) {
        // Calling ProductService search method
        return productService.search(keyword);
    }

    // Search only Services
    /** @param keyword the search keyword used to match service names **/
    /** @return a list of services whose names contain the keyword **/
    @GetMapping("/service")
    public List<Services> searchService(@RequestParam String keyword) {
        // Calling ServiceService search method
        return serviceService.search(keyword);
    }

    // Combined Search (Products + Services)
    /** @param keyword the search keyword used to match part and service names **/
    /** @return a unified list of both parts and services **/
    @GetMapping("/products")
    public List<Map<String, Object>> getAllItems(@RequestParam(required = false) String keyword) {
        // Calling SearchService searchAll (Combined Search) method
        return searchService.searchAll(keyword);
    }
}