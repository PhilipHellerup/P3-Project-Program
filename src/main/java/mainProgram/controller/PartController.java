package mainProgram.controller;

import java.util.List;
import mainProgram.Part;
import mainProgram.repository.PartRepository;
import org.springframework.web.bind.annotation.*;

// This file is optional and is purely if you want to access the table HTTP endpoint

@RestController
@RequestMapping("/parts_table")
public class PartController {

  private final PartRepository partRepository;

  public PartController(PartRepository partRepository) {
    this.partRepository = partRepository;
  }

  @GetMapping
  public List<Part> getAllParts() {
    return partRepository.findAll();
  }
}
