package mainProgram; // Project Organization

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
public class Job {
    // Attributes
    private String id;
    private String name;
    private String customerName;
    private String customerPhone;
    private String bikeDescription;
    private List<Service> services = new ArrayList<>();
    private List<Part> parts = new ArrayList<>();
    private int workTimeMinutes;
    private double pricePerMinute;
    private double cost;
    private int duration;
    private String date;
    private status status;

    // TODO: Create some data validation and cleaning
    // Constructor
    public Job(String id, String name, String customerName, String customerPhone, String bikeDescription, List<Service> services, List<Part> parts, int workTimeMinutes, double pricePerMinute, String date, status status) {
        this.id = id.trim();
        this.name = name.trim();
        this.customerName = customerName.trim();
        this.customerPhone = (customerPhone == null) ? "" : customerPhone.trim(); //Optional
        this.bikeDescription = (bikeDescription == null) ? "" : bikeDescription.trim(); //Optional
        this.services = new ArrayList<>(services); // defensive copy
        this.parts = new ArrayList<>(parts);       // defensive copy
        this.workTimeMinutes = workTimeMinutes;
        this.pricePerMinute = pricePerMinute;
        this.date = date;
        this.status = status;
    }

    // Calculate the cost of a job from the prices of parts and services, and the working time.
    private double calculateCost(){
        double cost = 0;
        for (Part part : parts){
            cost += part.getPrice();
        }
        for (Service service : services){
            cost += service.getPrice();
        }
        cost += workTimeMinutes * pricePerMinute;
        return cost;
    }

    private int calculateDuration(){
        int duration = 0;
        for (Service service : services){
            duration += service.getDuration();
        }

        return duration;
    }

    // Print out information about a job for debugging
    public void printToConsol(){
        System.out.println("Job ID: " + id);
        System.out.println("Job name: " + name);
        System.out.println("Customer name: " + customerName);
        System.out.println("Customer phone: " + customerPhone);
        System.out.println("Bike description: " + bikeDescription);
        System.out.print("Parts: [");
        for (Part part : parts){
            System.out.print(" " + part.getName() + ",");
        }
        System.out.println("]");
        System.out.print("Services: [");
        for (Service service : services){
            System.out.print(" " + service.getName() + ",");
        }
        System.out.println("]");
        System.out.println("Work time minutes: " + workTimeMinutes + " minutes");
        System.out.println("Price per minute: " + pricePerMinute + " kr.");
        System.out.println("Cost: " + getCost() + " kr.");
        System.out.println("Duration: " + getDuration() + " minutes");
        System.out.println("Date: " + date);
        System.out.println("Status: " + status);
    }


    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getBikeDescription() {
        return bikeDescription;
    }

    public List<Service> getServices() {
        return new ArrayList<>(services);
    }

    public List<Part> getParts() {
        return new ArrayList<>(parts);
    }

    public int getWorkTimeMinutes() {
        return workTimeMinutes;
    }

    public double getCost() {
        return calculateCost();
    }

    public int getDuration() {
        return calculateDuration();
    }

    public String getDate() {
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

    public void setId(String id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone){
        this.customerPhone = customerPhone;
    }

    public void setBikeDescription(String bikeDescription){
        this.bikeDescription = bikeDescription;
    }

    public void setServices(List<Service> services){
        this.services = new ArrayList<>(services);
    }

    public void setParts(List<Part> parts){
        this.parts = new ArrayList<>(parts);
    }

    public void setWorkTimeMinutes(int workTimeMinutes){
        this.workTimeMinutes = workTimeMinutes;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public void setDate(String date){
        this.date = date;
    }
}

