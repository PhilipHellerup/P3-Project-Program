package mainProgram.repository; // Project Organization

/* --- Imports --- */
import java.util.List;
import mainProgram.table.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/* --- JobRepository Interface --- */
// Repository interface for performing CRUD operations on the Job table.
// Spring Data JPA automatically provides the implementation at runtime.
// Includes built-in methods such as findAll(), findById(), save(), delete(), deleteById(), count(), existsById().
@Repository // Marks this interface as a Spring-managed repository component (Not needed as it extends JpaRepository, but added for clarity)
public interface JobRepository extends JpaRepository<Job, Integer>, SearchableRepository<Job> {
    /// Methods
    // Custom Query Method - READ
    // Search jobs by title (case-insensitive, partial match)
    List<Job> findByTitleContainingIgnoreCase(String keyword);

    // Custom Query Method - READ
    // Search jobs by title, customer phone and customer name (case-insensitive, partial match)
    @Query("""
    SELECT j FROM Job j
    WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(j.customer_phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(j.customer_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<Job> searchJobs(String keyword);

    // Custom Query Method - READ
    // Fetch all jobs ordered by date (oldest -> newest)
    /** @return a list of all jobs sorted by date from oldest to newest **/
    List<Job> findAllByOrderByDateAsc();

    // Custom Query Method - READ
    // Fetch all jobs ordered by date (newest -> oldest)
    /** @return a list of all jobs sorted by date from newest to oldest **/
    List<Job> findAllByOrderByDateDesc();

    // Spring Data auto-provides the common CRUD methods:
    // findAll(), findById(), save(), delete(), etc.
}
