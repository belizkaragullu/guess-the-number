package com.GuessNumberGame.repository;

import com.GuessNumberGame.model.Game;
import com.GuessNumberGame.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game,Long> {
    List<Game> findByPlayerId(Long playerId);
    List<Game> findByGameStatus(GameStatus gameStatus);

}
