package mainProgram; // Project Organization

// Part Class
public class Part {
    // Attributes
    private String name;
    private String type;
    private double price;

    // Constructor
    public Part(String name, String type, double price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    // Getters
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
