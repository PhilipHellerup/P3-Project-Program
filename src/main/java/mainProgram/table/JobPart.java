package mainProgram.table; // Project Organization

/* --- Imports --- */
import jakarta.persistence.*;

/* --- JobPart Class --- */
// Junction/join table entity that links Jobs with Products.
// Represents a many-to-many relationship: A Job can have many Products,
// and a Product can be used in many Jobs.
// Also stores the quantity of each product used in a specific job.
@Entity
@Table(name = "job_part_jointable")
public class JobPart {

  // Attributes
  // Unique identifier for this job-product association
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  // Reference to the Job that uses this product.
  // Many JobParts can belong to one Job.
  @ManyToOne
  @JoinColumn(name = "job_id")
  private Job job;

  // Reference to the Product being used in the job.
  // Many JobParts can reference the same Product.
  @ManyToOne
  @JoinColumn(name = "part_id")
  private Product product;

  // Number of units of this product used in the job
  private Integer quantity;

  // Methods
  // Getters
  public Product getPart() {
    return product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  // Setters
  public void setPart() {
    this.product = product;
  }

  public void setQuantity() {
    this.quantity = quantity;
  }
}
