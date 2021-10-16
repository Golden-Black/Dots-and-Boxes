import java.util.HashMap;
import java.util.Random;

public class AI {
    public int score;

    public AI(int score) {
        this.score = score;
    }

    public int[][] MiniMax(BoardMap boardMap, int currentLevel, int plys, int aiScore, boolean alphaBetaOn, boolean min, int[][] zeroPly, int userScore){
        if(plys == 0){
            return zeroPly;
        }

        if(currentLevel == plys + 1){ // return the minimum value on this level
            // search available moves
            return SearchMoves(boardMap, min, aiScore, userScore);
        }

        int[][] nextMv = new int[2][2];
        nextMv = SearchMoves(boardMap, min, aiScore, userScore);
        return MiniMax(boardMap, currentLevel++, plys, aiScore, alphaBetaOn, !min, zeroPly, userScore);
    }



    public int[][] SearchMoves(BoardMap boardMap, boolean min, int aiScore, int userScore) {
        HashMap<Integer, int[][]> availableEdges = new HashMap<Integer, int[][]>();

        for(int i = 0; i < 2 * boardMap.rows + 1; i++){
            for(int j = 0; j < 2 * boardMap.cols + 1; j++){
                if(boardMap.theMap.get(i).get(j).equals(" —— ")){
                    int[][] dots = new int[2][2];

                    dots[0][0] = i/2;        // leftDotRow
                    dots[0][1] = (j - 1)/2;  // leftDotCol
                    dots[1][0] = i/2;        // rightDotRow
                    dots[1][1] = (j + 1)/2;  // rightDotCol

                    int e = ScoringFunction(boardMap, dots, aiScore, userScore);
                    availableEdges.put(e, dots);
                }
                if(boardMap.theMap.get(i).get(j).equals("|")){
                    int[][] dots = new int[2][2];

                    dots[0][0] = (i - 1)/2;  // upperDotRow
                    dots[0][1] = j / 2;      // upperDotCol
                    dots[1][0] = (i + 1)/2;    // downDotRow
                    dots[1][1] = j / 2;          // downDotCol

                    int e = ScoringFunction();
                    availableEdges.put(e, dots);
                }
            }
        }
        int next = 0;
        if(min){
            int smallest = Integer.MAX_VALUE;
            for(int num: availableEdges.keySet()){
                smallest = Math.min(smallest, num);
            }
            next = smallest;
        }else{
            int max = Integer.MIN_VALUE;
            for(int num: availableEdges.keySet()){
                max = Math.max(max, num);
            }
            next = max;
        }
        return availableEdges.get(next);
    }

    private int ScoringFunction(BoardMap boardMap, int[][] dots, int aiScore, int userScore) {


        return aiScore - userScore;
    }
}