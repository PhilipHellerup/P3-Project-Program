package mainProgram.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import mainProgram.table.JobPart;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {
  List<JobPart> findByJobId(int jobId);
  // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
