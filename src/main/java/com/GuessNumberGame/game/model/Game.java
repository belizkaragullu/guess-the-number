package com.GuessNumberGame.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name ="game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
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

