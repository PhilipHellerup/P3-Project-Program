package mainProgram.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for customizing Jackson JSON serialization/deserialization behavior.
 * This configuration ensures consistent handling of Java 8+ date/time types across the application.
 */
@Configuration
public class JacksonConfig {

  /**
   * Creates and configures a custom ObjectMapper bean for JSON processing.
   *
   * <p>Configuration includes:
   * <ul>
   *   <li>JavaTimeModule registration for Java 8+ date/time API support (LocalDate, LocalDateTime, etc.)</li>
   *   <li>ISO-8601 date format instead of numeric timestamps</li>
   * </ul>
   *
   * @return configured ObjectMapper instance for use throughout the application
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    // Register JavaTimeModule to enable serialization/deserialization of Java 8+ date/time types
    mapper.registerModule(new JavaTimeModule());
    // Don't write dates as timestamps
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
