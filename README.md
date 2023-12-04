"# guess-the-number" 

The project is a guessing game where it generates a random number between 1 and 100. The user tries to guess this number while receiving feedback from the project (such as whether the guess is smaller or larger than the actual number).

Before starting the game, the user must be previously saved in the database. Therefore, we either create a player by entering a username or start the game using an existing player (players can play multiple games). We specify which user we are playing with using the player ID. The application creates a new game and returns a key that shows which game we are playing. We use this key to enter our guesses. The number of attempts made is recorded during the game.

Games are ranked based on the minimum attempt count. Additionally, there is a written ranking that includes usernames (Username =.., Game =..., Total Attempt = ...).It is included in the rankings only if the game is completed successfully.

The game continues until the correct guess is made. (INPROGRESS and isFinished(false))

If the user wants to quit the game before it's finished, the game is marked as UNSUCCESSFUL and isFinished(true). This means that it will not be considered in the rankings.

Successfully completed games are marked as SUCCESSFUL and isFinished(true).

Endpoints Used in Player Controller

- POST /game/start/{playerId}  
- POST /game/guess/{gameId}/{guess}
- PUT /game/quit/{gameId}
- GET /game/{gameId}
- GET /game/all-games
- GET /game/in-progress-games  
- GET /game/successful-games  
- GET /game/unsuccessful-games  
- GET /game/sorted-by-guess-attempts  
- GET /game/sorted-by-guess-attempts-with-usernames

Endpoints Used in Player Controller

- POST /players/create/{username}
- GET /players/get
- GET /players/get/{playerId}
