package mainProgram.services; // Project Organization

/* --- Imports --- */
import mainProgram.repository.ServiceRepository;
import mainProgram.table.Services;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/* --- ServiceService Class --- */
// Handles the business logic for service-related operations.
// The controller delegates work, keeping business rules out of the controller layer.
@Service
public class ServiceService implements BaseSearchService<Services> {
    /// Attributes
    // Repository for performing CRUD operations on the Services table
    private final ServiceRepository serviceRepository;

    /// Constructor
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /// Methods

    // Create Service - Create
    /** @param service the Service object to save **/
    /** @return the saved Service **/
    public Services createService(Services service) {
        // Save the new service using the repository
        return serviceRepository.save(service);
    }

    // Read Service by ID - READ
    /** @param id the ID of the service to fetch **/
    /** @return the service if found, otherwise null **/
    public Services getService(int id) {
        // Find the service or return null if not found
        return serviceRepository.findById(id).orElse(null);
    }

    // Update Service by ID - UPDATE
    /** @param id the ID of the service to update **/
    /** @param updates map containing fields and values to update **/
    /** @return updated service or null if not found **/
    public Services updateService(int id, Map<String, Object> updates) {
        // Attempt to find the existing service
        return serviceRepository.findById(id).map(service -> {
            // Apply each field from the `updates` map/list
            updates.forEach((field, value) -> {
                switch (field) {
                    case "name" -> {
                        // Update service name
                        service.setName((String) value);
                    }
                    case "price" -> {
                        // Update price (supports Number or String)
                        if (value instanceof Number num) {
                            service.setPrice(num.doubleValue());
                        }
                        else {
                            service.setPrice(Double.parseDouble(value.toString()));
                        }
                    }
                    case "duration" -> {
                        // Update duration (supports Number or String)
                        if (value instanceof Number num) {
                            service.setDuration(num.intValue());
                        }
                        else {
                            service.setDuration(Integer.parseInt(value.toString()));
                        }
                    }
                    default -> {
                        // Unexpected fields here
                        System.out.println("Unknown field: " + field);
                    }
                }
            });

            // Save the updated service
            return serviceRepository.save(service);

        }).orElse(null); // If the service does not exist
    }

    // Delete Service - DELETE
    /** @param id the ID of the service to delete **/
    /** @return true if deleted, false if not found **/
    public boolean deleteService(int id) {
        // Check if the service exists before deleting
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Search Service (from BaseSearchService) - HELPER METHOD
    /** @param keyword optional search term **/
    /** @return list of services matching keyword or all services if none provided **/
    @Override
    public List<Services> search(String keyword) {
        // Check if the keyword is null or blank
        if (keyword == null || keyword.isBlank()) {
            // If no keyword is provided, return the full list of products
            return serviceRepository.findAll();
        }

        // Search by name (case-insensitive, partial match)
        return serviceRepository.findByNameContainingIgnoreCase(keyword);
    }
}