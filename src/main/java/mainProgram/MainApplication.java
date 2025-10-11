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
  }
}
