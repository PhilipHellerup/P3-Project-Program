package mainProgram.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    ///  this is not working anymore, but maybe we need a search to only get parts somewhere.
//    @GetMapping("/part")
//    public List<Product> searchProduct(@RequestParam String q) {
//        return productService.search(q);
//    }
    @GetMapping("/service")
    public List<Services> searchService(@RequestParam String q) {
        return serviceService.search(q);
    }

    @GetMapping("/products")
    public List<Map<String, Object>> getAllItems(@RequestParam(required = false) String q) {
        List<Product> products = productService.search(q);
        List<Services> services = serviceService.search(q);

        List<Map<String, Object>> results = new ArrayList<>();

        for (Product p : products) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("price", p.getPrice());
            map.put("type", "part");
            results.add(map);
        }

        for (Services s : services) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("name", s.getName());
            map.put("price", s.getPrice());
            map.put("type", "service");
            results.add(map);
        }

        return results;
    }
}