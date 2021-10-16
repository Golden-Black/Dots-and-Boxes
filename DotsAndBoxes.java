import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

public class DotsAndBoxes {
    public void start(int rows, int cols, int plys, boolean userMove) throws IOException {
        BoardMap boardMap = new BoardMap(rows, cols, plys);
        AI ai = new AI(0);
        User user = new User(0);
        boardMap.printMap();
        System.out.println("\n");

        while(!boardMap.GameIsComplete()){
            if(userMove) { // if the user is going to take the next move
                System.out.println("Enter the locations of the two dots next the edge.");
                int[] firstDot;
                firstDot = GatherDotInfo(boardMap, "first");
                int[] secondDot;
                secondDot = GatherDotInfo(boardMap, "second");
                user.score += MarkDown(boardMap, firstDot, secondDot, userMove);
            }else{
                int[][] nextMove = new int[2][2];
                boolean alphaBetaOn = true;
                boolean min = plys % 2 != 0;
                
                int[][] zeroPly = NoSearchMove(boardMap);
                nextMove = ai.MiniMax(boardMap, 0, plys, ai.score, alphaBetaOn, min, zeroPly, user.score);
                ai.score += MarkDown(boardMap, nextMove[0], nextMove[1], userMove);
                System.out.println("AI has made the move");
            }
            boardMap.printMap();
            System.out.println("AI scored: " + ai.score);
            System.out.println("User scored: " + user.score);
            System.out.println("\n");
            userMove = !userMove;
        }
        System.out.println("The game is over.");
        System.out.println("AI vs User");
        System.out.println(ai.score + " " + " : " + " " + user.score);

        if(ai.score > user.score){
            System.out.println("AI is the winner!");
        }else if(ai.score == user.score){
            System.out.println("It is a draw!");
        }else{
            System.out.println("Congrats! You won!");
        }
    }


    // if ply is 0, the AI will return a random available next step
    private int[][] NoSearchMove(BoardMap boardMap) {
        int counting = -1;
        HashMap<Integer, int[][]> availableEdges = new HashMap<Integer, int[][]>();

        for(int i = 0; i < 2 * boardMap.rows + 1; i++){
            for(int j = 0; j < 2 * boardMap.cols + 1; j++){
                if(boardMap.theMap.get(i).get(j).equals(" —— ")){
                    int[][] dots = new int[2][2];

                    dots[0][0] = i/2;        // leftDotRow
                    dots[0][1] = (j - 1)/2;  // leftDotCol
                    dots[1][0] = i/2;        // rightDotRow
                    dots[1][1] = (j + 1)/2;  // rightDotCol
                    availableEdges.put(counting += 1, dots);
                }
                if(boardMap.theMap.get(i).get(j).equals("|")){
                    int[][] dots = new int[2][2];

                    dots[0][0] = (i - 1)/2;  // upperDotRow
                    dots[0][1] = j / 2;      // upperDotCol
                    dots[1][0] = (i + 1)/2;    // downDotRow
                    dots[1][1] = j / 2;          // downDotCol
                    availableEdges.put(counting += 1, dots);
                }
            }
        }
        Random rd = new Random();
        int move = rd.nextInt(availableEdges.size());
        return availableEdges.get(move);
    }


    // Record the user input for the location information of the dot
    public int[] GatherDotInfo(BoardMap boardMap, String str) throws IOException {
        System.out.println("Please specify the roww and column coordinates of the " + str + " dot (separated by comma: )");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String firstDotBr = br.readLine();
        int dotRow = Integer.parseInt(String.valueOf(firstDotBr.charAt(0)));
        int dotCol = Integer.parseInt(String.valueOf(firstDotBr.charAt(2)));

        while(dotCol < 0 || dotCol > boardMap.cols || dotRow < 0 || dotRow > boardMap.rows){
            System.out.println("The dot entered does not exist.");
            System.out.println("Please specify the row and column coordinates of the " + str + " dot (separated by comma: ");
            BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
            String firstDotBr1 = br4.readLine();
            dotRow = Integer.parseInt(String.valueOf(firstDotBr1.charAt(0)));
            dotCol = Integer.parseInt(String.valueOf(firstDotBr1.charAt(2)));
        }
        int[] dot = new int[2];
        dot[0] = dotRow;
        dot[1] = dotCol;
        return dot;
    }


