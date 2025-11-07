package mainProgram.table;

/* --- Imports --- */

import jakarta.persistence.*;

/**
 * Join table entity representing the association between Jobs and Products.
 *
 * <p>Each record indicates a Product used in a Job, including the quantity.</p>
 */
@Entity
@Table(name = "job_part_jointable")
public class JobPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Reference to the Job
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // Reference to the Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Constructor
    public JobPart(Job job, Product product, Integer quantity) {
        this.job = job;
        this.product = product;
        this.quantity = quantity;
    }

    // Number of units of this product used in the job
    private Integer quantity;

    public JobPart() {
    }

    // Method to increase the quantity
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    // Setters
    public void setJob(Job job) {
        this.job = job;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
