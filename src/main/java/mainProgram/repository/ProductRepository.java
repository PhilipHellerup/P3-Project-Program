package mainProgram.repository;

import mainProgram.table.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
