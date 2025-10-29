package mainProgram.controller;

import java.util.List;
import mainProgram.repository.ProductRepository;
import mainProgram.table.Product;
import org.springframework.web.bind.annotation.*;

// This file is optional and is purely if you want to access the table HTTP endpoint

@RestController
@RequestMapping("/")
public class PartController {

  private final ProductRepository productRepository;

  public PartController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GetMapping("api/part")
  @ResponseBody
  public List<Product> getAllParts() {
    return productRepository.findAll();
  }
}
