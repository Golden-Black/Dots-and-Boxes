import java.io.IOException;
import java.util.ArrayList;

public class AI {
    public int score;

    public AI(int score) {
        this.score = score;
    }

    public int[][] NextStep(ArrayList<ArrayList<String>> theMap, int plys, int aiScore, int userScore, boolean min) throws IOException, CloneNotSupportedException {
        int[][] nextMove = new int[2][2];
        int[][] temp = new int[1][3];
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        temp = MinMax(theMap, plys, aiScore, userScore, min, alpha, beta);
        nextMove[0] = temp[0];
        nextMove[1] = temp[1];
        return nextMove;
    }

    public int[][] MinMax(ArrayList<ArrayList<String>> theMap, int plys, int aiScore, int userScore, boolean min, int alpha, int beta) throws IOException, CloneNotSupportedException {
        if (plys == 1) {
            ArrayList<int[][]> children = new ArrayList<int[][]>();
            // take the next available step
            for(int i = 0; i < theMap.size(); i++) {
                for (int j = 0; j < theMap.get(0).size(); j++) {
                    if (theMap.get(i).get(j).equals(" —— ")) {
                        int[][] dots = new int[2][2];
                        dots[0][0] = i / 2;        // leftDotRow
                        dots[0][1] = (j - 1) / 2;  // leftDotCol
                        dots[1][0] = i / 2;        // rightDotRow
                        dots[1][1] = (j + 1) / 2;  // rightDotCol
                        children.add(dots);
                    }
                    if (theMap.get(i).get(j).equals("|")) {
                        int[][] dots = new int[2][2];
                        dots[0][0] = (i - 1) / 2;  // upperDotRow
                        dots[0][1] = j / 2;      // upperDotCol
                        dots[1][0] = (i + 1) / 2;  // downDotRow
                        dots[1][1] = j / 2;      // downDotCol
                        children.add(dots);
                    }
                }
            }

            // count the score
            ArrayList<Integer> aiScores = new ArrayList<Integer>();
            ArrayList<Integer> userScores = new ArrayList<Integer>();

            for (int[][] child : children) {
                int[][] dots = child;
                // update the map
                // update the score (don't pass the dot)
                ArrayList<ArrayList<String>> map = copyMap(theMap);

                if(min == false){
                    int ai = aiScore;
                    ai += checkScore(map, dots, ai, userScore, false);
                    aiScores.add(ai);
                }else{
                    int user = userScore;
                    user += checkScore(map, dots, aiScore, userScore, false);
                    userScores.add(user);
                }
            }

            int max = Integer.MIN_VALUE;
            int mini = Integer.MAX_VALUE;
            int[] score = new int[1];
            int loc = 0;
            if(min == false) {
                for(int b = 0; b < aiScores.size(); b++){
                    if(aiScores.get(b) - userScore > max) {
                        max = aiScores.get(b) - userScore;
                        loc = b;
                    }
                }
                score[0] = max;
            }else {
                for (int b = 0; b < userScores.size(); b++) {
                    if (aiScore - userScores.get(b) < mini) {
                        mini = aiScore - userScores.get(b);
                        loc = b;
                    }
                }
                score[0] = mini;
            }

            int[][] nxMove = new int[3][2];
            nxMove[0] = children.get(loc)[0];
            nxMove[1] = children.get(loc)[1];
            nxMove[2] = score;
            // return the step and the score
            return nxMove;
        }

        // find all child positions
        ArrayList<int[][]> children = new ArrayList<int[][]>();
        for(int i = 0; i < theMap.size(); i++) {
            for (int j = 0; j < theMap.get(0).size(); j++) {
                if (theMap.get(i).get(j).equals(" —— ")) {
                    int[][] dots = new int[2][2];
                    dots[0][0] = i / 2;        // leftDotRow
                    dots[0][1] = (j - 1) / 2;  // leftDotCol
                    dots[1][0] = i / 2;        // rightDotRow
                    dots[1][1] = (j + 1) / 2;  // rightDotCol
                    children.add(dots);
                }
                if (theMap.get(i).get(j).equals("|")) {
                    int[][] dots = new int[2][2];
                    dots[0][0] = (i - 1) / 2;  // upperDotRow
                    dots[0][1] = j / 2;      // upperDotCol
                    dots[1][0] = (i + 1) / 2;  // downDotRow
                    dots[1][1] = j / 2;      // downDotCol
                    children.add(dots);
                }
            }
        }

        if(children.size() < plys){
            plys = children.size();
        }

        if(min) { // find the minimum value of user moves
            // update the map
            int mini = Integer.MAX_VALUE;

            // find corresponding scores of all children moves
            ArrayList<Integer> child_score = new ArrayList<Integer>();
            int[][] dots = new int[2][2];
            int[] score = new int[1];
            int[][] result = new int[3][2];

            for (int[][] child : children) {
                dots = child;
                // update the map
                // update the score (don't pass the dot)
                ArrayList<ArrayList<String>> map = copyMap(theMap);
                int user = userScore;
                user += checkScore(map, dots, aiScore, userScore, false);
                min = !min;
                result = MinMax(map, plys -1, aiScore, user, false, alpha, beta);

                score = result[2];
                beta = Math.min(score[0], beta);
                if(beta <= alpha){
                    break;
                }
//                child_score.add(score[0]);
            }

            // return the position with the largest score
//            int miniLocation = 0;
//            for(int b = 0; b < child_score.size(); b++){
//                if(child_score.get(b) < mini) {
//                    mini = child_score.get(b);
//                    miniLocation = b;
//                }
//            }
//
//            int[] minScore = new int[1];
//            minScore[0] = mini;
//
//            int[][] nxMove = new int[3][2];
//            nxMove[0] = children.get(miniLocation)[0];
//            nxMove[1] = children.get(miniLocation)[1];
//            nxMove[2] = minScore;
            return result;
        }

        if(!min){ // find the maximum value of the ai moves
            int max = Integer.MIN_VALUE;

            // find corresponding scores of all children moves
            ArrayList<Integer> child_score = new ArrayList<Integer>();

            // for each child, update the map, update the score, recursive call
            int[][] dots = new int[2][2];
            int[] score = new int[1];
            int[][] result = new int[3][2];

            for (int[][] child : children) {
                dots = child;
                ArrayList<ArrayList<String>> map = copyMap(theMap);
                // update the score & update the map
                int ai = aiScore;
                ai += checkScore(map, dots, aiScore, userScore, false);
                min = !min;
                result = MinMax(map, plys -1, ai, userScore, true, alpha, beta);
                score = result[2];
                alpha = Math.max(score[0], alpha);
                if(beta <= alpha){
                    break;
                }
//                child_score.add(score[0]);
            }

            // return the position with the largest score
//            int maxLocation = 0;
//            for(int b = 0; b < child_score.size(); b++){
//                if(child_score.get(b) > max) {
//                    max = child_score.get(b);
//                    maxLocation = b;
//                }
//            }
//            int[] maxScore = new int[1];
//            maxScore[0] = max;
//
//            int[][] nxMove = new int[3][2];
//            nxMove[0] = children.get(maxLocation)[0];
//            nxMove[1] = children.get(maxLocation)[1];
//            nxMove[2] = maxScore;
            return result;
        }


        return new int[0][];
    }

