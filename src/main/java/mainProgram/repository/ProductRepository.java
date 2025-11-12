package mainProgram.repository;

import mainProgram.table.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Search by name (partial)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
