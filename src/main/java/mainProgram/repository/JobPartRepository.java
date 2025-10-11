package mainProgram.repository;

import java.util.List;
import mainProgram.JobPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {
  List<JobPart> findByJobId(Long jobId);
  // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
