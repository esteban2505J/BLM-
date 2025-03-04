package com.service.branch.persistence.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Getter
@Setter
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    @ElementCollection
    @CollectionTable(name = "branch_employees", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "employee_id")
    private Set<Long> employeeIds = new HashSet<>();

    @Column(length = 255)
    private String address;


    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private Double budget;

    @Column(nullable = false)
    private Double revenue;

    @Column(nullable = false)
    private Long managerId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
