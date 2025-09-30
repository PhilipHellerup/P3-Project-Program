package project.program;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

    // Serve index.html from templates/
    @GetMapping("/")
    public String home() {
        return "index"; // Spring will look for templates/index.html
    }

    // REST endpoint returning greeting text
    @GetMapping("/greet")
    @ResponseBody
    public String greet() {
        return "Hello, welcome to my Spring Boot app!";
    }
}