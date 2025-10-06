package mainProgram; // Project Organization

/* --- Libraries --- */
// Provides the main entry point for running Spring Boot programs
import org.springframework.boot.SpringApplication;

// Enables auto-configuration and component scanning
import org.springframework.boot.autoconfigure.SpringBootApplication;


/* --- MainApplication Class --- */
@SpringBootApplication // Marks this as the main Spring Boot application class
public class MainApplication {
    // Main Method: Entry point of the application
    public static void main(String[] args) {
        // Runs the Spring Boot application
        SpringApplication.run(MainApplication.class, args);

        Part[] newparts = new Part[]{new Part(22)};
        Service[] services = new Service[]{new Service("lubrication", 150, 10)};

        Job newjob = new Job("22", "testjob1", "Jonas", "30261919", "cyklen kommer mandag, den skal have ny kassette",
                services, newparts, 20, 17, "2025-04-15", status.inProgress );

        double cost = newjob.calculateCost();

        System.out.println(cost);
        System.out.println(newjob.toString());
    }
}
