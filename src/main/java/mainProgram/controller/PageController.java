package mainProgram.controller; // Project Organization

/* --- Imports --- */

import java.util.List;
import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import mainProgram.repository.JobServiceRepository;
import mainProgram.repository.ProductRepository;
import mainProgram.repository.ServiceRepository;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import mainProgram.table.Product;
import mainProgram.table.Services;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/* --- PageController Class --- */
// Controller responsible for serving HTML pages (views) for the application.
// This controller handles navigation and prepares model data for Thymeleaf templates.
// Unlike REST controllers, this returns view names that are resolved to HTML templates.
@Controller
@RequestMapping("/")
public class PageController {

    // Attributes
    private final JobRepository jobRepository;
    private final JobService jobService;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final JobPartRepository jobPartRepository;
    private final JobServiceRepository jobServiceRepository;

    // Constructor for Dependency Injection

    /**
     * @param jobRepository     the repository for accessing job data
     * @param jobService        the service layer for business logic related to jobs
     * @param productRepository the repository for accessing product data
     * @param jobPartRepository the repository for connecting jobs and products
     **/
    public PageController(
        JobRepository jobRepository,
        JobService jobService,
        ProductRepository productRepository,
        ServiceRepository serviceRepository,
        JobPartRepository jobPartRepository,
        JobServiceRepository jobServiceRepository
    ) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
        this.jobPartRepository = jobPartRepository;
        this.jobServiceRepository = jobServiceRepository;
    }

    // Methods
    // Handles requests to the root URL and redirects to the calendar page.

    /**
     * @return redirect directive to the calendar page
     **/
    @GetMapping("")
    public String home() {
        return "redirect:kalender";
    }

    // Displays the calendar page showing all jobs in a calendar view.

    /**
     * @return the name of the calendar template (calendar.html)
     **/
    @GetMapping("kalender")
    public String calendarPage() {
        return "calendar";
    }

    // Displays the job details page (empty/default view)
    // This endpoint shows the job details template without pre-loading specific job data.

    /**
     * @return the name of the job details template (jobDetails.html)
     **/
    @GetMapping("job-detaljer")
    public String jobDetails() {
        return "jobDetails";
    }

    // This method handles requests to the products page and populates the model for the active tab.
    // Depending on the 'filter' parameter, either the products table or the services table
    // will be shown on the page. Both lists are added to the model so Thymeleaf can
    // render the correct table based on the active tab.
    /** @param filter the active tab filter; expected values are "products" or "services" **/
    /** @param model the Spring MVC model used to pass data to the view **/
    /**
     * @return the name of the Thymeleaf template ("products.html") to render
     **/
    @GetMapping("products")
    public String productsPage(@RequestParam(defaultValue = "products") String filter, Model model) {
        // Track which tab is active (Products OR Services)
        model.addAttribute("currentFilter", filter);

        // Retrieve all products from the repository and sort by ID in ascending order.
        // Sorting by ID ensures consistent ordering in the product table on the product page.
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        // Add the list of products to the model with the key "products".
        // This allows the Thymeleaf template (products.html) to access the products
        // using the variable name "products" for rendering in tables, forms, etc.
        model.addAttribute("products", products);

        // Retrieve all services from the repository and sort by ID in ascending order.
        // Sorting by ID ensures consistent ordering in the service table on the product page.
        List<Services> services = serviceRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        // Add the list of services to the model with the key "services".
        // This allows the Thymeleaf template (products.html) to access the services
        // using the variable name "services" for rendering in tables, forms, etc.
        model.addAttribute("services", services); // Add services to the model

        // Return the name of the template to be rendered.
        // Spring resolves this to "templates/products.html" by default.
        return "products";
    }

    // Displays the job list page with all jobs sorted by date in ascending order.
    // Populates the model with a list of all jobs for rendering in the template.
    /** @param model the Spring MVC model to pass data to the view **/
    /**
     * @return the name of the job list template (jobliste.html)
     **/
    @GetMapping("jobliste")
    public String jobliste(Model model) {
        // Fetch all jobs ordered by date (earliest first)
        model.addAttribute("jobs", jobRepository.findAllByOrderByDateAsc());

        return "jobliste";
    }

    // Displays detailed information for a specific job, including associated parts.
    // Retrieves the job and its related parts, then populates the model for the detail view.
    /** @param id the ID of the job to display **/
    /** @param model the Spring MVC model to pass data to the view **/
    /**
     * @return the name of the job details template (jobDetails.html)
     **/
    @GetMapping("jobliste/{id}")
    public String jobDetails(@PathVariable int id, Model model) {
        // Retrieve the job by ID
        Job job = jobService.getJobById(id);

        // Retrieve all parts associated with this job
        // List<JobPart> jobParts = jobService.getPartsForJob((long) id);

        // Add data to the model for rendering in the template
        model.addAttribute("job", job);
        model.addAttribute("jobParts", jobPartRepository.findByJobId(id));
        model.addAttribute("jobService", jobServiceRepository.findByJobId(id));

        return "jobDetails";
    }
}
