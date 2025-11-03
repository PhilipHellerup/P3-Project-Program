package mainProgram.controller;

import java.util.List;
import mainProgram.services.JobService;
import mainProgram.table.Job;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

  private final JobService jobService;

  public SearchController(JobService jobService) {
    this.jobService = jobService;
  }

  @GetMapping("/job")
  public List<Job> searchItems(@RequestParam String q) {
    return jobService.search(q);
  }
}
