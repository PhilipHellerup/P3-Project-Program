package mainProgram;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data auto-provides findAll(), findById(), save(), delete(), etc.
}
