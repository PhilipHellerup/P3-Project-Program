package mainProgram;

import org.springframework.web.bind.annotation.*;
import java.util.List;

// This file is optional and is purely if you want to access the table HTTP endpoint

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
