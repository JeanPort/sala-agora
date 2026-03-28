package com.jean.realmeet.exception;

public class RoomNotFoundException extends ResourceNotFoundException{

    private static final String MSG_NOT_FOUND = "Room not found";

    public RoomNotFoundException() {
        super(MSG_NOT_FOUND);
    }

    public RoomNotFoundException(String message) {
        super(message);
    }
}
