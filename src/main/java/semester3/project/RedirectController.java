package semester3.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
    @GetMapping("/")
    public String redirectToNew() {
        // Redirects to /new
        return "redirect:/hello";
    }
}
