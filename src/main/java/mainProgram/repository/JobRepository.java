package mainProgram.repository;

import mainProgram.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
    List<Job> findAllByOrderByDateAsc();
    // Or for newest first:
    List<Job> findAllByOrderByDateDesc();
}

