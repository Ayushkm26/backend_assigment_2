package com.example.backend_spring.controller;

import com.example.backend_spring.model.User;
import com.example.backend_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public User createUser( @Valid @RequestBody User user){

        return userService.createUser(user);

    }

    @GetMapping("/getUsers")
    public List<User> getUsers(){

        return userService.getUsers();

    }
    @GetMapping("/{id}/orders")
    public String getUserOrders(@PathVariable Long id){
        return userService.getUserOrders(id);
    }




}

//
