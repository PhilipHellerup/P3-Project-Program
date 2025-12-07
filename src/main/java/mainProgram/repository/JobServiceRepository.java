package mainProgram.repository; // Project Organization

/* --- Imports --- */
import mainProgram.table.JobServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/* --- JobServiceRepository Interface --- */
// Repository interface for CRUD operations on the JobServices table.
// Spring Data JPA automatically generates the implementation at runtime.
@Repository
// Marks this interface as a Spring-managed repository component (Not needed as it extends JpaRepository, but added for clarity)
public interface JobServiceRepository extends JpaRepository<JobServices, Long> {
    /// Methods

    // Custom Query Method - READ
    // Fetch all JobServices entries that belong to a specific job
    List<JobServices> findByJobId(int jobId);

    // Custom Query Method - DELETE
    // Delete a single JobServices entry based on both jobId and serviceId
    void deleteByJobIdAndServiceId(Integer jobId, Integer serviceId);

    // Custom Query Method - DELETE
    /** @Transactional ensures this delete operation runs inside a database transaction **/
    @Transactional // Required for the delete operations that may affect multiple rows
    void deleteByJobId(int jobId);

    // Spring Data auto-provides the common CRUD methods:
    // findAll(), findById(), save(), delete(), etc.
}
