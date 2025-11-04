package mainProgram.services;

import java.util.List;
import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import mainProgram.table.Job;
import mainProgram.table.JobPart;
import org.springframework.stereotype.Service;

@Service
public class JobService implements BaseSearchService<Job> {

  private final JobRepository jobRepository;
  private final JobPartRepository jobPartRepository;

  public JobService(JobRepository jobRepository, JobPartRepository jobPartRepository) {
    this.jobRepository = jobRepository;
    this.jobPartRepository = jobPartRepository;
  }

  public Job getJobById(int id) {
    return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
  }

  public List<JobPart> getPartsForJob(Long jobId) {
    return jobPartRepository.findByJobId(jobId);
  }

  // Custom search function for job/ repair
  @Override
  public List<Job> search(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return List.of();
    }
    return jobRepository.findByTitleContainingIgnoreCase(keyword);
  }
}
