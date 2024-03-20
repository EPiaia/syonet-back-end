package piaia.eduardo.syonet.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import piaia.eduardo.syonet.model.User;
import piaia.eduardo.syonet.record.LoginRequest;
import piaia.eduardo.syonet.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> login(@RequestBody @Valid LoginRequest loginRequest) {
        User user = userRepository.findUserByUsername(loginRequest.username());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(loginRequest.password())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

}
