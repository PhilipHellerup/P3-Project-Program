package mainProgram.services;

import java.util.List;

import mainProgram.repository.*;
import mainProgram.table.*;
import org.springframework.stereotype.Service;

@Service
public class JobService implements BaseSearchService<Job> {

    private final JobRepository jobRepository;
    private final JobPartRepository jobPartRepository;
    private final JobServiceRepository jobServiceRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;

    public JobService(JobRepository jobRepository, JobPartRepository jobPartRepository, JobServiceRepository jobServiceRepository, ProductRepository productRepository, ServiceRepository serviceRepository) {
        this.jobRepository = jobRepository;
        this.jobPartRepository = jobPartRepository;
        this.jobServiceRepository = jobServiceRepository;
        this.productRepository = productRepository;
        this.serviceRepository = serviceRepository;
    }

    public Job getJobById(int id) {
        return jobRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    /// Find the parts associated with a repair
    public List<JobPart> getPartsForJob(int jobId) {
        return jobPartRepository.findByJobId(jobId);
    }


    /// We dont use this anymore
    public void addProductToRepair(int repairId, int productId, int quantity) {
        // Check if the repair and product exist
        Job repair = getJobById(repairId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the product is already linked to the part, if so adjust only the amount. If not, create a new jobPart
        JobPart existingProduct = jobPartRepository.findByJobId(repairId)
                .stream()
                .filter(jobpart -> jobpart.getProduct().getId() == (productId))
                .findFirst()
                .orElse(null);

        if (existingProduct != null) {
            existingProduct.addQuantity(quantity);
            jobPartRepository.save(existingProduct); // ensure updated quantity persisted
        } else {
            jobPartRepository.save(new JobPart(repair, product, quantity));
        }

    }

    ///  Add a part to a repair
    public void addPartToRepair(int repairId, int partId, int quantity) {
        // Find repair and part
        Job repair = getJobById(repairId);
        Product part = productRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        // Check if the part already exists in this repair
        JobPart existingPart = jobPartRepository.findByJobId(repairId)
                .stream()
                .filter(jp -> jp.getProduct().getId() == partId)
                .findFirst()
                .orElse(null);

        if (existingPart != null) {
            existingPart.addQuantity(quantity);
            jobPartRepository.save(existingPart);
        } else {
            jobPartRepository.save(new JobPart(repair, part, quantity));
        }
    }

    ///  Add service to a repair
    public void addServiceToRepair(int repairId, int serviceId, int quantity) {
        // Find repair and service
        Job repair = getJobById(repairId);
        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        // Check if the service already exists in this repair
        JobServices existingService = jobServiceRepository.findByJobId(repairId)
                .stream()
                .filter(js -> js.getService().getId() == serviceId)
                .findFirst()
                .orElse(null);

        if (existingService != null) {
            existingService.addQuantity(quantity);
            jobServiceRepository.save(existingService);
        } else {
            jobServiceRepository.save(new JobServices(repair, service, quantity));
        }
    }


    /// Custom search function for job/ repair
    @Override
    public List<Job> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return jobRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
