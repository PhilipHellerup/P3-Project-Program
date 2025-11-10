package mainProgram.repository;

import java.util.List;
import mainProgram.table.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Services, Integer> {
    List<Services> findByNameContainingIgnoreCase(String keyword);
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
