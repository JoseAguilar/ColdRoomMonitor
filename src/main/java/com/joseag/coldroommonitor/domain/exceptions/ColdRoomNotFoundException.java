package com.joseag.coldroommonitor.domain.exceptions;

public class ColdRoomNotFoundException extends RuntimeException{
    public ColdRoomNotFoundException(Long id){
        super("ColdRoom not found with id "+id);
    }
}
