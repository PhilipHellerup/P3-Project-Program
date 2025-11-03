package mainProgram.repository; // Project Organization

/* --- Imports --- */
import mainProgram.table.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/* --- ProductRepository Interface --- */
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
    //#TODO: Write More Comments! (Philip)
}

