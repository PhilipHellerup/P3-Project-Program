package mainProgram.initializer; // Project Organization

/* --- Imports --- */
import jakarta.annotation.PostConstruct;
import mainProgram.repository.JobStatusRepository;
import mainProgram.table.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.TreeMap;

/* --- JobStatusInitializer Class --- */
// Initializes the JobStatus table with default reference data if missing
// Component responsible for inserting or updating the default job statuses
// in the database when the application starts.
// Ensures that the 'job_status' table exists (via Hibernate auto-DDL) and always contains the required reference values.
@Component // Makes this class a Spring-managed bean auto-detected at startup
public class JobStatusInitializer {
    /// Attributes
    // Logger for printing information and error messages during initialization
    private static final Logger logger = LoggerFactory.getLogger(JobStatusInitializer.class);

    // Repository used to read/write JobStatus records in the database
    private final JobStatusRepository repository;

    // Default job statuses (ID -> Name) that should always exist in the database
    private static final Map<Short, String> DEFAULT_STATUSES = new TreeMap<>(Map.of(
            (short) 1, "notDelivered",
            (short) 2, "delivered",
            (short) 3, "inProgress",
            (short) 4, "missingPart",
            (short) 5, "finished",
            (short) 6, "pickedUp"
    ));

    /// Constructor
    // Spring injects the JobStatusRepository here
    public JobStatusInitializer(JobStatusRepository repository) {
        this.repository = repository;
    }

    /// Methods
    // Initialization Method
    // Runs automatically after Spring has constructed the bean.
    // This method checks if each default job status exists:
    // - If the status exists but the name is incorrect -> Update it
    // - If the status does not exist -> Insert it
    // Ensures the reference table always stays correct.
    @PostConstruct // Tells Spring to run this method AFTER dependency injection is complete
    public void initialize() {
        try {
            // Logging Info
            logger.info("Checking JobStatus table...");

            // Loop through each default ID -> name pair
            DEFAULT_STATUSES.forEach((id, name) -> {
                // Try to find the JobStatus with the given ID
                repository.findById(id).ifPresentOrElse(
                        // Case 1: Status exists -> Update name if needed
                        existing -> {
                            if (!existing.getName().equals(name)) {
                                existing.setName(name);    // Update name
                                repository.save(existing); // Save changes
                                logger.info("Updated JobStatus ID {} to name '{}'", id, name); // Log Update
                            }
                        },
                        // Case 2: Status does not exist -> Create a new one
                        () -> {
                            JobStatus status = new JobStatus(); // Create new status
                            status.setId(id);        // Set ID
                            status.setName(name);    // Set name
                            repository.save(status); // Insert into database
                            logger.info("Inserted JobStatus ID {} with name '{}'", id, name); // Log Creation
                        }
                );
            });

            // Final Log message confirming that initialization has finished
            // Prints the total number of JobStatus records currently stored in the database
            logger.info("JobStatus initialization complete. Total records: {}", repository.count());
        }
        // Catch any unexpected error
        catch (Exception e) {
            // Any unexpected error during initialization is Logged
            logger.error("Failed to initialize JobStatus table", e);
        }
    }
}
