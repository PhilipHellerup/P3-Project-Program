package mainProgram; // Project Organization

// Product Class
public class Product {

  // Attributes
  private int id;
  private double price;

  // Constructor
  public Product(int id, double price) {
    this.id = id;
    this.price = price;
  }

  // Getters
  public int getId() {
    return id;
  }

  public double getPrice() {
    return price;
  }

  // Setters
  public void setId(int id) {
    this.id = id;
  }

  public void setPrice(double price) {
    this.price = price;
  }
}
