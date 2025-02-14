package com.microservice.user.persitence.model.entities;


import com.microservice.user.persitence.model.enums.Status;

import com.microservice.user.persitence.model.vo.TokenEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false, length = 50)
    private String firstName;

    @Column(nullable = false, unique = true, length = 50)
    private String userId;

    @Column(nullable = false, unique = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = false, length = 50)
    private String userName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = false , length = 50)
    private String phoneNumber;

    @Column(nullable = false, unique = false, length = 50)
    private Status status;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_branches", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "branch_id")
    private Set<String> branchIds = new HashSet<>();

    @Column(name = "account_non_expired")
    private boolean isAccountNonExpired = true;

    @Column(name = "account_non_locked")
    private boolean isAccountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;

    @Column(nullable = true, unique = true, length = 50)
    private String employeeCode;

    private int failedLoginAttempts = 0;
    private boolean accountLocked = false;

    private LocalDateTime lockoutUntil;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TokenEntity> tokens = new ArrayList<>();

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
