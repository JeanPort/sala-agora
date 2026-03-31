package com.jean.realmeet.controller;

import com.jean.realmeet.dto.CreateRoomRequest;
import com.jean.realmeet.dto.RoomResponse;
import com.jean.realmeet.service.RoomService;
import com.jean.realmeet.util.ResponseEntityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;


    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable Long roomId){
        return ResponseEntityUtils.ok(roomService.getRoom(roomId));
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid CreateRoomRequest request){
        var roomCreated = roomService.create(request);
        return ResponseEntityUtils.created(roomCreated, roomCreated.id());
    }
}
