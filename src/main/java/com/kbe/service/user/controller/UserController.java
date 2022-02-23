package com.kbe.service.user.controller;

import com.kbe.service.user.model.User;
import com.kbe.service.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {
        "*"
},
        allowedHeaders = "*",
        allowCredentials = "false",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.DELETE
        })
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User u = userRepository.findUserByEmail(user.getEmail());
        if (u!=null) {
            return new ResponseEntity<>("user already exists", HttpStatus.BAD_REQUEST);
        } else {
            User savedUser = userRepository.insert(user);
            if (savedUser != null) {
                return new ResponseEntity<>(savedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User u = userRepository.findUserByEmail(email);
        System.out.println(u);
        if (u.equals(null)) {
            return new ResponseEntity<>("No user found", HttpStatus.BAD_REQUEST);
        }
        if (u.getPassword().equals(password)) {
            System.out.println("yes");
            return new ResponseEntity<>(u, HttpStatus.OK);
        } else {
            System.out.println("no");
            return new ResponseEntity<>("wrong password", HttpStatus.BAD_REQUEST);
        }
    }

}
