package mainProgram.table; // Project Organization

/* --- Imports --- */
import jakarta.persistence.*;
import java.time.LocalDateTime;

/* --- Job Entity --- */
// Represents a repair job entry stored in the "jobs" table.
// Contains customer information, pricing, work durations, scheduling date and status.
// Linked to JobStatus through a many-to-one relation.
@Entity // Marks this class as a JPA entity so it maps to a database table
@Table(name = "jobs") // Specifies the database table name used for this entity
public class Job {
    /// Attributes

    // Primary Key in PostgreSQL - Unique Auto-incrementing ID for each job
    @Id // Marks this field as the primary key of the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID using the database's identity (auto-increment)
    private Integer id;

    // Brief title describing the job
    private String title;

    // Customer name associated with the job
    private String customer_name;

    // Customer contact phone number
    private String customer_phone;

    // Detailed description of the job/problem
    private String job_description;

   // Time spent working, measured in minutes
    private Integer work_time_minutes;

    // Price charged per minuted of work
    private Double price_per_minute;

    // Duration planned/allocated for the job (minutes)
    private Integer duration;

    // Date and time when the job is scheduled/created
    private LocalDateTime date;

    // Current status of the job (notDelivered, inProgress, finished, etc.)
    // Many jobs can share the same status
    @ManyToOne // Defines many jobs can share one status (relationship mapping)
    @JoinColumn(name = "status_id", nullable = false) // Creates the foreign key column linking to JobStatus (required field)
    private JobStatus status;

    /// Getters and Setters

    // Gets the unique ID of the job.
    /** @return the unique job ID **/
    public Integer getId() {
        return id;
    }

    // Sets the unique ID of the job
    /** @param id the job ID to set **/
    public void setId(Integer id) {
        this.id = id;
    }

    // Gets the title of the job
    /** @return the job title **/
    public String getTitle() {
        return title;
    }

    // Sets the title of the job
    /** @param title the job title to set **/
    public void setTitle(String title) {
        this.title = title;
    }

    // Gets the customer's name
    /** @return the customer name **/
    public String getCustomer_name() {
        return customer_name;
    }

    // Sets the customer's name
    /** @param customer_name **/
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    // Gets the customer's phone number
    /** @return the customer phone number **/
    public String getCustomer_phone() {
        return customer_phone;
    }

    // Sets the customer's phone number.
    /** @param customer_phone the customer phone number to set **/
    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    // Gets the detailed job description
    /** @return the job description **/
    public String getJob_description() {
        return job_description;
    }

    // Sets the detailed job description.
    /** @param job_description the job description to set **/
    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    // Gets the work time in minutes
    /** @return the work time in minutes **/
    public Integer getWork_time_minutes() {
        return work_time_minutes;
    }

    // Sets the work time in minutes
    /** @param work_time_minutes the work time in minutes to set **/
    public void setWork_time_minutes(Integer work_time_minutes) {
        this.work_time_minutes = work_time_minutes;
    }

    // Gets the price charged per minute of work
    /** @return the price per minute **/
    public Double getPrice_per_minute() {
        return price_per_minute;
    }

    // Sets the price charged per minute of work.
    /** @param price_per_minute the price per minute to set **/
    public void setPrice_per_minute(Double price_per_minute) {
        this.price_per_minute = price_per_minute;
    }

    // Gets the duration in minutes
    /** @return the duration of the job in minutes **/
    public Integer getDuration() {
        return duration;
    }

    // Sets the duration in minutes
    /** @param duration the work time in minutes to set **/
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    // Gets the date and time of the job
    /** @return the job data and time **/
    public LocalDateTime getDate() {
        return date;
    }

    // Sets the data and time of the job
    /** @param date the job date and time to set **/
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Gets the current status of the job
    /** @return the job status **/
    public JobStatus getStatus() {
        return status;
    }

    // Sets the current status of the job
    /** @param status the job status to set **/
    public void setStatus(JobStatus status) {
        this.status = status;
    }
}
