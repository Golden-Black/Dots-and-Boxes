import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        DotsAndBoxes game = new DotsAndBoxes();
        game.start(setRows(), setCols(), setPlys(), setFirstMove());
    }

    // ask user the row size of the board
    public static int setRows() throws IOException {
        System.out.println("Please specify the number of rows of the board: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String strRow = br.readLine();
        while(Integer.parseInt(strRow) < 1){
            System.out.println("The row number must be greater or equal to 1!");
            System.out.println("Please re-specify the number of rows of the board: ");
            BufferedReader br_temp = new BufferedReader(new InputStreamReader(System.in));
            strRow = br_temp.readLine();
        }
        return Integer.parseInt(strRow);
    }

    // ask user the column size of the board
    public static int setCols() throws IOException {
        System.out.println("Please specify the number of columns of the board: ");
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String strCol = br1.readLine();
        while(Integer.parseInt(strCol) < 1){
            System.out.println("The column number must be greater or equal to 1!");
            System.out.println("Please re-specify the number of rows of the board: ");
            BufferedReader br_temp = new BufferedReader(new InputStreamReader(System.in));
            strCol = br_temp.readLine();
        }
        return Integer.parseInt(strCol);
    }

    // ask user about the plys
    public static int setPlys() throws IOException {
        System.out.println("Please enter the number of plys: ");
        BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        String strPly = br2.readLine();
        return Integer.parseInt(strPly);
    }

    // deciding who goes first
    public static boolean setFirstMove() throws IOException {
        boolean userMove = true;
        System.out.println("Do you want to make the first move? Please type in 'y' or 'n'. (y/n)");
        BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
        String firstMove = br3.readLine();
        while(!firstMove.equals("y") && !firstMove.equals("n")){
            System.out.println("Do you want to make the first move? Please type in 'y' or 'n'. (y/n)");
            BufferedReader br4 = new BufferedReader(new InputStreamReader(System.in));
            firstMove = br4.readLine();
        }

        if(firstMove.equals("y")){
            System.out.println("You will start first.");
        }else{
            System.out.println("AI will start first.");
            userMove = false;
        }
        System.out.println("Tip: '——' or '|' means the edge has not been taken yet. \n 'x' or ' xx ' indicates the edge has been taken.\n");
        return userMove;
    }
}