    // Mark down the edge of user's choice & check if user inputs are valid
    // Included repeated-edge checking & dot-eligibility checking
    public int MarkDown(BoardMap boardMap, int[] firstDot, int[] secondDot, boolean userMove) throws IOException {
        boolean eligibleDots = EligibleDots(boardMap, firstDot, secondDot);
        int score = 0;

        // if two dots are the same
        while(!eligibleDots){
            firstDot = GatherDotInfo(boardMap, "first");
            secondDot = GatherDotInfo(boardMap, "second");
            eligibleDots = EligibleDots(boardMap, firstDot, secondDot);
        }

        // mark the edge as checked/occupied
        int firstDotRow = firstDot[0];
        int firstDotCol = firstDot[1];
        int secondDotRow = secondDot[0];
        int secondDotCol = secondDot[1];

        if(firstDotRow == secondDotRow){ // firstDot[0] -> row
            if(firstDotCol < secondDotCol){ // go right to find the edge (to the first dot)
                boardMap.theMap.get(2 * firstDotRow).set(2 * firstDotCol + 1, " xx ");
            }else{ // go right to find the edge (to the second dot)
                boardMap.theMap.get(2 * secondDotRow).set(2 * secondDotCol + 1, " xx ");
            }
            score = checkScore(boardMap, firstDotRow, firstDotCol, secondDotRow, secondDotCol, userMove);
        }

        if(firstDotCol == secondDotCol){ // firstDot[1] -> col
            if(firstDotRow < secondDotRow){  // go down to find the edge (to the first dot)
                boardMap.theMap.get(2 * firstDotRow + 1).set(2 * firstDotCol, "x");
            }else{ // go down to find the edge (to the second dot)
                boardMap.theMap.get(2 * secondDotRow + 1).set(2 * secondDotCol, "x");
            }
            score = checkScore(boardMap, firstDotRow, firstDotCol, secondDotRow, secondDotCol, userMove);
        }
        return score;
    }


    public boolean EligibleDots(BoardMap boardMap, int[] firstDot, int[] secondDot) throws IOException {
        int firstDotRow = firstDot[0];
        int firstDotCol = firstDot[1];
        int secondDotRow = secondDot[0];
        int secondDotCol = secondDot[1];

        // check if the dots are the same
        if(firstDotRow == secondDotRow && firstDotCol == secondDotCol){
            System.out.println("Two dots entered are the same, please try again.");
            return false;
        }

        if(firstDotRow != secondDotRow && firstDotCol != secondDotCol){
            System.out.println("Two dots entered are not joint dots, please try again.");
            return false;
        }

        // check if the edge between the dots have already been marked
        if(firstDotRow == secondDotRow){ // firstDot[0] -> row
            if(firstDotCol < secondDotCol){ // go right to find the edge (to the first dot)
                if(boardMap.theMap.get(2 * firstDotRow).get(2 * firstDotCol + 1).equals(" xx ")){
                    System.out.println("The edge has been marked. Please enter two new dots.");
                    return false;
                }
                // return !boardMap.theMap.get(firstDotRow).get(firstDotCol + 1).equals(" —— ")
            }else{ // go right to find the edge (to the second dot)
                if(boardMap.theMap.get(2 * secondDotRow).get(2 * secondDotCol + 1).equals(" xx ")){
                    System.out.println("The edge has been marked. Please enter two new dots.");
                    return false;
                }
                // return boardMap.theMap.get(secondDotRow).get(secondDotCol + 1).equals(" —— ")
            }
        }

        if(firstDotCol == secondDotCol){ // firstDot[1] -> col
            if(firstDotRow < secondDotRow){  // go down to find the edge (to the first dot)
                if(boardMap.theMap.get(2 * firstDotRow + 1).get(2 * firstDotCol).equals("x")){
                    System.out.println("The edge has been marked. Please enter two new dots.");
                    return false;
                }
            }else{ // go down to find the edge (to the second dot)
                if(boardMap.theMap.get(2 * secondDotRow + 1).get(2 * secondDotCol).equals("x")){
                    System.out.println("The edge has been marked. Please enter two new dots.");
                    return false;
                }
            }
        }
        return true;
    }


