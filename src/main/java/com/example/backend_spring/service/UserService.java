package com.example.backend_spring.service;

import com.example.backend_spring.model.User;
import com.example.backend_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {

        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }

        try {
            return userRepository.save(user);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Database error while saving user");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    public List<User> getUsers() {

        try {
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                throw new RuntimeException("No users found");
            }

            return users;

        } catch (DataAccessException ex) {
            throw new RuntimeException("Database error while fetching users");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred");
        }
    }
    private final RestTemplate restTemplate = new RestTemplate();

    public String getUserOrders(Long userId) {

        String url = "http://localhost:8080/api/orders/getOrdersById/" + userId;

        try {
            String response = restTemplate.getForObject(url, String.class);

            // Handle empty or null response
            if (response == null || response.isEmpty()) {
                return "No orders found for user with id: " + userId;
            }

            return response;

        } catch (HttpClientErrorException.NotFound ex) {
            return "No orders found for user with id: " + userId;

        } catch (Exception ex) {
            return "Error fetching orders";
        }
    }
}

