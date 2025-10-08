package mainProgram; // Project Organization

/* --- Libraries --- */
// Marks this class as a Spring MVC Controller (Model - View - Controller)
import mainProgram.database.UserRepository;
import org.springframework.stereotype.Controller;

// Maps HTTP GET requests to specific methods
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/* --- PageController Class --- */
// Handles requests that return HTML views (html files).
@Controller
public class PageController {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobService jobService;

    public PageController(UserRepository userRepository, JobRepository jobRepository, JobService jobService) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }
    // Handles requests to the root URL ("/") and serves index.html from templates directory
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index"; // -> src/main/resources/templates/index.html
    }

    @GetMapping("/job-detaljer")
    public String jobDetails() {
        return "jobDetails"; // -> src/main/resources/templates/jobDetails.html
    }

   @GetMapping("/jobliste")
   public String jobliste(Model model) {
        model.addAttribute("jobs", jobRepository.findAll());
        return "jobliste";
   }

    @GetMapping("/jobliste/{id}")
    public String jobDetails(@PathVariable Long id, Model model) {
        Job job = jobService.getJobById(id);
        List<JobPart> jobParts = jobService.getPartsForJob(id);

        model.addAttribute("job", job);
        model.addAttribute("jobParts", jobParts);

        return "jobDetails";
    }
}
