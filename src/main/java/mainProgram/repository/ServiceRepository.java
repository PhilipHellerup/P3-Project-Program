package mainProgram.repository; // Project Organization

/* --- Imports --- */
import mainProgram.table.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/* --- ServiceRepository Interface --- */
// Repository interface for handling database operations related to the Services entity.
// Extends JpaRepository to automatically inherit CRUD operations (save, findById, findAll, delete, etc.).
@Repository // Marks this interface as a Spring-managed repository component (Not needed as it extends JpaRepository, but added for clarity)
public interface ServiceRepository extends JpaRepository<Services, Integer> {
    /// Methods

    // Search Service by Name - READ
    /** @param name part or full name string to search for (case-insensitive) **/
    /** @return list of services where the name contains the given string **/
    List<Services> findByNameContainingIgnoreCase(String name);

    // Spring Data auto-provides the common CRUD methods:
    // findAll(), findById(), save(), delete(), etc.
}
