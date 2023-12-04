package com.GuessNumberGame.game.service;

import com.GuessNumberGame.game.exception.GameNotFoundException;
import com.GuessNumberGame.game.model.Game;
import com.GuessNumberGame.game.model.GameStatus;
import com.GuessNumberGame.game.model.Player;
import com.GuessNumberGame.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;

    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }
    public Game getGameById(Long gameId){
        return gameRepository.findById(gameId)
                .orElseThrow(()-> new GameNotFoundException("Game " + gameId + " can not found." ));
    }
    public List<Game> getGamesByStatus(GameStatus gameStatus) {
        return gameRepository.findByGameStatus(gameStatus);
    }

    //KULLANICI IDSİNİ GİRİNCE OYUN OLUSTURULDU AMA USERNAME MI GIRMELIYDI (USERNAME UNIQ DEGIL)
    public Long createGame(Long playerId){
        Player player = playerService.findPlayerById(playerId); // new player mi yapmalıydım

        Random random = new Random();
        int randomNumber =random.nextInt(5); //sımdılık
        Game game = new Game();
        game.setRandomNumber(randomNumber);
        game.setGameFinished(false);
        game.setTotalAttempts(0);
        game.setPlayer(player);
        game.setGameStatus(GameStatus.IN_PROGRESS);

        game = gameRepository.save(game); //SAVE YAPTIM AMA OYUN IDSINI DONDURUYORUM CONTROLLERDA DA BASIYORUM OYUNCU ID YI GIRIP OYNAYABILSIN DIYE

        if(game != null){
            return game.getId();
        }
        else {
            return null;
        }

    }

    //OYUN MANTIGI BURADA ISLENIYIR GAME ID VE GUESS VERILIYOR
    public String makeGuess(Long gameId, int guess){
        Game game =gameRepository.findById(gameId)
                .orElseThrow(()-> new GameNotFoundException("Game " + gameId + " can not found." ));

        int randomnumber = game.getRandomNumber();

        if(!game.isGameFinished()) {

            game.setTotalAttempts(game.getTotalAttempts() + 1);

            if (guess == randomnumber) {
                game.setGameFinished(true);
                game.setGameStatus(GameStatus.SUCCESSFUL);
                return "Congratulations you have guessed the number in " + game.getTotalAttempts() + " attempts!";

            } else if (guess < randomnumber) {
                return "Your guess is too low. Try again.";

            } else {
                return "Your guess is too high. Try again.";
            }
        }
        return "Game has finished already.";
    }
    public GameStatus quitGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game " + gameId + " can not be found."));

        if (!game.isGameFinished()) {
            game.setGameFinished(true);
            game.setGameStatus(GameStatus.UNSUCCESSFUL);
            gameRepository.save(game);

            return GameStatus.UNSUCCESSFUL; //OYUN DEVAM EDERKEN QUIT
        }

        else if(game.getGameStatus() == GameStatus.SUCCESSFUL){
            return GameStatus.SUCCESSFUL; //BASARILI OYUN QUIT ICIN CAGRILIRSA
        }
        else{
            return GameStatus.UNSUCCESSFUL; //QUIT EDILMIS OYUN TEKRAR CAGRILIRSA
        }
    }

    public List<Game> getGamesSortedByTotalAttempts() {
        List<Game> gameList = getGamesByStatus(GameStatus.SUCCESSFUL);

        gameList.sort(Comparator.comparingInt(Game::getTotalAttempts));
        return gameList;
    }

    public Game getMinimumAttemptGameOfAPlayer(Long playerId) {
        List<Game> onePlayersGameList = gameRepository.findByPlayerId(playerId);

        if (!onePlayersGameList.isEmpty()) {
            return onePlayersGameList.stream()
                    .min(Comparator.comparingInt(Game::getTotalAttempts))
                    .orElse(null);
        }

        return null;
    }


    public List<String> getGamesSortedByTotalAttemptsWithUserNames() {
        List<Game> games =getGamesByStatus(GameStatus.SUCCESSFUL);

        games.sort(Comparator.comparingInt(Game::getTotalAttempts));

        List<String> gamesWithUsernames = new ArrayList<>();
        for (Game game : games) {
            gamesWithUsernames.add(game.getPlayer().getUsername() + ", Game ID: " + game.getId() + ", Total Attempts: " + game.getTotalAttempts());
        }

        return gamesWithUsernames;
    }

}
