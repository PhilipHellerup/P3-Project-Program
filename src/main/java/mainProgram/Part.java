package mainProgram; // Project Organization

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Part Class
@Entity
@Table(name = "parts")
public class Part {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // assumes serial/bigserial PK
  // Attributes
  private Integer id;

  private String title;
  private String type;
  private Double price;

  @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<JobPart> jobParts = new ArrayList<>();

  // Constructor
  public Part(String title, String type, Double price) {
    this.title = title;
    this.type = type;
    this.price = price;
  }

  public Part() {}

  // Getters
  public String getTitle() {
    return title;
  }

  public String getType() {
    return type;
  }

  public double getPrice() {
    return price;
  }

  // Setters
  public void setName(String name) {
    this.title = title;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setPrice(Double price) {
    this.price = price;
  }
}
