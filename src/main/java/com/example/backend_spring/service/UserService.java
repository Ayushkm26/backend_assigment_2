package com.example.backend_spring.service;

import com.example.backend_spring.model.User;
import com.example.backend_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){

        return userRepository.save(user);

    }

    public List<User> getUsers(){

        return userRepository.findAll();

    }
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUserOrders(Long userId){
        // Correct port and add the missing slash


        String url = "http://localhost:8080/api/orders/getOrdersById/" +userId;
        return restTemplate.getForObject(url, String.class);
    }
}

