package mainProgram.services;

import java.util.List;
import mainProgram.repository.ServiceRepository;
import mainProgram.table.Services;
import org.springframework.stereotype.Service;

@Service
public class ServiceService implements BaseSearchService<Services> {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Services> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return serviceRepository.findByNameContainingIgnoreCase(keyword);
    }
}
