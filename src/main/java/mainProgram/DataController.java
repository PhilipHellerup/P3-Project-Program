package mainProgram; // Project Organization

/* --- Libraries --- */
// Marks this class as a Spring MVC Controller (Model - View - Controller)
import org.springframework.stereotype.Controller;

// Maps HTTP GET requests to specific methods
import org.springframework.web.bind.annotation.GetMapping;

// Marks this class as a REST controller (returns data directly, NOT an HTML view)
import org.springframework.web.bind.annotation.RestController;


/* --- DataController Class --- */
// Handles GET requests that return data (text, JSON, etc.) instead of HTML views.
@RestController
public class DataController {
    // Handles requests to "/quote" and directly returns plain text as a response
    @GetMapping("/quote")
    public String quote() {
        return "And in case I don't see you, good afternoon, good evening, and good night! - Jim Carrey";
    }
}
