package com.GuessNumberGame.game.controller;

import com.GuessNumberGame.game.model.Player;
import com.GuessNumberGame.game.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/create/{username}")
    public ResponseEntity<Player> createPlayer(@PathVariable String username){
        Player newPlayer = playerService.createPlayer(username);
        return ResponseEntity.ok(newPlayer);

    }
    @GetMapping("/get")
    public ResponseEntity<List<Player>> getAllPlayers(){
        List<Player> playerList = playerService.getAllPlayers();
        return ResponseEntity.ok(playerList);
    }
    @GetMapping("/get/{playerId}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long playerId){
        Player player = playerService.findPlayerById(playerId);
        return ResponseEntity.ok(player);
    }

}
