package mainProgram;

import org.springframework.web.bind.annotation.*;
import java.util.List;

// This file is optional and is purely if you want to access the table HTTP endpoint

@RestController
@RequestMapping("/jobs_table")
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}