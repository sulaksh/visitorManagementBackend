package com.example.minor_project.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
