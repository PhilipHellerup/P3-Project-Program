package mainProgram; // Project Organization

// Job Status
enum status{
    notDelivered,
    delivered,
    inProgress,
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
    private Service [] services;
    private Part [] parts;
    private int workTimeMinutes;
    private int pricePerMinute;
    private double cost;
    private int duration;
    private String date;
    private status status;

    // TODO: Create some data validation and cleaning
    // Constructor
    public Job(String id, String name, String customerName, String customerPhone, String bikeDescription,
               Service[] services, Part[] parts, int workTimeMinutes, double cost, int duration, String date) {
        this.id = id;
        this.name = name;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.bikeDescription = bikeDescription;
        this.services = services;
        this.parts = parts;
        this.workTimeMinutes = workTimeMinutes;
        this.cost = cost;
    }

    // Calculate the cost of a job from the prices of parts and services, and the working time.
    public double calculateCost(){
        double cost = 0;

        for (Part part : parts){
            cost += part.price;
        }

        for (Service service : services){
            cost += service.price;
        }

        cost += workTimeMinutes * pricePerMinute;

        return cost;
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

    public Service[] getServices() {
        return services;
    }

    public Part[] getParts() {
        return parts;
    }

    public int getWorkTimeMinutes() {
        return workTimeMinutes;
    }

    public double getCost() {
        return cost;
    }

    public int getDuration() {
        return duration;
    }

    public String getDate() {
        return date;
    }

    public status getStatus() {
        return status;
    }

    // Setters

}


