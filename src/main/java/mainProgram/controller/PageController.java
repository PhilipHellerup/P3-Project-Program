package mainProgram.controller; // Project Organization

/* --- Imports --- */
import java.util.List;
import mainProgram.repository.JobServiceRepository;
import mainProgram.table.Product;
import mainProgram.table.Services;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import mainProgram.services.JobService;
import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import mainProgram.repository.ProductRepository;
import mainProgram.repository.ServiceRepository;
import mainProgram.table.Job;
import org.springframework.web.bind.annotation.RequestParam;

/* --- PageController Class --- */
// Controller responsible for serving HTML pages (Thymeleaf templates).
// Handles navigation, prepares model data, and returns the view names.
// Unlike REST controllers, this returns the template name (resolved to HTML) rather than JSON.
@Controller
@RequestMapping("/") // Base path for all PageController routes
public class PageController {
    /// Attributes (Repositories & Services for loading model data)
    private final JobRepository jobRepository;
    private final JobService jobService;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final JobPartRepository jobPartRepository;
    private final JobServiceRepository jobServiceRepository;

    /// Constructor (Dependency Injection)
    public PageController(JobRepository jobRepository, JobService jobService, ProductRepository productRepository, ServiceRepository serviceRepository, JobPartRepository jobPartRepository, JobServiceRepository jobServiceRepository) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.jobPartRepository = jobPartRepository;
        this.jobServiceRepository = jobServiceRepository;
    }

    /// Methods

    // Redirect root URL to the Calendar Page
    /** @return redirect directive to the Calendar Page **/
    @GetMapping("")
    public String home() {
        // Redirecting to Calendar Page
        return "redirect:kalender";
    }

    // Displays the calendar page showing all jobs in a calendar view.

    // Displays the Calendar Page showing all jobs in a calendar view.
    /** @return the name of the calendar template (calendar.html) **/
    @GetMapping("kalender")
    public String calendarPage() {
        // Display Calendar Page
        return "calendar";
    }

    // Displays the Job Details Page (empty/default view)
    // This endpoint shows the Job Details template without preloading specific job data.
    /** @return the name of the Job Details template (jobDetails.html) **/
    @GetMapping("job-detaljer")
    public String jobDetails() {
        // Display Job Details Page
        return "jobDetails";
    }

    // Display Products/Parts & Services Page with an active tab
    /** @param filter active tab filter, expected values are "products" or "services" **/
    /** @param model the Spring MVC Model used to pass data to the view **/
    /** @return the name of the Thymeleaf template ("products.html") to render **/
    @GetMapping("products")
    public String productsPage(@RequestParam(defaultValue = "products") String filter, Model model) {
        // Track which tab is active (Products OR Services)
        model.addAttribute("currentFilter", filter);

        // Fetch ALL products/parts from the repository, sorted by ID ascending order.
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("products", products); // Add products/parts to the model for Thymeleaf

        // Fetch ALL services from the repository, sorted by ID ascending order.
        List<Services> services = serviceRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("services", services); // Add services to the model for Thymeleaf

        // Return the view template for rendering ("products.html")
        return "products";
    }

    // Display detailed job information for a specific job
    /** @param model the Spring MVC Model to pass the job and related parts/services to the view **/
    /** @return the name of the job list template ("jobliste.html") **/
    @GetMapping("jobliste")
    public String jobliste(Model model) {
        // Fetch all jobs ordered by date ascending (Earliest First)
        model.addAttribute("jobs", jobRepository.findAllByOrderByDateAsc());

        // Return the view template for rendering ("jobliste.html")
        return "jobliste";
    }

    // Display detailed job information for a specific job
    /** @param id the ID of the job to display **/
    /** @param model the Spring MVC Model to pass the job and related parts/services to the view **/
    /** @return the name of the job details template (jobDetails.html) **/
    @GetMapping("jobliste/{id}")
    public String jobDetails(@PathVariable int id, Model model) {
        // Fetch job by ID using the service layer
        Job job = jobService.getJobById(id);
        model.addAttribute("job", job);

        // Fetch all parts associated with this job from the repository
        model.addAttribute("jobParts", jobPartRepository.findByJobId(id));

        // Fetch all service associated with this job from the repository
        model.addAttribute("jobService", jobServiceRepository.findByJobId(id));

        // Return the view template for rendering ("jobDetails.html")
        return "jobDetails";
    }
}
