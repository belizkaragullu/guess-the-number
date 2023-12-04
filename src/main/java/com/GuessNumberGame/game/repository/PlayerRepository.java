package com.GuessNumberGame.game.repository;

import com.GuessNumberGame.game.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
   // Player findById(long id);
   // Player findByUsername(String username);
   // Player findAllByOrderByGuessAsc();

}
