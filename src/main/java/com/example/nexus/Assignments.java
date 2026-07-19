package com.example.nexus;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assignments")
public class Assignments {

    public enum AssignmentStatus {
        ASSIGNED,
        ACCEPTED,
        REJECTED,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status;

    private Integer attemptNumber;

    private LocalDateTime assignedAt;


}