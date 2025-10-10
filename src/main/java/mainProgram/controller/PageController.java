package mainProgram.controller; // Project Organization

/* --- Libraries --- */
// Marks this class as a Spring MVC Controller (Model - View - Controller)
import mainProgram.Job;
import mainProgram.JobPart;
import mainProgram.JobService;
import mainProgram.repository.JobRepository;
import org.springframework.stereotype.Controller;

// Maps HTTP GET requests to specific methods
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/* --- PageController Class --- */
// Handles requests that return HTML views (html files).
@Controller
@RequestMapping("/")
public class PageController {
    private final JobRepository jobRepository;
    private final JobService jobService;

    public PageController(JobRepository jobRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }
    // Handles requests to the root URL ("/") and serves index.html from templates directory
    @GetMapping("")
    public String home() {
        return "redirect:calendar";
    }

    @GetMapping("calendar")
    public String calendarPage() {
        return "calendar";
    }

    @GetMapping("job-detaljer")
    public String jobDetails() {
        return "jobDetails"; // -> src/main/resources/templates/jobDetails.html
    }

   @GetMapping("jobliste")
   public String jobliste(Model model) {
        model.addAttribute("jobs", jobRepository.findAllByOrderByDateAsc());
        return "jobliste";
   }

    @GetMapping("jobliste/{id}")
    public String jobDetails(@PathVariable int id, Model model) {
        Job job = jobService.getJobById(id);
        List<JobPart> jobParts = jobService.getPartsForJob((long) id);

        model.addAttribute("job", job);
        model.addAttribute("jobParts", jobParts);

        return "jobDetails";
    }
}
