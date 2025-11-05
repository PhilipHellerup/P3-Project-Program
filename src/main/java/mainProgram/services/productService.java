package mainProgram.services;

import mainProgram.repository.ProductRepository;
import mainProgram.table.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class productService implements BaseSearchService<Product> {

    private final ProductRepository productRepository;

    public productService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}
