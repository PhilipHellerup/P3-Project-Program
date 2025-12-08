package mainProgram.repository; // Project Organization

/* --- Imports --- */
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import mainProgram.table.JobPart;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/* --- JobPartRepository Interface --- */
// Repository interface for performing CRUD operations on the JobPart table.
// Spring Data JPA automatically generates the implementation at runtime.
@Repository
// Marks this interface as a Spring-managed repository component (Not needed as it extends JpaRepository, but added for clarity)
public interface JobPartRepository extends JpaRepository<JobPart, Long> {
    /// Methods

    // Custom Query Method - READ
    // Fetch all JobPart entries that belong to a specific job
    List<JobPart> findByJobId(int jobId);

    // Custom Query Method - DELETE
    // Delete a single JobPart based on both jobId and partId
    void deleteByJobIdAndProductId(Integer jobId, Integer partId);

    // Custom Query Method - DELETE
    /** @Transactional ensures this delete operation runs inside a database transaction **/
    @Transactional // Required for the delete operations that modify multiple rows
    void deleteByJobId(int jobId);

    // Spring Data auto-provides the common CRUD methods:
    // findAll(), findById(), save(), delete(), etc.
}
