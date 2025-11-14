package mainProgram.repository;

import mainProgram.services.JobService;
import mainProgram.table.JobPart;
import mainProgram.table.JobServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobServiceRepository extends JpaRepository<JobServices, Long> {
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
    List<JobServices> findByJobId(int jobId);

    void deleteByJobIdAndServiceId(Integer jobId, Integer serviceId);

    @Transactional
    void deleteByJobId(int jobId);
}
