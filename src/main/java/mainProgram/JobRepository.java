package mainProgram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}

