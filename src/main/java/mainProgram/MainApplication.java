package mainProgram; // Project Organization

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<Part> newparts = new ArrayList<>(List.of(new Part("SCO Chain", "Chain", 20.48)));
        List<Service> services = new ArrayList<>(List.of(new Service("lubrication", 150, 10), new Service("change of chain", 250, 20)));

        Job newjob = new Job("22", "testjob1", "Jonas", "30261919", "cyklen kommer mandag, den skal have ny kassette",
                services, newparts, 20, 17, "2025-04-15", status.inProgress );

        double cost = newjob.getCost();
        System.out.println(cost);

        newjob.printToConsol();

        newjob.setStatus(status.finished);
    }
}
