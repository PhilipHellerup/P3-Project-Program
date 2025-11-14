package mainProgram.services; // Project Organization

/* --- Imports --- */
import mainProgram.repository.ServiceRepository;
import mainProgram.table.Services;
import org.springframework.stereotype.Service;
import java.util.List;

//#TODO MAKE COMMENTS!
/* --- ServiceService Class --- */
@Service
public class ServiceService implements BaseSearchService<Services> {
    // Attributes
    private final ServiceRepository serviceRepository;

    // Constructor
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Methods
    @Override
    public List<Services> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            // Return all services if no keyword provided
            return serviceRepository.findAll();
        }

        // Search only by name (case-insensitive, partial match)
        return serviceRepository.findByNameContainingIgnoreCase(keyword);
    }
}
