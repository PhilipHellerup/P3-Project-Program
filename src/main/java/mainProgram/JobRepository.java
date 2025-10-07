package mainProgram;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
