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

    //kullanıcı id giriyor ve variablelar set ediliyor
    public Long createGame(Long playerId){
        Player player = playerService.findPlayerById(playerId);

        Random random = new Random();
        int randomNumber =random.nextInt(100)+1;
        Game game = new Game();
        game.setRandomNumber(randomNumber);
        game.setGameFinished(false);
        game.setTotalAttempts(0); //guess attempt 0 setlendi
        game.setPlayer(player);
        game.setGameStatus(GameStatus.IN_PROGRESS); // finish olana kadar in progress statusunde olacak

        game = gameRepository.save(game);

        if(game != null){
            return game.getId(); //kullanıcıya geri donduruluyor ki api tarafında hangi gameid kullancagını bilsin
        }
        else {
            return null;
        }

    }

    //oyun mantıgı burada isleniyor, game id ve guess parametresi alınarak belirtilen gamede tahmin yurutuluyor
    public String makeGuess(Long gameId, int guess){
        Game game =gameRepository.findById(gameId)
                .orElseThrow(()-> new GameNotFoundException("Game " + gameId + " can not found." ));

        int randomnumber = game.getRandomNumber();
        int totalAttempts = game.getTotalAttempts();

        if(!game.isGameFinished()) {
            totalAttempts++;
            if (guess == randomnumber) {
                game.setTotalAttempts(totalAttempts);
                game.setGameFinished(true);
                game.setGameStatus(GameStatus.SUCCESSFUL);
                gameRepository.save(game);
                return "Congratulations you have guessed the number in " + game.getTotalAttempts() + " attempts!";

            } else if (guess < randomnumber) {
                game.setTotalAttempts(totalAttempts);
                gameRepository.save(game);
                return "Your guess is too low. Try again.";

            } else {
                game.setTotalAttempts(totalAttempts);
                gameRepository.save(game);
                return "Your guess is too high. Try again.";
            }


        }
        return "Game has finished already."; //eger verilen game id coktan bitmis bir oyunu gosteriyorsa
    }
    public GameStatus quitGame(Long gameId) { // oyun devam ederken quit olmak icin
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game " + gameId + " can not be found."));

        if (!game.isGameFinished()) {
            game.setGameFinished(true);
            game.setGameStatus(GameStatus.UNSUCCESSFUL);
            gameRepository.save(game);

            return GameStatus.UNSUCCESSFUL; //OYUN DEVAM EDERKEN QUIT EDILIYOR
        }

        else if(game.getGameStatus() == GameStatus.SUCCESSFUL){
            return GameStatus.SUCCESSFUL; //ZATEN BASARILI OYUN QUIT ICIN CAGRILIRSA
        }
        else{
            return GameStatus.UNSUCCESSFUL; //ZATEN QUIT EDILMIS OYUN TEKRAR QUIT ICIN CAGRILIRSA
        }
    }

    public List<Game> getGamesSortedByTotalAttempts() { //tum basarılı sonuclanmıs oyunları tahmin sıralamasına gore dondur
        List<Game> gameList = getGamesByStatus(GameStatus.SUCCESSFUL);

        gameList.sort(Comparator.comparingInt(Game::getTotalAttempts));
        return gameList;
    }


    public List<String> getGamesSortedByTotalAttemptsWithUserNames() { //tum sonuclanmıs uygulamaları kullanıcı isimleriyle birlikte metin seklinde dondur ve guess sayısına gore sort et
        List<Game> games =getGamesByStatus(GameStatus.SUCCESSFUL);

        games.sort(Comparator.comparingInt(Game::getTotalAttempts));

        List<String> gamesWithUsernames = new ArrayList<>();
        for (Game game : games) {
            gamesWithUsernames.add(game.getPlayer().getUsername() + ", Game ID: " + game.getId() + ", Total Attempts: " + game.getTotalAttempts());
        }

        return gamesWithUsernames;
    }

}
