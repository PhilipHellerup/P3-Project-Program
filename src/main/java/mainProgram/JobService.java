package mainProgram;

import java.util.List;

import org.springframework.stereotype.Service;

import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import mainProgram.table.Job;
import mainProgram.table.JobPart;

@Service
public class JobService {

  private final JobRepository jobRepository;
  private final JobPartRepository jobPartRepository;

  public JobService(JobRepository jobRepository, JobPartRepository jobPartRepository) {
    this.jobRepository = jobRepository;
    this.jobPartRepository = jobPartRepository;
  }

  public Job getJobById(int id) {
    return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
  }

  public List<JobPart> getPartsForJob(int jobId) {
    return jobPartRepository.findByJobId(jobId);
  }
}
