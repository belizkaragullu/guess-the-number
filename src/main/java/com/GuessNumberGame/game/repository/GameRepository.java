package com.GuessNumberGame.game.repository;

import com.GuessNumberGame.game.model.Game;
import com.GuessNumberGame.game.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game,Long> {
    List<Game> findByPlayerId(Long playerId);
    List<Game> findByGameStatus(GameStatus gameStatus);

}
