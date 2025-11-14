package mainProgram.repository;

import mainProgram.table.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Search by name (partial)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    /**
     * Searches across multiple fields: id, productNumber (Varenr), name (Navn), EAN, and type.
     * Case-insensitive partial matches.
     */
    @Query(
        """
        SELECT p FROM Product p
        WHERE CAST(p.id AS string) LIKE CONCAT('%', :kw, '%')
           OR LOWER(p.productNumber) LIKE LOWER(CONCAT('%', :kw, '%'))
           OR LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%'))
           OR LOWER(p.EAN) LIKE LOWER(CONCAT('%', :kw, '%'))
           OR LOWER(p.type) LIKE LOWER(CONCAT('%', :kw, '%'))
        ORDER BY p.name ASC
        """
    )
    List<Product> search(@Param("kw") String keyword);
}
