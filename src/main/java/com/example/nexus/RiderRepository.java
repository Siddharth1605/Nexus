package com.example.nexus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RiderRepository extends JpaRepository<Rider, UUID> {
}
