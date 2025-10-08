package mainProgram.controller;

import mainProgram.Job;
import mainProgram.JobRepository;
import mainProgram.JobStatus;
import mainProgram.JobStatusRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class JobController {

    private final JobRepository jobRepository;
    private final JobStatusRepository statusRepository;

    public JobController(JobRepository jobRepository, JobStatusRepository statusRepository) {
        this.jobRepository = jobRepository;
        this.statusRepository = statusRepository;
    }

    // Serve Thymeleaf calendar page
    @GetMapping("calendar")
    public String calendarPage(Model model) {
        return "calendar";
    }

    // REST: list jobs
    @GetMapping("api/jobs")
    @ResponseBody
    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    // REST: create job
    @PostMapping("api/jobs")
    @ResponseBody
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        if (job.getDate() == null || job.getTitle() == null) return ResponseEntity.badRequest().build();
        // Ensure status is valid
        if (job.getStatus() == null || job.getStatus().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        JobStatus status = statusRepository.findById(job.getStatus().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid status_id"));
        job.setStatus(status);
        Job saved = jobRepository.save(job);
        return ResponseEntity.ok(saved);
    }

    // REST: update job
    @PutMapping("api/jobs/{id}")
    @ResponseBody
    public ResponseEntity<Job> updateJob(@PathVariable Integer id, @RequestBody Job job) {
        return jobRepository.findById(id).map(existing -> {
            existing.setTitle(job.getTitle());
            existing.setCustomer_name(job.getCustomer_name());
            existing.setCustomer_phone(job.getCustomer_phone());
            existing.setJob_description(job.getJob_description());
            existing.setWork_time_minutes(job.getWork_time_minutes());
            existing.setPrice_per_minute(job.getPrice_per_minute());
            existing.setDate(job.getDate());

            if (job.getStatus() != null && job.getStatus().getId() != null) {
                JobStatus status = statusRepository.findById(job.getStatus().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid status_id"));
                existing.setStatus(status);
            }

            Job updated = jobRepository.save(existing);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
