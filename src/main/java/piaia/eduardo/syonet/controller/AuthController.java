package piaia.eduardo.syonet.controller;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @CrossOrigin
    @PostMapping
    public ResponseEntity<User> login(@RequestBody @Valid LoginRequest loginRequest) {
        User user = userRepository.findUserByUsername(loginRequest.username());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(loginRequest.password()));
            String password = String.format("%032x", new BigInteger(1, md5.digest()));
            
            if (!user.getPassword().equals(password)) {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        user.setPassword(null);

        return ResponseEntity.ok(user);
    }

}
