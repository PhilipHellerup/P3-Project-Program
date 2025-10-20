package mainProgram.controller;

import java.util.List;
import mainProgram.JobService;
import mainProgram.repository.JobRepository;
import mainProgram.table.Job;
import mainProgram.table.JobPart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for serving HTML pages (views) for the application.
 * This controller handles navigation and prepares model data for Thymeleaf templates.
 * Unlike REST controllers, this returns view names that are resolved to HTML templates.
 */
@Controller
@RequestMapping("/")
public class PageController {

  private final JobRepository jobRepository;
  private final JobService jobService;

  /**
   * Constructor for dependency injection.
   *
   * @param jobRepository the repository for accessing job data
   * @param jobService the service layer for business logic related to jobs
   */
  public PageController(JobRepository jobRepository, JobService jobService) {
    this.jobRepository = jobRepository;
    this.jobService = jobService;
  }

  /**
   * Handles requests to the root URL and redirects to the calendar page.
   *
   * @return redirect directive to the calendar page
   */
  @GetMapping("")
  public String home() {
    return "redirect:kalender";
  }

  /**
   * Displays the calendar page showing all jobs in a calendar view.
   *
   * @return the name of the calendar template (calendar.html)
   */
  @GetMapping("kalender")
  public String calendarPage() {
    return "calendar";
  }

  /**
   * Displays the job details page (empty/default view).
   * This endpoint shows the job details template without pre-loading specific job data.
   *
   * @return the name of the job details template (jobDetails.html)
   */
  @GetMapping("job-detaljer")
  public String jobDetails() {
    return "jobDetails";
  }

  /**
   * Displays the products list page with all products.
   * Populates the model with a list of all products for rendering in the template.
   *
   * @return the name of the product list template (products.html)
   */
  @GetMapping("produktliste")
  public String products() {
        return "products";
    }

  /**
   * Displays the job list page with all jobs sorted by date in ascending order.
   * Populates the model with a list of all jobs for rendering in the template.
   *
   * @param model the Spring MVC model to pass data to the view
   * @return the name of the job list template (jobliste.html)
   */
  @GetMapping("jobliste")
  public String jobliste(Model model) {
    // Fetch all jobs ordered by date (earliest first)
    model.addAttribute("jobs", jobRepository.findAllByOrderByDateAsc());
    return "jobliste";
  }

  /**
   * Displays detailed information for a specific job, including associated parts.
   * Retrieves the job and its related parts, then populates the model for the detail view.
   *
   * @param id the ID of the job to display
   * @param model the Spring MVC model to pass data to the view
   * @return the name of the job details template (jobDetails.html)
   */
  @GetMapping("jobliste/{id}")
  public String jobDetails(@PathVariable int id, Model model) {
    // Retrieve the job by ID
    Job job = jobService.getJobById(id);
    // Retrieve all parts associated with this job
    List<JobPart> jobParts = jobService.getPartsForJob((long) id);

    // Add data to the model for rendering in the template
    model.addAttribute("job", job);
    model.addAttribute("jobParts", jobParts);

    return "jobDetails";
  }
}
