package mainProgram.repository;

import java.util.List;
import mainProgram.table.JobPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {
    List<JobPart> findByJobId(int jobId);
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
