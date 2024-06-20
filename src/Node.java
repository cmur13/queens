import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    Board board; // The chessboard configuration
    int value; // The evaluation value associated with this node
    List<Queen.coordinates> queens;  // List of queen coordinates


    // Constructor: Creates a new Node with the specified parameters
    public Node(Board board, int value, ArrayList<Queen.coordinates> queens){
        this.board = board;
        this.value = value;
        this.queens = queens;
    }
    // Copy constructor: Creates a new Node by copying another Node
    public Node(Node duplicate){
        this.board = new Board(duplicate.board);
        this.value = duplicate.value;
        this.queens = new ArrayList<Queen.coordinates>(duplicate.queens);
    }

    // Updates the Node with new values
    public void update(Board board, int value, ArrayList<Queen.coordinates> queens){
        this.board = board;
        this.value = value;
        this.queens = queens;
    }

    // sets the value field of Node
    public void setValue(int value){
        this.value = value;
    }

    // returns a string representation of the Node
    public String toString(){
        return (this.board.toString() + '\n' + this.value);
    }

    // compares two Node objects based on their value
    @Override
    public int compareTo(Node n) {
        return Integer.compare(this.value, n.value);
    }
}