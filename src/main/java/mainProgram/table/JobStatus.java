package mainProgram.table;

import jakarta.persistence.*;

/**
 * Entity class representing the status of a job in the system.
 *
 * <p>This class serves as a lookup/reference table that defines the possible states
 * a job can be in (e.g., "Pending", "In Progress", "Completed", "Cancelled"). It
 * establishes a one-to-many relationship where multiple jobs can share the same status.</p>
 *
 * <p>Key characteristics:</p>
 * <ul>
 *   <li>Uses manually assigned IDs (not auto-generated) for predictable status codes</li>
 *   <li>Status names are unique across the system</li>
 *   <li>Acts as a reference table for Job entities</li>
 * </ul>
 *
 * @see Job
 */
@Entity
@Table(name = "job_status")
public class JobStatus {

  /**
   * Unique identifier for the job status.
   *
   * <p>Unlike the Job entity, this ID is not auto-generated. It uses manually
   * assigned values (typically small integers like 1, 2, 3) to represent specific
   * statuses. This allows for consistent, predictable status codes across the system.</p>
   *
   * <p>Note: Uses Short type to save space since the number of statuses is typically
   * small (e.g., less than 100).</p>
   */
  @Id
  private Short id;

  /**
   * Human-readable name of the job status.
   *
   * <p>This field must be unique and cannot be null. Examples might include:
   * "Pending", "In Progress", "Completed", "Cancelled", "On Hold".</p>
   *
   * <p>The uniqueness constraint ensures that each status name appears only once
   * in the database, preventing duplicate status definitions.</p>
   */
  @Column(nullable = false, unique = true)
  private String name;

  // Getters and Setters

  /**
   * Gets the unique identifier of the job status.
   *
   * @return the status ID
   */
  public Short getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the job status.
   *
   * <p>Note: This should typically only be set when creating new status records,
   * and should not be changed once the status is in use.</p>
   *
   * @param id the status ID to set
   */
  public void setId(Short id) {
    this.id = id;
  }

  /**
   * Gets the name of the job status.
   *
   * @return the status name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the job status.
   *
   * @param name the status name to set (must be unique and non-null)
   */
  public void setName(String name) {
    this.name = name;
  }
}
