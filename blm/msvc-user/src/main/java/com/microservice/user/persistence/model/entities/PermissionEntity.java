package com.microservice.user.persistence.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@Table(name = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true , updatable = false )
    private String name;

    @Column(nullable = false)
    private boolean active = true;
}
