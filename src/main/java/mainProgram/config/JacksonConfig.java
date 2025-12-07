package mainProgram.config; // Project Organization

/* --- Imports --- */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/* --- JacksonConfig Class --- */
// Spring configuration class used to customize how JSON is handled in the application.
// This setup ensures consistent serialization/deserialization of Java objects,
// especially date and time values used throughout the system.
// Note:
// Serialization = Converting Object into Byte Stream
// Deserialization = Converting Byte Stream into Object
@Configuration // Marks this class as a Spring configuration source for application-level beans
public class JacksonConfig {
    /// Methods

    // Create ObjectMapper Bean
    // Creates and configures the ObjectMapper used for processing JSON.
    // Configuration Includes:
    // - Support for Java 8+ date/time classes (LocalDate, LocalDateTime, etc.)
    // - Disabling timestamps so dates use the readable ISO-8601 format instead of numeric timestamps
    /** @return a customized ObjectMapper used globally by Spring for converting Java objects to/from JSON **/
    @Bean // Tells Spring to manage and inject this method’s return value as a bean
    public ObjectMapper objectMapper() {
        // Create a new ObjectMapper instane
        ObjectMapper mapper = new ObjectMapper();

        // Add support for the Java Time API
        // Without this, Spring cannot serialize LocalDate/LocalDateTime correctly
        mapper.registerModule(new JavaTimeModule());

        // Disable timestamp serialization (e.g. 1698852910000)
        // Enables standard ISO-8601 strings instead: "2025-12-07T14:23:00"
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Return the configured ObjectMapper to the Spring context
        return mapper;
    }
}
