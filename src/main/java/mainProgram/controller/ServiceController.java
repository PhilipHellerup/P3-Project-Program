package mainProgram.controller; // Project Organization

/* --- Imports --- */
import mainProgram.repository.ServiceRepository;
import mainProgram.table.Product;
import mainProgram.table.Services;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/* --- ServiceController --- */
// REST controller for handling service-related operations.
// Provides endpoints for creating, updating, deleting, and listing services in the database.
@RestController
@RequestMapping("/api/services") // Base path for all API routes in this controller
public class ServiceController {
    // Attributes
    private final ServiceRepository serviceRepository; // Injected repository for CRUD operations on Services

    // Constructor for Dependency Injection
    // Spring automatically provides an instance of ServiceRepository at runtime.
    /** @param serviceRepository the repository handling CRUD operations for Service entities. **/
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Methods

    // CREATE
    // Handles POST requests to create a new service
    // Endpoint: POST /api/services
    /** @param service the service object to create **/
    /** @return ResponseEntity containing the created service if successful (HTTP 200 OK) **/
    @PostMapping
    public ResponseEntity<Services> createService(@RequestBody Services service) {
        // Save the new service to the database using the repository and its .save() method.
        Services savedService = serviceRepository.save(service);

        // Return HTTP 200 OK with the saved service object as a JSON response
        return ResponseEntity.ok(savedService);
    }

    // Get a single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Services> getProduct(@PathVariable int id) {
      return serviceRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    // Handles DELETE requests to remove a service by ID
    // Endpoint: DELETE /api/services/{id}
    /** @param id the unique id of the service to delete **/
    /** @return ResponseEntity with HTTP 204 No Content if deletion is successful,
     **         or HTTP 404 Not Found if the service does not exist **/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable int id) {
        // Check if the service with the given ID exists
        if (serviceRepository.existsById(id)) {
            // Delete the service from the database
            serviceRepository.deleteById(id);

            // Return HTTP 204: No Content (indicating success, but no response body needed)
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        else {
            // Return HTTP 404: Not Found (if the service does not exist in the database)
            return ResponseEntity.notFound().build(); // 404 if not found
        }
    }

    // UPDATE/EDIT
    // Handles PUT requests to update a service by ID
    // Endpoint: PUT /api/services/{id}
    // The frontend sends a JSON body containing fields to update: name, price, duration
    /** @param id the unique id of the service to update
    * @param updates a Map containing field names as keys and new values as values
    * @return ResponseEntity containing the updated service (HTTP 200 OK) if found and updated,
     **         or HTTP 404 Not Found if the service with the given ID does not exist **/
    @PutMapping("/{id}")
    public ResponseEntity<?> editService(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        // Find the service by ID
        return serviceRepository.findById(id).map(service -> {
            // Iterate over each field in the updates map
            // The map comes from the frontend and contains key/value pairs of fields to update
            updates.forEach((field, value) -> {
                switch (field) {
                    case "name" -> service.setName((String) value);
                    case "price" -> {
                        // Handle price as number or string
                        if (value instanceof Number num) {
                            service.setPrice(num.doubleValue());
                        } else {
                            service.setPrice(Double.parseDouble(value.toString()));
                        }
                    }
                    case "duration" -> {
                        // Handle duration as number or string
                        if (value instanceof Number num) {
                            service.setDuration(num.intValue());
                        } else {
                            service.setDuration(Integer.parseInt(value.toString()));
                        }
                    }
                    default -> System.out.println("Unknown field: " + field); // Safety fallback for unexpected fields
                }
            });

            // Save the updated service in the database
            Services saved = serviceRepository.save(service);

            // Return HTTP 200 OK because the service updated successfully
            return ResponseEntity.ok(saved);
        })
        // Service with the given ID not found
        .orElseGet(() -> {
            // Return HTTP 404: Not Found = Service Not Found
            return ResponseEntity.notFound().build();
        });
    }
}
