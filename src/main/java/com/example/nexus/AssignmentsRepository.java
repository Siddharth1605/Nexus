package com.example.nexus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssignmentsRepository extends JpaRepository<Assignments, UUID> {
    public Order findOrderById(UUID orderid);
}
