package com.service.branch.persistence.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String website;

    @Column(length = 500)
    private String logoUrl;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Branch> branches = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "enterprise_employees", joinColumns = @JoinColumn(name = "enterprise_id"))
    @Column(name = "employee_id")
    private Set<Long> employeesId = new HashSet<>();

}
