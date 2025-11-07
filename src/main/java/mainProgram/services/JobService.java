package mainProgram.services;

import java.util.List;
import mainProgram.repository.JobPartRepository;
import mainProgram.repository.JobRepository;
import mainProgram.repository.ProductRepository;
import mainProgram.table.Job;
import mainProgram.table.JobPart;
import mainProgram.table.Product;
import org.springframework.stereotype.Service;

@Service
public class JobService implements BaseSearchService<Job> {

    private final JobRepository jobRepository;
    private final JobPartRepository jobPartRepository;
    private final ProductRepository productRepository;

    public JobService(
        JobRepository jobRepository,
        JobPartRepository jobPartRepository,
        ProductRepository productRepository
    ) {
        this.jobRepository = jobRepository;
        this.jobPartRepository = jobPartRepository;
        this.productRepository = productRepository;
    }

    public Job getJobById(int id) {
        return jobRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    /// Find the parts associated with a repair
    public List<JobPart> getPartsForJob(int jobId) {
        return jobPartRepository.findByJobId(jobId);
    }

    /// Add a new product to a repair, using the JobPart join-table
    public void addProductToRepair(int repairId, int productId, int quantity) {
        // Check if the repair and product exist
        Job repair = getJobById(repairId);
        Product product = productRepository
            .findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the product is already linked to the part, if so adjust only the amount. If not, create a new jobPart
        JobPart existingProduct = jobPartRepository
            .findByJobId(repairId)
            .stream()
            .filter((jobpart) -> jobpart.getProduct().getId() == (productId))
            .findFirst()
            .orElse(null);

        if (existingProduct != null) {
            existingProduct.addQuantity(quantity);
            jobPartRepository.save(existingProduct); // ensure updated quantity persisted
        } else {
            jobPartRepository.save(new JobPart(repair, product, quantity));
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
