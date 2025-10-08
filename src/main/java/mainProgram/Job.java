package mainProgram;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String customer_name;
    private String customer_phone;
    private String job_description;
    private Integer work_time_minutes;
    private Double price_per_minute;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private JobStatus status;

    // getters & setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCustomer_name() { return customer_name; }
    public void setCustomer_name(String customer_name) { this.customer_name = customer_name; }

    public String getCustomer_phone() { return customer_phone; }
    public void setCustomer_phone(String customer_phone) { this.customer_phone = customer_phone; }

    public String getJob_description() { return job_description; }
    public void setJob_description(String job_description) { this.job_description = job_description; }

    public Integer getWork_time_minutes() { return work_time_minutes; }
    public void setWork_time_minutes(Integer work_time_minutes) { this.work_time_minutes = work_time_minutes; }

    public Double getPrice_per_minute() { return price_per_minute; }
    public void setPrice_per_minute(Double price_per_minute) { this.price_per_minute = price_per_minute; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }
}
