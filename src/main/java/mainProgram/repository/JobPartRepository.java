package mainProgram.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import mainProgram.table.JobPart;
import org.springframework.transaction.annotation.Transactional;

public interface JobPartRepository extends JpaRepository<JobPart, Long> {
    List<JobPart> findByJobId(int jobId);

    void deleteByJobIdAndProductId(Integer jobId, Integer partId);
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.

    @Transactional
    void deleteByJobId(int jobId);
}
