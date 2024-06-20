
public class Board {
    private Tile[][] board; // 2D array to represent the chessboard
    private int boardSize; // Size of the board (n x n)

    // Constructor: Creates a new board with the specified size
    public Board(int n){
        this.board = new Tile[n][n];
        this.boardSize = n;
        initializeBoard(); // Initialize the board with empty tiles
    }


    // Copy constructor: Creates a new board by copying another board
    public Board(Board copy){
        this.boardSize = copy.getSize();
        this.board = copy(copy);
    }

    // Creates a deep copy of the board
    private Tile[][] copy(Board copy) {
        Tile[][] k = new Tile[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                k[i][j] = new Tile(copy.board[i][j]);
            }
        }
        return k;
    }



    // Initializes the board with empty tiles
    private void initializeBoard() {
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                board[i][j] = new Tile(i,j);
            }
        }
    }

    // Gets the tile at the specified coordinates (i, j)
    public Tile getTile(int i, int j){
        return board[i][j];
    }

    // Gets the tile at the specified coordinates (using Queen's coordinates)
    public Tile getTile(Queen.coordinates c){
        return board[c.x][c.y];
    }

    // Returns the size of the board
    public int getSize(){
        return this.boardSize;
    }

    // Generates a string representation of the board (with queens as 'Q' and empty tiles as '_')
    public String toString() {
        StringBuilder k = new StringBuilder("");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (getTile(i, j).hasQueen())
                    k.append("|Q|"); // Show 'Q' for queen
                else
                    k.append("|_|"); // Show '_' for empty tile
            }
            k.append("\n");
        }
        return k.toString();
    }

}
