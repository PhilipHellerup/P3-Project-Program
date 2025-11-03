package mainProgram.table;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a job in the system.
 *
 * <p>This class models a job record with customer information, work details,
 * pricing, and status. It maps to the "jobs" table in the database and establishes
 * a many-to-one relationship with JobStatus.</p>
 *
 * <p>Jobs track essential information such as:</p>
 * <ul>
 *   <li>Customer contact details (name and phone)</li>
 *   <li>Job description and title</li>
 *   <li>Work time and pricing information</li>
 *   <li>Job creation/scheduling date</li>
 *   <li>Current status (through JobStatus relationship)</li>
 * </ul>
 *
 * @see JobStatus
 */
@Entity
@Table(name = "jobs")
public class Job {

  /**
   * Unique identifier for the job.
   *
   * <p>Auto-generated using database identity strategy (assumes SERIAL or BIGSERIAL
   * primary key in PostgreSQL).</p>
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Brief title or summary of the job.
   */
  private String title;

  /**
   * Name of the customer who requested the job.
   */
  private String customer_name;

  /**
   * Contact phone number for the customer.
   */
  private String customer_phone;

  /**
   * Detailed description of the work to be performed.
   */
  private String job_description;

  /**
   * Total working hours charged for a job, measured in minutes
   */
  private Integer work_time_minutes;

  /**
   * Rate charged per minute of work performed.
   *
   * <p>Total job cost can be calculated as: work_time_minutes × price_per_minute</p>
   */
  private Double price_per_minute;

  /**
   * Total time allocated for a job, measured in minutes.
   */
  private Integer duration;

  /**
   * Date and time when the job was created or scheduled.
   */
  private LocalDateTime date;

  /**
   * Current status of the job (e.g., pending, in progress, completed).
   *
   * <p>This establishes a many-to-one relationship with the JobStatus entity.
   * Multiple jobs can share the same status.</p>
   */
  @ManyToOne
  @JoinColumn(name = "status_id", nullable = false)
  private JobStatus status;

  // Getters and Setters

  /**
   * Gets the unique identifier of the job.
   *
   * @return the job ID
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the job.
   *
   * @param id the job ID to set
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Gets the title of the job.
   *
   * @return the job title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the job.
   *
   * @param title the job title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the customer's name.
   *
   * @return the customer name
   */
  public String getCustomer_name() {
    return customer_name;
  }

  /**
   * Sets the customer's name.
   *
   * @param customer_name the customer name to set
   */
  public void setCustomer_name(String customer_name) {
    this.customer_name = customer_name;
  }

  /**
   * Gets the customer's phone number.
   *
   * @return the customer phone number
   */
  public String getCustomer_phone() {
    return customer_phone;
  }

  /**
   * Sets the customer's phone number.
   *
   * @param customer_phone the customer phone number to set
   */
  public void setCustomer_phone(String customer_phone) {
    this.customer_phone = customer_phone;
  }

  /**
   * Gets the detailed job description.
   *
   * @return the job description
   */
  public String getJob_description() {
    return job_description;
  }

  /**
   * Sets the detailed job description.
   *
   * @param job_description the job description to set
   */
  public void setJob_description(String job_description) {
    this.job_description = job_description;
  }

  /**
   * Gets the work time in minutes.
   *
   * @return the work time in minutes
   */
  public Integer getWork_time_minutes() {
    return work_time_minutes;
  }

  /**
   * Sets the work time in minutes.
   *
   * @param work_time_minutes the work time in minutes to set
   */
  public void setWork_time_minutes(Integer work_time_minutes) {
    this.work_time_minutes = work_time_minutes;
  }

  /**
   * Gets the price charged per minute of work.
   *
   * @return the price per minute
   */
  public Double getPrice_per_minute() {
    return price_per_minute;
  }

  /**
   * Sets the price charged per minute of work.
   *
   * @param price_per_minute the price per minute to set
   */
  public void setPrice_per_minute(Double price_per_minute) {
    this.price_per_minute = price_per_minute;
  }

  /**
   * Gets the duration in minutes.
   *
   * @return the duration of the job in minutes
   */
  public Integer getDuration() {
    return duration;
  }

  /**
   * Sets the duration in minutes.
   *
   * @param duration the work time in minutes to set
   */
  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  /**
   * Gets the date and time of the job.
   *
   * @return the job date and time
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * Sets the date and time of the job.
   *
   * @param date the job date and time to set
   */
  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * Gets the current status of the job.
   *
   * @return the job status
   */
  public JobStatus getStatus() {
    return status;
  }

  /**
   * Sets the current status of the job.
   *
   * @param status the job status to set
   */
  public void setStatus(JobStatus status) {
    this.status = status;
  }
}
