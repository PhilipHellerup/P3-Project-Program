package mainProgram.table;

import jakarta.persistence.*;

@Entity
@Table(name = "job_status")
public class JobStatus {

  @Id
  private Short id;

  @Column(nullable = false, unique = true)
  private String name;

  // getters & setters
  public Short getId() {
    return id;
  }

  public void setId(Short id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
