public class Player {

    char color;

    public Player() {

    }

    public char getColor(){
        return this.color;
    }

    public Player(char color){
        this.color = color;
    }

    public void playMove(Game game, int finalI) {}

    public void playMove(Game game) {}

    public void rankOpponentsMove(Game game) {}
}
