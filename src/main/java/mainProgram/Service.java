package mainProgram;


// Service class
public class Service {
    // Attributes
    private String name;
    private double price;
    private int duration;

    // Constructor
    public Service(String name, double price, int serviceDuration){
        this.name = name;
        this.price = price;
    }

    // Getters
    public String getServiceName(){
        return name;
    }

    public double getServicePrice(){
        return price;
    }

    public int getServiceDuration(){
        return duration;
    }

    // Setters
}
