package com.GuessNumberGame.game.service;


import com.GuessNumberGame.game.exception.PlayerNotFoundException;
import com.GuessNumberGame.game.model.Game;
import com.GuessNumberGame.game.model.Player;
import com.GuessNumberGame.game.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;


    public Player createPlayer(String username){
        Player player = new Player();
        player.setUsername(username);
        return playerRepository.save(player);
    }

    public List<Player> getAllPlayers(){

        return playerRepository.findAll();
    }

    public Player findPlayerById(Long playerId){
        return playerRepository.findById(playerId)
                .orElseThrow(()-> new PlayerNotFoundException("Player not found, please register first."));
    }

}
