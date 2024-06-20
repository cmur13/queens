/**
 * This file contains the implementations used to solve the trials for the N-Queen problems
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

public class Queen {

    class coordinates {
        int x;
        int y;

        public coordinates(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    // fields
    private Board board;
    private List<coordinates> queens;
    private Map<Tile, ArrayList<Tile>> map;
    private PriorityQueue<Node> pq;
    private double tests;
    private double success;
    // add time
    private long startTime;
    private long endTime;
    private long time;
    private long totalTime;
    private long avgTime;
    private boolean debug;

    // default constructor
    public Queen(){
        tests = 0;
        success = 0;
        map = new HashMap<Tile, ArrayList<Tile>>();
        queens = new ArrayList<coordinates>();
        debug = false;
    }

    public Queen(int n){
        tests = 0;
        success = 0;
        debug = false;
        createBoard(n);
    }

    // creates a new board of size n
    public void createBoard(int n){
        this.board = new Board(n);
        map = new HashMap<Tile, ArrayList<Tile>>(n);
        queens = new ArrayList<coordinates>(n);
    }


    // Randomly places queens on the board (avoiding conflicts).
    public void generateRandom(){
        Random r = new Random();
        int y;
        for(int i = 0; i < board.getSize(); i++){
            y = Math.abs(r.nextInt()%board.getSize());
            if(!checkQueens(i,y))
                putQueens(i, y);
            else
                i--;
        }
    }

    //  Conducts performance tests (hill climbing or CSP) for n trials.
    public void tests(int n, int size, int mode, int additional1, double additional2, int debug){
        //long startTime;
        //long endTime;
        //long time;
        totalTime = 0;
        avgTime = 0;
        tests = 0;
        success = 0;
        if(debug == 1)
            this.debug = true;
        else if (debug == 0)
            this.debug = false;
        if(board == null){
            createBoard(size);
            generateRandom();
        }
        for(int i = 0; i < n; i++){
            switch(mode){
                case 1:
                    this.startTime = System.currentTimeMillis();
                    hillClimbing();
                    //endTime = System.currentTimeMillis();
                    //time = endTime - startTime;
                    //totalTime += time;
                    break;
                case 2:
                    this.startTime = System.currentTimeMillis();
                    minConflict(additional1);
                    //endTime = System.currentTimeMillis();
                    //time = endTime - startTime;
                    //totalTime += time;
                    break;
                default:
                    continue;
            }
            avgTime = totalTime/n;
            board = new Board(size);
            queens.clear();
            map.clear();
            if(pq != null)
                pq.clear();
            generateRandom();
        }
    }

    //  Calculates the execution time for a trial.
    public double getTime() {
        return (this.endTime - this.startTime) * 1000.0;
        //return (this.endTime - this.startTime);
    }

    private void replaceQueen(Tile variable, Tile value) {
        removeQueens(variable);
        moveQueens(value);
    }

    public boolean checkQueens(int x, int y){
        return board.getTile(x, y).hasQueen();
    }

    public void putQueens(int x, int y){
        if(board.getTile(x, y).hasQueen())
            return;
        else{
            board.getTile(x, y).placeQueens();
            queens.add(x, new coordinates(x,y));
        }
    }
    private void moveQueens(Tile value) {
        putQueens(value.getX(), value.getY());
    }

    private void removeQueen(int x) {
        coordinates c;
        for(int i = 0; i < board.getSize(); i++){
            if(board.getTile(x, i).hasQueen()){
                c = new coordinates(x,i);
                board.getTile(x, i).removeQueens();
                queens.remove(x);
                return;
            }
        }
    }

    private void removeQueens(Tile variable) {
        removeQueen(variable.getX());
    }

    // calculates the total number of attacking pairs of queens on the chessboard
    public int attack(){
        int allAttackers = 0;
        for(int i = 0; i < board.getSize(); i++){
            for(int j = 0; j < board.getSize(); j++){
                if(board.getTile(i,j).hasQueen())
                    allAttackers += attack(i,j);
            }
        }
        map.clear();
        return allAttackers;
    }

    //  helper method computes the number of attackers for a specific queen at position x and y
    private int attack(int x, int y){
        int a = 0;
        a += columnAttack(x, y);
        a += rowAttack(x, y);
        a += diagonalAttack(x, y);
        board.getTile(x,y).setValue(a);
        return a;
    }

    // calculates the number of attackers in the diagonal directions
    private int diagonalAttack(int x, int y) {
        int i = x; int j = y;
        int attack = 0;
        while(i < board.getSize() && j < board.getSize()){
            if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
                attack = Attackers(attack, board.getTile(x, y), board.getTile(i, j));
            i++;
            j++;
        }
        i = x; j = y;
        while(i < board.getSize() && j >= 0){
            if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
                attack = Attackers(attack, board.getTile(x, y), board.getTile(i, j));
            i++;
            j--;
        }
        i = x; j = y;
        while(i >= 0 && j < board.getSize()){
            if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
                attack = Attackers(attack, board.getTile(x, y), board.getTile(i, j));
            i--;
            j++;
        }
        i = x;
        j = y;
        while(i >= 0 && j >= 0){
            if(board.getTile(i, j).hasQueen() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
                attack = Attackers(attack, board.getTile(x, y), board.getTile(i, j));
            i--;
            j--;
        }
        return attack;
    }

    // computes the number of attackers in the same row as the queen
    private int rowAttack(int x, int y) {
        int attack = 0;
        for(int i = 0; i < board.getSize(); i++){
            if(board.getTile(x, i).hasQueen() && i != y && !counted(board.getTile(x, y), board.getTile(x,i)))
                attack = Attackers(attack, board.getTile(x, y), board.getTile(x,i));
        }
        return attack;
    }

    // this method calculates the number of attackers in the same column as the queen
    private int columnAttack(int x, int y) {
        int attack = 0;
        for(int i = 0; i < board.getSize(); i++){
            if(board.getTile(i, y).hasQueen() && i != x && !counted(board.getTile(x, y), board.getTile(i,y)))
                attack = Attackers(attack, board.getTile(x, y), board.getTile(i,y));
        }
        return attack;
    }

    //  helper method updates the total number of attackers and maintains the map of attacking pairs
    private int Attackers(int total, Tile first, Tile second){
        total++;
        if(map.get(first) != null && map.get(second) != null){
            if(!map.get(first).contains(second) && !map.get(second).contains(first)){
                map.get(first).add(second);
                map.get(second).add(first);
            }
        }else if(map.get(first) == null && map.get(second) != null){
            map.put(first, new ArrayList<Tile>());
            map.get(first).add(second);
            map.get(second).add(first);
        }else if(map.get(second) == null && map.get(first) != null){
            map.put(second, new ArrayList<Tile>());
            map.get(first).add(second);
            map.get(second).add(first);
        }else{
            map.put(first, new ArrayList<Tile>());
            map.put(second, new ArrayList<Tile>());
            map.get(first).add(second);
            map.get(second).add(first);
        }
        return total;
    }

    // method checks whether a pair of tiles has already been counted as attackers
    private boolean counted(Tile first, Tile second){
        if(map.isEmpty())
            return false;
        else if(map.containsKey(first))
            if(map.get(first).contains(second))
                return true;
            else
                return false;
        else
            return false;
    }

    // This method implements the hill climbing algorithm for solving the n-Queens problem
    public Node hillClimbing(){
        Node current;
        Node next;
        current = new Node(board, attack(), (ArrayList<coordinates>) queens);
        pq = new PriorityQueue<Node>();
        while(true) {
            next = getNeighbor(current);
            if (next.value >= current.value) {
                tests++;
                if (current.value == 0) {
                    success++;
                    if (debug)
                        System.out.println(current.board.toString());
                }
                return current;
            }
            current = next;
        }
    }

    // helper method generates a neighboring configuration (node) based on the current configuration
    private Node getNeighbor(Node current) {
        coordinates c;
        Node copy = new Node(current);
        for(int i = 0; i < copy.board.getSize(); i++){
            for(int j = 0; j < copy.board.getSize(); j++){
                c = new coordinates(i,j);
                if(!copy.board.getTile(i, j).hasQueen()){
                    this.board = copy.board;			//copy status of the board
                    this.queens = copy.queens;			//place current queen locations in list
                    removeQueen(i);							//remove queen in row i
                    putQueens(i,j);						//move queen to coords (i,j)
                    copy.setValue(attack());		//get new value
                    pq.add(copy);					//enqueue copy node
                    copy = new Node(current);			//make new copy
                }
            }
        }
        Node n = pq.poll();
        return n;
    }

    public Node minConflict(int steps){
        tests++;
        Node current;
        Tile variable;
        Tile value;
        boolean wrong = false;
        Random r = new Random();
        current = new Node(board, attack(), (ArrayList<coordinates>) queens);
        for(int i = 0; i < steps; i++){
            if(current.value == 0){
                success++;
                if(debug)
                    System.out.println(board.toString());
                return current;
            }
            do{
                variable = board.getTile(queens.get(Math.abs(r.nextInt()%queens.size())));
                if(totalAttacks(variable) > 0)
                    wrong = true;
                else
                    wrong = false;
            }while(!wrong);
            value = minimize(variable);
            replaceQueen(variable, value);
            variable = value;
            current.update(board, attack(), (ArrayList<coordinates>) queens);
            wrong = false;
        }
        return current;
    }

    //helper method selects the best value (position) for a given variable (queen)
    private Tile minimize(Tile variable) {
        List<Tile> m = new ArrayList<Tile>();
        int mins = attack(variable.getX(),variable.getY());
        Random r = new Random();
        Tile focus;
        for(int i = 0; i < board.getSize(); i++){
            focus = board.getTile(variable.getX(), i);
            if(i == variable.getY()){
                continue;
            }else if(totalAttacks(focus) < mins){
                mins = totalAttacks(focus);
                m = new ArrayList<Tile>();
                m.add(focus);
            } else if (totalAttacks(focus) == mins){
                m.add(focus);
            }
        }
        if(!m.isEmpty())
            return m.get(Math.abs(r.nextInt()%m.size()));
        else
            return variable;
    }

    // computes the total number of attacks for a given variable (queen).
    private int totalAttacks(Tile variable) {
        int x; int y; int toReturn = 0;
        x = variable.getX();
        y = variable.getY();
        toReturn += columnAttack(x,y);
        toReturn += diagonalAttack(x,y);
        variable.setValue(toReturn);
        map.clear();
        return toReturn;
    }
    //  Generates a summary of trial results (number of trials and success rate).
    public String getData(){
        String toReturn;
        toReturn = "Trials: "+(int) tests +"\nSuccess Rate: "+(double)(success/ tests)*100+"%";
        return toReturn;
    }
}