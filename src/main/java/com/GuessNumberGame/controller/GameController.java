package com.GuessNumberGame.controller;

import com.GuessNumberGame.model.Game;
import com.GuessNumberGame.model.GameStatus;
import com.GuessNumberGame.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    //oyun olusturuldu player id si girildiginde
    @PostMapping("/start/{playerId}")
    public ResponseEntity<String> createGame(@PathVariable Long playerId){
        Long createdGameId =gameService.createGame(playerId);

        if(createdGameId != null){
            return ResponseEntity.status(HttpStatus.CREATED).body("Game has created. Your game id is " +createdGameId);
        }
        else{
            return ResponseEntity.badRequest().body("Failed to create game.");
        }
    }

    //guess yazariz donen ifade serviste yazıldı
    @PostMapping ("/guess/{gameId}/{guess}")
    ResponseEntity<String> makeGuess(@RequestBody Long gameId, @PathVariable int guess){
        String result= gameService.makeGuess(gameId, guess);
        return ResponseEntity.ok(result);
    }

    //IN PROGRESS ie oyun quit edilebilir
    @PutMapping("/quit/{gameId}")
    public ResponseEntity<String> quitGame(@PathVariable Long gameId){

        GameStatus gameStatus =gameService.quitGame(gameId);

        if(gameStatus == GameStatus.UNSUCCESSFUL){
            return ResponseEntity.ok(" Game has been quit. You can start another game!");
        }
        else{
            return ResponseEntity.ok("This game has already been completed.");
        }
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId){
        Game game = gameService.getGameById(gameId);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/all-games")
    public ResponseEntity<List<Game>> getAllGames(){
        List<Game> gameList =gameService.getAllGames();
        return ResponseEntity.ok(gameList);
    }

    @GetMapping("/in-progress-games")
    public ResponseEntity<List<Game>> getInProgressGames(){
        List<Game> inProgressGames = gameService.getGamesByStatus(GameStatus.IN_PROGRESS);
        return ResponseEntity.ok(inProgressGames);
    }

    @GetMapping("/successful-games")
    public ResponseEntity<List<Game>> getSuccessfulGames(){
        List<Game> successfulGames = gameService.getGamesByStatus(GameStatus.SUCCESSFUL);
        return ResponseEntity.ok(successfulGames);
    }

    @GetMapping("/unsuccessful-games")
    public ResponseEntity<List<Game>> getUnsuccessfulGames(){
        List<Game> unsuccessfulGames = gameService.getGamesByStatus(GameStatus.UNSUCCESSFUL);
        return ResponseEntity.ok(unsuccessfulGames);
    }

    //tum oyunların attempte gore sıralanısı
    @GetMapping("/sorted-by-guess-attempts")
    public ResponseEntity<List<Game>> getGamesSortedByTotalAttempts(){
        List<Game> gameList =gameService.getGamesSortedByTotalAttempts();
        return ResponseEntity.ok(gameList);
    }

    //bir playerın oynadıgı oyunlardan attempti en az olanı
    @GetMapping("player-minimum-guess-attempt-game/{playerId}")
    public ResponseEntity<Game> getMinimumAttemptGameByPlayerId(@PathVariable Long playerId){
        Game game =gameService.getMinimumAttemptGameOfAPlayer(playerId);

        if(game!=null){
            return ResponseEntity.ok(game);
        }
        else {
            return ResponseEntity.notFound().build(); //???
        }
    }

    //tum oyunların attempte gore sıralanısı ve player username eklenmıs hali STRING METNI donuyor
    @GetMapping("/sorted-by-guess-attempts-with-usernames")
    public ResponseEntity<List<String>> getGamesSortedByTotalAttemptsWithUserNames(){
        List<String> gameAndUsernameList =gameService.getGamesSortedByTotalAttemptsWithUserNames();
        return ResponseEntity.ok(gameAndUsernameList);
    }

}
