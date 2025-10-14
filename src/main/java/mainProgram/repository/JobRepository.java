package mainProgram.repository;

import java.util.List;
import mainProgram.table.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
  // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
  List<Job> findAllByOrderByDateAsc();
  // Or for newest first:
  List<Job> findAllByOrderByDateDesc();
}
