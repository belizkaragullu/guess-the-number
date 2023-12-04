package com.GuessNumberGame.service;


import com.GuessNumberGame.exception.PlayerNotFoundException;
import com.GuessNumberGame.model.Game;
import com.GuessNumberGame.model.Player;
import com.GuessNumberGame.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameService gameService; //kullanamadım

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

    public List<Player> getEveryPlayersBestScoreInAscOrder(){

        List<Player> players = playerRepository.findAll();

        players.forEach(player -> {
            List<Game> games = player.getGames();
            games.sort(Comparator.comparingInt(Game::getTotalAttempts));
            if (!games.isEmpty()) {
                Game bestGame = games.get(0);
                List<Game> bestGameList = new ArrayList<>();
                bestGameList.add(new Game(bestGame.getId(), bestGame.getTotalAttempts()));
                player.setGames(bestGameList);
            }
        });

        players.sort(Comparator.comparingInt(player -> player.getGames().get(0).getTotalAttempts()));
        return players;
    }

 // YUKARIDAKİ FONKSİYONU GAME SERVİCEDEN HER PLAYERIN EN BASARILI OLDUGU OYUNU ALIP, PLAYERLARI EN BASARILI OLDUKLARI OYUN İLE KIYASLAMAK İSTEDİM AMA YAZAMADIM
   /* public List<Player> getBestScores() {

        List<Player> players = playerRepository.findAll();



        for (Player player : players) {

            List<Game> minimumAttemptGames = gameService.getMinimumAttemptGameOfAPlayer(player.getId());
            player.setGames(minimumAttemptGames);
        }

        players.sort(Comparator.comparingInt(player -> player.getGames().get(0).getTotalAttempts()));
        return players;
    }

*/
}
