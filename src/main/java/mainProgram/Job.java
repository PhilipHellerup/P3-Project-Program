package mainProgram; // Project Organization
import jakarta.persistence.*;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// Job Status (Enum)

/* enum status{
    notDelivered,
    delivered,
    inProgress,
    missingPart,
    finished,
    pickedUp
} */

// Job Class
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // assumes serial/bigserial PK
    // Attributes
    private Integer id;
    private String title;
    private String customer_name;
    private String customer_phone;
    private String job_description;
    private Integer work_time_minutes;
    private Double price_per_minute;
    private LocalDateTime date;

    /*
    private status status;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobPart> jobParts = new ArrayList<>();

    // TODO: Create some data validation and cleaning
    // Constructor
    public Job(Integer id, String name, String customerName, String customerPhone, String bikeDescription, int workTimeMinutes,
               double pricePerMinute, LocalDateTime date, status status) {
        this.id = id;
        this.title = name.trim();
        this.customer_name = customerName.trim();
        this.customer_phone = (customerPhone == null) ? "" : customerPhone.trim(); //Optional
        this.job_description = (bikeDescription == null) ? "" : bikeDescription.trim(); //Optional
        this.work_time_minutes = workTimeMinutes;
        this.price_per_minute = pricePerMinute;
        this.date = date;
        this.status = status;
    }

    public Job() {

    }


    // Calculate the cost of a job from the prices of parts and services, and the working time.
//    private double calculateCost(){
//        double cost = 0;
//        for (Part part : parts){
//            cost += part.getPrice();
//        }
//        for (Service service : services){
//            cost += service.getPrice();
//        }
//        cost += workTimeMinutes * pricePerMinute;
//        return cost;
//    }

//    private int calculateDuration(){
//        int duration = 0;
//        for (Service service : services){
//            duration += service.getDuration();
//        }
//
//        return duration;
//    }

    // Print out information about a job for debugging
    public void printToConsole(){
        System.out.println("Job ID: " + id);
        System.out.println("Job name: " + title);
        System.out.println("Customer name: " + customer_name);
        System.out.println("Customer phone: " + customer_phone);
        System.out.println("Bike description: " + job_description);
        System.out.print("Parts: [");
//        for (Part part : parts){
//            System.out.print(" " + part.getName() + ",");
//        }
//        System.out.println("]");
//        System.out.print("Services: [");
//        for (Service service : services){
//            System.out.print(" " + service.getName() + ",");
//        }
        System.out.println("]");
        System.out.println("Work time minutes: " + work_time_minutes + " minutes");
        System.out.println("Price per minute: " + price_per_minute + " kr.");
//        System.out.println("Cost: " + getCost() + " kr.");
//        System.out.println("Duration: " + getDuration() + " minutes");
        System.out.println("Date: " + date);
        System.out.println("Status: " + status);
    }
*/
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

