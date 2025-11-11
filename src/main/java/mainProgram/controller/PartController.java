package mainProgram.controller;

import java.util.List;
import mainProgram.repository.JobPartRepository;
import mainProgram.repository.ProductRepository;
import mainProgram.table.JobPart;
import mainProgram.table.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// This file is optional and is purely if you want to access the table HTTP endpoint

@RestController
@RequestMapping("/")
public class PartController {

    private final ProductRepository productRepository;
    private final JobPartRepository jobPartRepository;

    public PartController(ProductRepository productRepository, JobPartRepository jobPartRepository) {
        this.productRepository = productRepository;
        this.jobPartRepository = jobPartRepository;
    }

    @GetMapping("api/part")
    @ResponseBody
    public List<Product> getAllParts() {
        return productRepository.findAll();
    }

    @GetMapping("api/products")
    @ResponseBody
    public List<JobPart> getAllProducts() {
        return jobPartRepository.findAll();
    }

    @GetMapping("api/products/{id}")
    @ResponseBody
    public List<JobPart> getAllProducts(@PathVariable int id) {
        return jobPartRepository.findByJobId(id);
    }
}
