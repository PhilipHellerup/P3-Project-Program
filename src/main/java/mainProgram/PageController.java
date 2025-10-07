package mainProgram; // Project Organization

/* --- Libraries --- */
// Marks this class as a Spring MVC Controller (Model - View - Controller)
import org.springframework.stereotype.Controller;

// Maps HTTP GET requests to specific methods
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/* --- PageController Class --- */
// Handles requests that return HTML views (html files).
@Controller
public class PageController {

    private final UserRepository userRepository;

    public PageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // Handles requests to the root URL ("/") and serves index.html from templates directory
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "index"; // -> src/main/resources/templates/index.html
    }

    @GetMapping("/job-detaljer")
    public String jobDetails(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "jobDetails"; // -> src/main/resources/templates/jobDetails.html
    }
}
