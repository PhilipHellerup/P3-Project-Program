package mainProgram.repository;

import mainProgram.table.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for JobStatus entity database operations.
 *
 * <p>This repository manages job status lookup data, which represents the various states
 * a job can be in (e.g., "Not Delivered", "In Progress", "Completed", etc.). Since job
 * statuses are typically reference data that changes infrequently, this repository
 * primarily provides read operations.</p>
 *
 * <p>Spring Data JPA automatically provides the following inherited methods:</p>
 * <ul>
 *   <li>findAll() - Retrieve all job statuses</li>
 *   <li>findById(Short id) - Find a status by its ID</li>
 *   <li>save(JobStatus status) - Create or update a status</li>
 *   <li>delete(JobStatus status) - Delete a status</li>
 *   <li>deleteById(Short id) - Delete a status by ID</li>
 *   <li>count() - Count total number of statuses</li>
 *   <li>existsById(Short id) - Check if a status exists</li>
 * </ul>
 *
 * <p><strong>Note:</strong> The ID type is {@code Short} rather than {@code Integer} because
 * status codes typically use a small range of values (1-6 for example), making a smaller
 * data type more efficient.</p>
 *
 * @see JobStatus
 * @see JpaRepository
 */
@Repository
public interface JobStatusRepository extends JpaRepository<JobStatus, Short> {
  // No custom query methods needed - all operations use inherited CRUD methods
}
