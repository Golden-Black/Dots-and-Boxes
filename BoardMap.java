import java.util.ArrayList;
import java.util.Random;

public class BoardMap implements Cloneable {
    public int rows;
    public int cols;
    private int plys;
    public ArrayList<ArrayList<String>> theMap = new ArrayList<ArrayList<String>>();


    // constructor
    public BoardMap(int rows, int cols, int plys){
        this.rows = rows;
        this.cols = cols;
        this.plys = plys;
        // constructing the map
        for(int i = 0; i < 2 * rows + 1; i++){
            if(i % 2 == 0){
                theMap.add(generateRows_with_dots());
            }
            if(i % 2 == 1){
                theMap.add(generateRows_no_dots());
            }
        }
    }


    // what each row with dots looks like
    public ArrayList<String> generateRows_with_dots(){
        ArrayList<String> theRow_with_dots = new ArrayList<String>();
        for(int x = 0; x < 2 * cols + 1; x++){
            if(x % 2 == 0){
                theRow_with_dots.add("*");
            }
            if(x % 2 == 1){
                theRow_with_dots.add(" —— ");
            }
        }
        return theRow_with_dots;
    }


    // what each row without dots looks like
    public ArrayList<String> generateRows_no_dots() {
        ArrayList<String> theRow_no_dots = new ArrayList<String>();
        for (int y = 0; y < 2 * cols + 1; y++) {
            Random rd = new Random();
            if (y % 2 == 0) {
                theRow_no_dots.add("|");
            }
            if (y % 2 == 1) {
                String str = "  ";
                int random5 = rd.nextInt(5);
                theRow_no_dots.add(str + Integer.toString(random5 + 1) + " ");
            }
        }
        return theRow_no_dots;
    }


    // construct the column number for the dots
    // the very initial row with just numbers
    public void printMap(){
        // build the horizontal coordinate
        StringBuilder strX = new StringBuilder("  ");
        for(int b = 0; b < cols + 1; b++){
            strX.append(b).append("    ");
        }
        System.out.println(strX);

        // include the vertical coordinate
        int dotY = 0;
        for(int i = 0; i < 2 * rows + 1; i++){
            String strY = "";
            if(i % 2 == 0){
                strY = strY + Integer.toString(dotY) + " ";
                dotY = dotY + 1;
            }else{
                strY = strY + "  ";
            }

            for(int j = 0; j < 2 * cols + 1; j++){
                 strY += theMap.get(i).get(j);
            }
            System.out.println(strY);
        }
    }


    // Check to see if the map still has untaken edges such as "|" or " —— "
    // to determine if the game is completed.
    public boolean GameIsComplete(){
        for (ArrayList<String> strings : theMap) {
            if (strings.contains(" —— ") || strings.contains("|")) {
                return false;
            }
        }
        return true;
    }
}
