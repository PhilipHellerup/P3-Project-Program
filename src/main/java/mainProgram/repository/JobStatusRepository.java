package mainProgram.repository;// Project Organization

/* --- Imports --- */
import mainProgram.table.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* --- JobStatusRepository Interface --- */
// Repository interface for CRUD & Lookup Operations on the JobStatus table.
// JobStatus represents the different lifecycle states of a repair/job
// (e.g., "Not Delivered", "Received", "Under Repair", "Finished").
// Spring Data JPA automatically generates the implementation at runtime,
// meaning we only define the interface — no manual SQL or classes required.
// Notes:
// - ID type is Short instead of Integer because status codes use a small numeric range.
// - This repository mostly performs read operations since job statuses change rarely.
@Repository // Marks this interface as a Spring-managed repository component (Not needed as it extends JpaRepository, but added for clarity)
public interface JobStatusRepository extends JpaRepository<JobStatus, Short> {
    // Spring Data auto-provides the common CRUD methods:
    // findAll(), findById(), save(), delete(), etc.
}
