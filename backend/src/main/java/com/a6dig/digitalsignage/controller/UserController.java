package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.entity.User;
import com.a6dig.digitalsignage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}