    private ArrayList<ArrayList<String>> copyMap(ArrayList<ArrayList<String>> theMap) {
        ArrayList<ArrayList<String>> mapCopy = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> strings : theMap) {
            ArrayList<String> row = new ArrayList<String>();
            for (int j = 0; j < theMap.get(0).size(); j++) {
                row.add(strings.get(j));
            }
            mapCopy.add(row);
        }
        return mapCopy;
    }

    public int checkScore(ArrayList<ArrayList<String>> map, int[][] dot, int aiScore, int userScore, boolean min) {
        if(!min){
            return aiScore + newScore(map, dot);
        }else{
            return userScore + newScore(map, dot);
        }
    }

    private int newScore(ArrayList<ArrayList<String>> map, int[][] dot) {
        int score = 0;
        String chosenEdge = "";
        int firstDotRow = dot[0][0];
        int firstDotCol = dot[0][1];
        int secondDotRow = dot[1][0];
        int secondDotCol = dot[1][1];
        int chosenEdgeRow = 0;
        int chosenEdgeCol = 0;

        if(firstDotRow == secondDotRow){ // check up & down
            if(firstDotCol < secondDotCol){ // go right to find the edge (to the first dot)
                map.get(2 * firstDotRow).set(2 * firstDotCol + 1, " xx ");
                chosenEdgeRow = 2 * firstDotRow;
                chosenEdgeCol = 2 * firstDotCol + 1;
            }else{ // go right to find the edge (to the second dot)
                // chosenEdge = boardMap.theMap.get(2 * secondDotRow).get(2 * secondDotCol + 1);
                map.get(2 * secondDotRow).set(2 * secondDotCol + 1, " xx ");
                chosenEdgeRow = 2 * secondDotRow;
                chosenEdgeCol = 2 * secondDotCol + 1;
            }

            // check up square
            boolean up = false, up_left = false, up_right = false;
            if(chosenEdgeRow > 0) {
                if (map.get(chosenEdgeRow - 2).get(chosenEdgeCol).equals(" xx ")) { // check up edge
                    up = true;
                }
                if (map.get(chosenEdgeRow - 1).get(chosenEdgeCol - 1).equals("x")) { // check left edge
                    up_left = true;
                }
                if (map.get(chosenEdgeRow - 1).get(chosenEdgeCol + 1).equals("x")) { // check right edge
                    up_right = true;
                }
            }
            if(up && up_left && up_right){
                score = score + Integer.parseInt(String.valueOf(map.get(chosenEdgeRow - 1).get(chosenEdgeCol).charAt(2)));
            }

            // check down square
            boolean down = false, down_left = false, down_right = false;
            if(chosenEdgeRow < map.size() - 1) {
                if (map.get(chosenEdgeRow + 2).get(chosenEdgeCol).equals(" xx ")) { // check down edge
                    down = true;
                }
                if (map.get(chosenEdgeRow + 1).get(chosenEdgeCol - 1).equals("x")) { // check left edge
                    down_left = true;
                }
                if (map.get(chosenEdgeRow + 1).get(chosenEdgeCol + 1).equals("x")) { // check right edge
                    down_right = true;
                }
            }
            if(down && down_left && down_right){
                score = score + Integer.parseInt(String.valueOf(map.get(chosenEdgeRow + 1).get(chosenEdgeCol).charAt(2)));
            }

        }
        if(firstDotCol == secondDotCol){ // check left & right
            if(firstDotRow < secondDotRow){  // go down to find the edge (to the first dot)
                // chosenEdge = map.get(2 * firstDotRow + 1).get(2 * firstDotCol);
                map.get(2 * firstDotRow + 1).set(2 * firstDotCol, "x");
                chosenEdgeRow = 2 * firstDotRow + 1;
                chosenEdgeCol = 2 * firstDotCol;
            }else{ // go down to find the edge (to the second dot)
                chosenEdge = map.get(2 * secondDotRow + 1).get(2 * secondDotCol);
                map.get(2 * secondDotRow + 1).set(2 * secondDotCol, "x");
                chosenEdgeRow = 2 * secondDotRow + 1;
                chosenEdgeCol = 2 * secondDotCol;
            }

            // check left square
            boolean left = false, left_up = false, left_down = false;
            if(chosenEdgeCol > 0){
                if (map.get(chosenEdgeRow).get(chosenEdgeCol - 2).equals("x")) { // check left edge
                    left = true;
                }
                if (map.get(chosenEdgeRow - 1).get(chosenEdgeCol - 1).equals(" xx ")) { // check up edge
                    left_up = true;
                }
                if (map.get(chosenEdgeRow + 1).get(chosenEdgeCol - 1).equals(" xx ")) { // check down edge
                    left_down = true;
                }
            }
            if(left && left_up && left_down){
                score = score + Integer.parseInt(String.valueOf(map.get(chosenEdgeRow).get(chosenEdgeCol - 1).charAt(2)));
            }

            // check right square
            boolean right = false, right_up = false, right_down = false;
            if(chosenEdgeCol < map.get(0).size() - 1){
                if (map.get(chosenEdgeRow).get(chosenEdgeCol + 2).equals("x")) { // check right edge
                    right = true;
                }
                if (map.get(chosenEdgeRow - 1).get(chosenEdgeCol + 1).equals(" xx ")) { // check up edge
                    right_up = true;
                }
                if (map.get(chosenEdgeRow + 1).get(chosenEdgeCol + 1).equals(" xx ")) { // check down edge
                    right_down = true;
                }
            }
            if(right && right_up && right_down){
                score = score + Integer.parseInt(String.valueOf(map.get(chosenEdgeRow).get(chosenEdgeCol + 1).charAt(2)));
            }
        }
        return score;
    }


}
//    ArrayList<ArrayList<String>> minMaxMap = boardMap.theCopy;
//    int[][][] nextMove = NextMove(minMaxMap, currentLevel, plys, aiScore, userScore, min, alphaBetaOn);
//
//    int[][] result = new int[2][2];
//        result[0] = nextMove[0][0];
//                result[1] = nextMove[0][1];
//                return result;
