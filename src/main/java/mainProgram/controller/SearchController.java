package mainProgram.controller;

import java.util.List;

import mainProgram.services.JobService;
import mainProgram.services.ServiceService;
import mainProgram.services.productService;
import mainProgram.table.Job;
import mainProgram.table.Product;
import mainProgram.table.Services;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final JobService jobService;
    private final productService productService;
    private final ServiceService serviceService;

    public SearchController(JobService jobService, productService productService, ServiceService serviceService) {
        this.jobService = jobService;
        this.productService = productService;
        this.serviceService = serviceService;
    }

    @GetMapping("/job")
    public List<Job> searchRepair(@RequestParam String q) {
        return jobService.search(q);
    }

    @GetMapping("/repair")
    public List<Product> searchProduct(@RequestParam String q) {
        return productService.search(q);
    }

    @GetMapping("/service")
    public List<Services> searchService(@RequestParam String q) {
        return serviceService.search(q);
    }
}