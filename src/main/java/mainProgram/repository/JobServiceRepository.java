package mainProgram.repository;

import java.util.List;
import mainProgram.services.JobService;
import mainProgram.table.JobPart;
import mainProgram.table.JobServices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobServiceRepository extends JpaRepository<JobServices, Long> {
    List<JobServices> findByJobId(int jobId);

    void deleteByJobIdAndServiceId(Integer jobId, Integer serviceId);
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
