package mainProgram; // Project Organization

// Service Class
public class Service {
    // Attributes
    private String name;
    private double price;
    private int duration;

    // Constructor
    public Service(String name, double price, int duration){
        this.name = name;
        this.price = price;
        this.duration = duration;
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
