package mainProgram; // Project Organization
import jakarta.persistence.*;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// Job Status (Enum)

enum status{
    notDelivered,
    delivered,
    inProgress,
    missingPart,
    finished,
    pickedUp
}

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
    public void printToConsol(){
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


    // Getters
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public String getJob_description() {
        return job_description;
    }

//    public Service[] getServices() {
//        return services;
//    }
//
//    public Part[] getParts() {
//        return parts;
//    }

    public int getWorkTimeMinutes() {
        return work_time_minutes;
    }

//    public double getCost() {
//        return calculateCost();
//    }
//
//    public int getDuration() {
//        return calculateDuration();
//    }

    public LocalDateTime getDate() {
        return date;
    }

    public status getStatus() {
        return status;
    }

    // Setters

    public void setStatus(status status){
        if(status == status.finished){
            System.out.println("Job has been finished, do you want to notify the costumer?");
        } else {
        this.status = status;
        }
    }

    public void setId(Integer id){
        this.id = id;
    }

    public void setName(String name){
        this.title = name;
    }

    public void setCustomerName(String customerName){
        this.customer_name = customerName;
    }

    public void setCustomer_phone(String customer_phone){
        this.customer_phone = customer_phone;
    }

    public void setBikeDescription(String bikeDescription){
        this.job_description = bikeDescription;
    }


    public void setWorkTimeMinutes(int workTimeMinutes){
        this.work_time_minutes = workTimeMinutes;
    }


    public void setDate(LocalDateTime date){
        this.date = date;
    }
}

