import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Queen queen = new Queen();
        Scanner sc = new Scanner(System.in);

        int n = 1000;
        // set the board size to 8
        int size = 8;
        int cases;
        int debug;
        boolean solution = false;
        int done = 0;
        // main menu loop
        while (!solution) {
            displayMenu(); // display menu options
            cases = Integer.parseInt(sc.nextLine());
            debug = 1; // debug mode

            switch (cases) {
                case 1:
                    SHC(queen, n, size, debug); // run steepest hill climbing algorithm
                    break;
                case 2:
                    CSP(sc, queen, n, size, debug); // Run Min-Conflicts Algorithm
                    break;
                case 3:
                    solution = true;  // Exit the program
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            if(!solution) {
                reprompt();
                //reprompt(); // Ask if the user wants to run again
                done = Integer.parseInt(sc.nextLine());
                if (done == 0)
                    solution = true;  // Exit if user chooses not to run again
                else if (done == 1)
                    solution = false; // Continue if user chooses to run again
            }
        }
    }
    // this is the main menu
    private static void displayMenu() {
        System.out.println("Choose an option:");
        System.out.println("1 - Hill climbing algorithm");
        System.out.println("2 - Min-Conflicts Algorithm");
        System.out.println("3 - Exit");
    }

    private static void reprompt() {
        System.out.println("\nWould you like to continue? \n1 - Yes \n2 - No\n");
    }

    private static void printTime(Queen queen) {
        System.out.println("Average time (ms): "+ queen.getTime());
    }

    // runs the steeping hill climbing algorithm
    private static void SHC(Queen queen, int n, int size, int debug) {
        System.out.println("Running steepest hill climbing algorithm...");
        queen.tests(n, size, 1, 0, 0, debug);
        System.out.println(queen.getData());
        printTime(queen);
    }

    // runs the min conflicts algorithm
    private static void CSP(Scanner sc, Queen queen, int n, int size, int debug) {
        System.out.println("Running Min-Conflicts Algorithm");
        int steps = 0;
        if(steps == 0) {
            steps = 500; // set the number of steps (adjust as needed)
            queen.tests(n, size, 2, steps, 0, debug);
            System.out.println(queen.getData());
            printTime(queen);
        }
    }
}