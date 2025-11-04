package mainProgram.initializer;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.TreeMap;
import mainProgram.repository.JobStatusRepository;
import mainProgram.table.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Initializes the JobStatus table with default reference data if missing.
 *
 * <p>This component runs automatically at application startup. It ensures
 * the 'job_status' table exists (via Hibernate auto-DDL) and contains all
 * the expected default status records.</p>
 *
 * <p>If the table is empty or certain statuses are missing, they are
 * automatically created. Existing records are left unchanged.</p>
 */
@Component
public class JobStatusInitializer {

    private static final Logger logger = LoggerFactory.getLogger(JobStatusInitializer.class);

    private final JobStatusRepository repository;

    // Define your default statuses here (ID → Name)
    private static final Map<Short, String> DEFAULT_STATUSES = new TreeMap<>(
        Map.of(
            (short) 1,
            "notDelivered",
            (short) 2,
            "delivered",
            (short) 3,
            "inProgress",
            (short) 4,
            "missingPart",
            (short) 5,
            "finished",
            (short) 6,
            "pickedUp"
        )
    );

    public JobStatusInitializer(JobStatusRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initialize() {
        try {
            logger.info("Checking JobStatus table...");

            DEFAULT_STATUSES.forEach((id, name) -> {
                repository
                    .findById(id)
                    .ifPresentOrElse(
                        (existing) -> {
                            if (!existing.getName().equals(name)) {
                                existing.setName(name);
                                repository.save(existing);
                                logger.info("Updated JobStatus ID {} to name '{}'", id, name);
                            }
                        },
                        () -> {
                            JobStatus status = new JobStatus();
                            status.setId(id);
                            status.setName(name);
                            repository.save(status);
                            logger.info("Inserted JobStatus ID {} with name '{}'", id, name);
                        }
                    );
            });

            logger.info("JobStatus initialization complete. Total records: {}", repository.count());
        } catch (Exception e) {
            logger.error("Failed to initialize JobStatus table", e);
        }
    }
}
