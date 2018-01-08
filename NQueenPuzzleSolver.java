package com.microfocus.arthur.karl;

import java.util.ArrayList;

/**
 * Created by karthur on 8/26/2017.
 */
public class NQueenPuzzleSolver {
    private static ArrayList<int []> g_solutions; // For keeping track of solutions
    private static int [] g_currentQueens; // Only need to record column per row
    private static boolean [][] g_board; // For keeping track of eliminated squares
    private static int g_dimension;
    private static boolean g_enforceUnique;
//    private static String [] g_horizontalCoords = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private static String [] g_horizontalCoords = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private static String [] g_verticalCoords = {" 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26"};
//    private static int g_solutionCount;

    public static void main(String[] args) {
        if( args.length < 2 ) {
            args = new String[] {
//                    "4", // Start with a model that is easily verified: 2 solutions, non-corners
//                    "5",
//                    "7",
                    "8", // Most users will expect the chess board size puzzle
                    "true" // Most users will want unique solutions
//                    "false"
            };
        }

        int numberOfQueens = Integer.parseInt(args[0]);
        boolean uniqueSolutions = Boolean.parseBoolean(args[1]);

        process(numberOfQueens,uniqueSolutions);
        displaySolutions();
    }

    private static void process(int squareSize, boolean onlyUnique) {
        g_dimension = (squareSize < 0) ? -1 * squareSize : squareSize;
        if(g_dimension < 4) {
            // Queens Puzzle does not work for 2 or 3; and 1 is trivial
            return;
        }
        g_enforceUnique = onlyUnique;

        initialize();
        findAllSolutions();
    }

    private static void findAllSolutions() {
        // Allow one queen per row and one per column; always start with first row
        placeWithinRow(0);
    }

    private static void placeWithinRow(int row) {
        if(row == g_dimension) {
            // A solution was found!
            recordSolution(g_currentQueens);
//            g_solutionCount = g_solutionCount + 1;
//            System.out.println("\nSolution #" + g_solutionCount);
//            displayBoard(g_currentQueens);
            return;
        }

        for(int column = 0; column < g_dimension; column++) {
            if(!g_board[column][row]){
                ArrayList<int []> newlyEliminated = placeQueen(column,row);
                placeWithinRow(row + 1);
                removeQueen(newlyEliminated);
            }
        }
    }

    // Assumes the designated location is already verified to be allowed
    private static ArrayList<int []> placeQueen(int column, int row) {
        // Place the queen at the specified location
        g_currentQueens[row] = column;

        // Disable, remember, and return squares below it that are eliminated by this queen
        ArrayList<int []> eliminated = new ArrayList<>();
        int leftDiagonal = column;
        int rightDiagonal = column;
        for(int i = row + 1; i < g_dimension; i++) {
            // Remove column immediately below if not already eliminated (by previous diagonals)
            if(!g_board[column][i]) {
                int [] coordinate = {column, i};
                eliminated.add(coordinate);
                g_board[column][i] = true;
            }
            // Remove left diagonal below if valid and not already eliminated
            leftDiagonal = leftDiagonal - 1;
            if((leftDiagonal > -1) && (!g_board[leftDiagonal][i])) {
                int [] coordinate = {leftDiagonal, i};
                eliminated.add(coordinate);
                g_board[leftDiagonal][i] = true;
            }

            // Remove right diagonal below if valid and not already eliminated
            rightDiagonal = rightDiagonal + 1;
            if((rightDiagonal < g_dimension) && (!g_board[rightDiagonal][i])) {
                int [] coordinate = {rightDiagonal,i};
                eliminated.add(coordinate);
                g_board[rightDiagonal][i] = true;
            }
        }
        return eliminated;
    }

    private static void removeQueen(ArrayList<int []> previous) {
        // Enable squares below it that were previously eliminated by this queen
        int [] current;
        for(int i = 0; i < previous.size(); i++) {
            current = previous.get(i);
            g_board[current[0]][current[1]] = false;
        }
        previous.clear();
    }

