package com.project1.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project1.orderservice.model.Order;

public interface Orderrepository extends JpaRepository<Order, Long> {
}
