package mainProgram.repository;

import mainProgram.JobPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {
    List<JobPart> findByJobId(Long jobId);
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
