package com.microservice.user.persitence.model.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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




}
