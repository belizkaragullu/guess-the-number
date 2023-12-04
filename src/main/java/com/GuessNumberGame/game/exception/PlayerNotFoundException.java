package com.GuessNumberGame.game.exception;

public class PlayerNotFoundException extends RuntimeException{
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
