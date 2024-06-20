// this class represents a single tile on a chessboard for solving the n-queens problem
public class Tile {
    private boolean queen;
    private int x;
    private int y;
    private int value;

    public Tile(){
        queen = false;
    }

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Tile(Tile copy){
        this.x = copy.x;
        this.y = copy.y;
        this.queen = copy.queen;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public boolean hasQueen(){
        return queen;
    }

    public void placeQueens(){
        this.queen = true;
    }

    public void removeQueens(){
        this.queen = false;
    }

    public String toString(){
        return "("+x+","+y+") "+value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}