    private static void initialize() {
        g_solutions = new ArrayList<>();
        g_currentQueens = new int [g_dimension];
        g_board = new boolean[g_dimension][g_dimension];

        for(int i = 0; i < g_dimension; i++) {
            for(int j = 0; j < g_dimension; j++) {
                g_board[i][j] = false;
            }
        }
    }

    private static void displayBoard(boolean [][] board) {
        System.out.print("  ");
        for(int i = 0; i < g_dimension; i++) {
            System.out.print("|" + g_horizontalCoords[i]);
        }
        System.out.println("|");

        for(int i = 0; i < g_dimension; i++) {
            System.out.print(g_verticalCoords[g_dimension - i - 1]);
            for(int j = 0; j < g_dimension; j++) {
                System.out.print("|" + (board[j][i] ? "-" : " "));
            }
            System.out.println("|" + g_verticalCoords[g_dimension - i - 1]);
        }

        System.out.print("  ");
        for(int i = 0; i < g_dimension; i++) {
            System.out.print("|" + g_horizontalCoords[i]);
        }
        System.out.println("|");

    }

    private static void displayBoard(int [] queens) {
        System.out.print("  ");
        for(int i = 0; i < g_dimension; i++) {
            System.out.print("|" + g_horizontalCoords[i]);
        }
        System.out.println("|");

        for(int i = 0; i < g_dimension; i++) {
            System.out.print(g_verticalCoords[g_dimension - i - 1]);
            for(int j = 0; j < g_dimension; j++) {
                System.out.print("|" + (j == queens[i] ? "*" : " "));
            }
            System.out.println("|" + g_verticalCoords[g_dimension - i - 1]);
        }

        System.out.print("  ");
        for(int i = 0; i < g_dimension; i++) {
            System.out.print("|" + g_horizontalCoords[i]);
        }
        System.out.println("|");

    }

    private static void displaySolutions() {
        int [] current;
        for(int i = 0; i < g_solutions.size(); i++) {
            current = g_solutions.get(i);
            System.out.println("\nSolution #" + (i + 1));
            displayBoard(current);
        }
    }

    private static void recordSolution(int [] queenSet) {
        if((!g_enforceUnique) || solutionIsUnique(queenSet)) {
            g_solutions.add(queenSet.clone());
        }
    }

    private static boolean solutionIsUnique(int [] queenSet) {
        // Rotate x3, flip and rotate x3 again; comparing against existing solutions adding if not found, breaking otherwise
        int [] manipulatedSet = queenSet.clone();
        int [] current;
        for(int i = 0; i < g_solutions.size(); i++) {
            current = g_solutions.get(i);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }

            manipulatedSet = rotate(manipulatedSet);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }

            manipulatedSet = rotate(manipulatedSet);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }

            manipulatedSet = rotate(manipulatedSet);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }

            manipulatedSet = flip(manipulatedSet);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }

            manipulatedSet = rotate(manipulatedSet);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }

            manipulatedSet = rotate(manipulatedSet);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }

            manipulatedSet = rotate(manipulatedSet);
            if(isEqual(current, manipulatedSet)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isEqual(int [] setA, int [] setB) {
        for(int i = 0; i < g_dimension; i++) {
            if(setA[i] != setB[i]) {
                return false;
            }
        }
        return true;
    }

    private static int [] rotate(int [] queenSet) {
        int [] newSet = new int[g_dimension];
        for(int i = 0; i < g_dimension; i++) {
            newSet[queenSet[i]] = g_dimension - 1 - i;
        }
        return newSet;
    }

    private static int [] flip(int [] queenSet) {
        int [] newSet = new int[g_dimension];
        for(int i = 0; i < g_dimension; i++) {
            newSet[g_dimension - 1 - i] = queenSet[i];
        }
        return newSet;
    }
}
