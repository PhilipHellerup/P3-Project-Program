package mainProgram;

import mainProgram.database.User;
import mainProgram.database.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // List all users (used by your Index page)
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "index"; // resolves to src/main/resources/templates/index.html
    }

    // Show edit form for a single user
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        model.addAttribute("user", user);
        return "users/edit"; // templates/users/edit.html
    }

    // Process the update
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User formUser,
                             RedirectAttributes redirectAttributes) {
        // Defensive: ensure path id and form id match (form may or may not include id)
        User user = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));

        // Update only the fields you want editable
        user.setEmail(formUser.getEmail());
        user.setDisplay_name(formUser.getDisplay_name());

        userRepo.save(user);

        // Optionally show a flash message
        redirectAttributes.addFlashAttribute("message", "User updated successfully.");
        return "redirect:/";
    }
}
