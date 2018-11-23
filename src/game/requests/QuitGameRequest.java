package game.requests;

//Thrown if player requested to exit the current game
//(by default, it is thrown when player enters "quit" during the game
public class QuitGameRequest extends Exception {
    public QuitGameRequest() {
        super();
    }
}
