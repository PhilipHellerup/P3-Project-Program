package mainProgram; // Project Organization

import jakarta.persistence.*;

@Entity
@Table(name = "users")  // must match your DB table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // assumes serial/bigserial PK
    private Long id;

    private String email;
    private String display_name;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplay_name() { return display_name; }
    public String setDisplay_name(String display_name) { this.display_name = display_name; return this.display_name; }
}
