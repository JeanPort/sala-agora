package com.jean.realmeet.domain.repository;

import com.jean.realmeet.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndActive(Long id, Boolean active);


}
