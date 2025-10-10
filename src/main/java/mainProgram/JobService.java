package mainProgram;

import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final JobPartRepository jobPartRepository;

    public JobService(JobRepository jobRepository, JobPartRepository jobPartRepository) {
        this.jobRepository = jobRepository;
        this.jobPartRepository = jobPartRepository;
    }

    public Job getJobById(int id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public List<JobPart> getPartsForJob(Long jobId) {
        return jobPartRepository.findByJobId(jobId);
    }
}