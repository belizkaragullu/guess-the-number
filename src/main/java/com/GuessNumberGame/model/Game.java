package com.GuessNumberGame.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name ="game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="playerid")
    private Player player;

    private int randomNumber;
    private int totalAttempts;
    private boolean isGameFinished;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;
    public Game(Long id, int totalAttempts) {
        this.id = id;
        this.totalAttempts=totalAttempts;
    }
}

