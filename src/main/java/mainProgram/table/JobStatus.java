package mainProgram.table; // Project Organization

/* --- Imports --- */
import jakarta.persistence.*;

/* --- JobStatus Entity --- */
// Represents a lookup table for job status states (notDelivered, inProgress, finished, etc.).
// Multiple Job entries may reference one status (one-to-many relationship).
@Entity // Marks this class as a JPA entity mapped to a database table
@Table(name = "job_status") // Defines the database table name for JobStatus
public class JobStatus {
    /// Attributes

    // Unique ID for the status (manually assigned for predictable values)
    @Id // Marks this field as the primary key
    private Short id;

    // Human-readable status name — must be unique system-wide
    @Column(nullable = false, unique = true) // Cannot be null and must be unique
    private String name;

    /// Getters

    // Gets the unique identifier of the job status.
    /** @return the status ID **/
    public Short getId() {
        return id;
    }

    // Gets the human-readable status name.
    /** @return status name **/
    public String getName() {
        return name;
    }

    /// Setters

    // Sets the status ID (typically only once when creating new status records).
    /** @param id the status ID to assign **/
    public void setId(Short id) {
        this.id = id;
    }

    // Sets the status name (must be unique and non-null).
    /** @param name the status name to assign **/
    public void setName(String name) {
        this.name = name;
    }
}
