package com.jean.realmeet.domain.repository;

import com.jean.realmeet.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
