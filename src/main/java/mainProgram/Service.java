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
        this.duration = serviceDuration;
    }

    // Getters
    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public int getDuration(){
        return duration;
    }

    // Setters
}
