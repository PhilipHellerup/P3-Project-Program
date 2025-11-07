package mainProgram.controller;

import java.util.List;

import mainProgram.services.JobService;
import mainProgram.services.productService;
import mainProgram.table.Job;
import mainProgram.table.Product;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final JobService jobService;
    private final productService productService;

    public SearchController(JobService jobService, productService productService) {
        this.jobService = jobService;
        this.productService = productService;
    }

    @GetMapping("/job")
    public List<Job> searchRepair(@RequestParam String q) {
        return jobService.search(q);
    }

    @GetMapping("/repair")
    public List<Product> searchProduct(@RequestParam String q) {
        return productService.search(q);
    }
}