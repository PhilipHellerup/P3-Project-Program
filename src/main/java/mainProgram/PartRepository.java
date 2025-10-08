package mainProgram;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