    // check if the move that the user made has scored for him
    public int checkScore(BoardMap boardMap, int firstDotRow, int firstDotCol, int secondDotRow, int secondDotCol, boolean userMove){
        int score = 0;
        String chosenEdge = "";
        int chosenEdgeRow = 0;
        int chosenEdgeCol = 0;

        if(firstDotRow == secondDotRow){ // check up & down
            if(firstDotCol < secondDotCol){ // go right to find the edge (to the first dot)
                // chosenEdge = boardMap.theMap.get(2 * firstDotRow).get(2 * firstDotCol + 1);
                chosenEdgeRow = 2 * firstDotRow;
                chosenEdgeCol = 2 * firstDotCol + 1;
            }else{ // go right to find the edge (to the second dot)
                // chosenEdge = boardMap.theMap.get(2 * secondDotRow).get(2 * secondDotCol + 1);
                chosenEdgeRow = 2 * secondDotRow;
                chosenEdgeCol = 2 * secondDotCol + 1;
            }

            // check up square
            boolean up = false, up_left = false, up_right = false;
            if(chosenEdgeRow > 0) {
                if (boardMap.theMap.get(chosenEdgeRow - 2).get(chosenEdgeCol).equals(" xx ")) { // check up edge
                    up = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow - 1).get(chosenEdgeCol - 1).equals("x")) { // check left edge
                    up_left = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow - 1).get(chosenEdgeCol + 1).equals("x")) { // check right edge
                    up_right = true;
                }
            }
            if(up && up_left && up_right){
                score = score + Integer.parseInt(String.valueOf(boardMap.theMap.get(chosenEdgeRow - 1).get(chosenEdgeCol).charAt(2)));
            }

            // check down square
            boolean down = false, down_left = false, down_right = false;
            if(chosenEdgeRow < 2 * boardMap.rows) {
                if (boardMap.theMap.get(chosenEdgeRow + 2).get(chosenEdgeCol).equals(" xx ")) { // check down edge
                    down = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow + 1).get(chosenEdgeCol - 1).equals("x")) { // check left edge
                    down_left = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow + 1).get(chosenEdgeCol + 1).equals("x")) { // check right edge
                    down_right = true;
                }
            }
            if(down && down_left && down_right){
                score = score + Integer.parseInt(String.valueOf(boardMap.theMap.get(chosenEdgeRow + 1).get(chosenEdgeCol).charAt(2)));
            }

        }
        if(firstDotCol == secondDotCol){ // check left & right
            if(firstDotRow < secondDotRow){  // go down to find the edge (to the first dot)
                chosenEdge = boardMap.theMap.get(2 * firstDotRow + 1).get(2 * firstDotCol);
                chosenEdgeRow = 2 * firstDotRow + 1;
                chosenEdgeCol = 2 * firstDotCol;
            }else{ // go down to find the edge (to the second dot)
                chosenEdge = boardMap.theMap.get(2 * secondDotRow + 1).get(2 * secondDotCol);
                chosenEdgeRow = 2 * secondDotRow + 1;
                chosenEdgeCol = 2 * secondDotCol;
            }

            // check left square
            boolean left = false, left_up = false, left_down = false;
            if(chosenEdgeCol > 0){
                if (boardMap.theMap.get(chosenEdgeRow).get(chosenEdgeCol - 2).equals("x")) { // check left edge
                    left = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow - 1).get(chosenEdgeCol - 1).equals(" xx ")) { // check up edge
                    left_up = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow + 1).get(chosenEdgeCol - 1).equals(" xx ")) { // check down edge
                    left_down = true;
                }
            }
            if(left && left_up && left_down){
                score = score + Integer.parseInt(String.valueOf(boardMap.theMap.get(chosenEdgeRow).get(chosenEdgeCol - 1).charAt(2)));
            }

            // check right square
            boolean right = false, right_up = false, right_down = false;
            if(chosenEdgeCol < 2 * boardMap.cols){
                if (boardMap.theMap.get(chosenEdgeRow).get(chosenEdgeCol + 2).equals("x")) { // check right edge
                    right = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow - 1).get(chosenEdgeCol + 1).equals(" xx ")) { // check up edge
                    right_up = true;
                }
                if (boardMap.theMap.get(chosenEdgeRow + 1).get(chosenEdgeCol + 1).equals(" xx ")) { // check down edge
                    right_down = true;
                }
            }
            if(right && right_up && right_down){
                score = score + Integer.parseInt(String.valueOf(boardMap.theMap.get(chosenEdgeRow).get(chosenEdgeCol + 1).charAt(2)));
            }

        }

        return score;
    }

}
