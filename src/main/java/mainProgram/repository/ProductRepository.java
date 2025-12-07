package mainProgram.repository; // Project Organization

/* --- Imports --- */
import mainProgram.table.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/* --- ProductRepository Interface --- */
// Repository interface for CRUD operations on the Product (Parts) table.
// Spring Data JPA automatically generates the implementation at runtime.
@Repository // Marks this interface as a Spring-managed repository component (Not needed as it extends JpaRepository, but added for clarity)
public interface ProductRepository extends JpaRepository<Product, Integer> {
    /// Methods

    /* --- Advanced Multi-Field Search --- */
    // Searches across multiple fields: id, productNumber (Vare nr.), name (Navn), EAN, and type.
    // Case-insensitive partial matches.
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
    // Explanation of the Main SQL Components:
    // - CAST(p.id AS string):
    //   Converts numerical product ID into a string so it can be searched using text matching.
    //
    // - LIKE CONCAT(`%`, :kw, `%`):
    //   Performs a "contains" search.
    //   `%` means wildcard -> Matches any text before or after the keyword.
    //   Example: `%12%` will match `01234`, `312`, `12ABC`, etc.
    //
    // - LOWER(value):
    //   Converts text to lowercase, ensuring case-insensitive comparison.
    //   LOWER(field) LIKE LOWER(keyword) makes searching consistent even if data contains mixed capitalization
    //
    // - OR:
    //   Combines multiple fields checks. A match in ANY field returns the product
    //
    // - ORDER BY p.name ASC:
    //   Sorts the results alphabetically by product name

    /** @param keyword the search term (matches multiple fields) **/
    /** @return list of products matching the keyword search **/
    List<Product> search(@Param("kw") String keyword);
}
