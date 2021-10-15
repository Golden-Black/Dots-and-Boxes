import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DotsAndBoxes {
    public void start(int rows, int cols, int plys, boolean userMove) throws IOException {
        BoardMap boardMap = new BoardMap(rows, cols, plys);

        while(!boardMap.GameIsComplete()){
            boardMap.printMap();

            if(userMove) { // if the user is going to take the next move
                System.out.println("Enter the locations of the two dots next the edge.");
                int[] firstDot;
                firstDot = GatherDotInfo(boardMap, "first");
                int[] secondDot;
                secondDot = GatherDotInfo(boardMap, "second");
                MarkDown(boardMap, firstDot, secondDot);
            }else{
                System.out.println("AI has made the move");

            }
            userMove = !userMove;
        }
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

    public void MarkDown(BoardMap boardMap, int[] firstDot, int[] secondDot) throws IOException {
        boolean eligibleDots = EligibleDots(boardMap, firstDot, secondDot);

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
        }

        if(firstDotCol == secondDotCol){ // firstDot[1] -> col
            if(firstDotRow < secondDotRow){  // go down to find the edge (to the first dot)
                boardMap.theMap.get(2 * firstDotRow + 1).set(2 * secondDotCol, "x");
            }else{ // go down to find the edge (to the second dot)
                boardMap.theMap.get(2 * secondDotRow + 1).set(2 * secondDotCol, "x");
            }
        }

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


}
