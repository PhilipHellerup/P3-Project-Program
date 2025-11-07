package mainProgram.table;

import jakarta.persistence.*;

@Entity
@Table(name = "job_services")
public class JobServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private Services service;

    @Column(nullable = false)
    private int quantity;

    // Constructor
    public JobServices(Job job, Services service, Integer quantity) {
        this.job = job;
        this.service = service;
        this.quantity = quantity;
    }

    public JobServices() {

    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int qty) {
        this.quantity += qty;
    }
}
