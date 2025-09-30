package mainProgram; // Project Organization

/* --- Libraries --- */
// Marks this class as a Spring MVC Controller (Model - View - Controller)
import org.springframework.stereotype.Controller;

// Maps HTTP GET requests to specific methods
import org.springframework.web.bind.annotation.GetMapping;


/* --- PageController Class --- */
// Handles requests that return HTML views (html files).
@Controller
public class PageController {
    // Handles requests to the root URL ("/") and serves index.html from html directory
    @GetMapping("/")
    public String home() {
        return "index"; // Spring looks for html/index.html
    }
}
