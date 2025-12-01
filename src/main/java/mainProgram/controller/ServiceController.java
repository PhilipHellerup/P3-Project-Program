package mainProgram.controller; // Project Organization

/* --- Imports --- */
import mainProgram.services.ServiceService;
import mainProgram.table.Services;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/* --- ServiceController --- */
// REST controller for handling service-related operations.
// Provides endpoints for creating, updating, deleting, and viewing services.
@RestController
@RequestMapping("/api/services") // Base path for all API routes in this controller
public class ServiceController {
    /// Attributes
    // The Service Layer containing the business logic for Service entity operations
    private final ServiceService serviceService;

    /// Constructor
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /// Methods

    // Create Service - CREATE
    /** @param service the service object sent from frontend to create **/
    /** @return ResponseEntity containing the created service **/
    @PostMapping
    public ResponseEntity<Services> createService(@RequestBody Services service) {
        // Call the createService method from the service layer
        Services saved = serviceService.createService(service);

        // Return HTTP 200 OK with the created service object as JSON
        return ResponseEntity.ok(saved);
    }

    // Read Service by ID - READ
    /** @param id the unique ID of the service to fetch **/
    /** @return ResponseEntity with HTTP 200 OK if found, or 404 Not Found **/
    @GetMapping("/{id}")
    public ResponseEntity<Services> getService(@PathVariable int id) {
        // Call the service layer to fetch the service by ID
        Services service = serviceService.getService(id);

        // Return 200 OK if found, otherwise 404 Not Found
        return (service != null) ? ResponseEntity.ok(service) : ResponseEntity.notFound().build();
    }

    // Update Service - UPDATE
    // Frontend sends a JSON map of fields to update (name, price, duration, etc.)
    /** @param id the unique ID of the service to update **/
    /** @param updates a map containing fields and values to update **/
    /** @return ResponseEntity with updated service or 404 if not found **/
    @PutMapping("/{id}")
    public ResponseEntity<?> editService(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        // Call the updateService method from the service layer
        Services updated = serviceService.updateService(id, updates);

        // Return HTTP 200 OK if the update succeeded, otherwise 404 Not Found
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Delete Service - DELETE
    /** @param id the unique ID of the service to delete **/
    /** @return HTTP 204 No Content if deleted, or 404 Not Found **/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable int id) {
        // Call the service layer to delete the service by ID
        boolean deleted = serviceService.deleteService(id);

        // Return HTTP 204 if the service was deleted, otherwise 404 Not Found
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
