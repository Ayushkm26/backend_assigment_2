package com.example.backend_spring.repository;

import com.example.backend_spring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {}
// Method	Description
//save(Order order)	Insert or update an order
//findById(Long id)	Fetch an order by its ID
//findAll()	Fetch all orders
//deleteById(Long id)	Delete an order by ID
//count()	Count total orders
//existsById(Long id)	Check if an order exists
//…and more	Pagination, sorting, and custom query support
