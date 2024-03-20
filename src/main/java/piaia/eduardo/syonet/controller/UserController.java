package piaia.eduardo.syonet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import piaia.eduardo.syonet.model.User;
import piaia.eduardo.syonet.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> save(@RequestBody @Valid User user) {
        User userWithUsername = userRepository.findUserByUsername(user.getUsername());
        if (userWithUsername != null) {
            return ResponseEntity.badRequest().build();
        }

        User persistedUser = userRepository.save(user);
        return ResponseEntity.ok(persistedUser);
    }

}
