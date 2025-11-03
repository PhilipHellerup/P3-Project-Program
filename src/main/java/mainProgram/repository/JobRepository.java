package mainProgram.repository;

import java.util.List;
import mainProgram.table.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Job entity database operations.
 *
 * <p>This interface extends Spring Data JPA's JpaRepository, which automatically provides
 * standard CRUD operations without requiring explicit implementation. Spring Data JPA
 * generates the implementation at runtime based on method naming conventions.</p>
 *
 * <p>Inherited methods include:</p>
 * <ul>
 *   <li>findAll() - Retrieve all jobs</li>
 *   <li>findById(Integer id) - Find a job by its ID</li>
 *   <li>save(Job job) - Create or update a job</li>
 *   <li>delete(Job job) - Delete a job</li>
 *   <li>deleteById(Integer id) - Delete a job by ID</li>
 *   <li>count() - Count total number of jobs</li>
 *   <li>existsById(Integer id) - Check if a job exists</li>
 * </ul>
 *
 * @see Job
 * @see JpaRepository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Integer>, SearchableRepository<Job> {
  /**
   * Retrieves all jobs ordered by date in ascending order (earliest first).
   *
   * <p>This is a derived query method - Spring Data JPA automatically generates
   * the query implementation based on the method name following the convention:
   * findAllByOrderBy[Property][Direction]</p>
   *
   * @return a list of all jobs sorted by date from oldest to newest
   */
  List<Job> findAllByOrderByDateAsc();

  /**
   * Retrieves all jobs ordered by date in descending order (newest first).
   *
   * <p>This is a derived query method - Spring Data JPA automatically generates
   * the query implementation based on the method name following the convention:
   * findAllByOrderBy[Property][Direction]</p>
   *
   * @return a list of all jobs sorted by date from newest to oldest
   */
  List<Job> findAllByOrderByDateDesc();
}
