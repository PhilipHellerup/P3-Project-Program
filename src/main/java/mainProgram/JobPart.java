package mainProgram;

import jakarta.persistence.*;

@Entity
@Table(name = "job_part_jointable")
public class JobPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    private Integer quantity;

    // getters/setters
    public Part getPart() {
        return part;
    }

    public Integer getQuantity() {
        return quantity;
    }
}