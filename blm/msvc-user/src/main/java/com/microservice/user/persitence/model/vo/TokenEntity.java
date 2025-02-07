package com.microservice.user.persitence.model.vo;

import com.microservice.user.persitence.model.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tokens")
@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;




}
