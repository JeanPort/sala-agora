package com.jean.realmeet.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@Table(name = "tb_room")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @Column(name = "seats", nullable = false)
    private Integer seats;
    @Column(name = "active", nullable = false)
    private Boolean active;

    @PrePersist
    private void prePersist(){
        if (Objects.isNull(active)){
            active = true;
        }
    }
}
