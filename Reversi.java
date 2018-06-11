import java.util.*;

class State {
    char[] board;

    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
    }


    public boolean isTerminal() {

        // TO DO: determine if the board is a terminal node or not and return boolean
        if(getSuccessors('1').length == 0 && getSuccessors('2').length == 0){
            return true;
        }
        else return false;
    }


    public int getScore(){
        int countDark = 0;
        int countLight = 0;
        for (int i = 0; i < 16; i++) {
            if (board[i] == '1'){
                countDark++;
            }
            else if(board[i] == '2'){
                countLight++;
            }
        }
        if(countDark == countLight){
            return 0;
        }
        else if(countDark > countLight){
            return 1;
        }
        else return -1;
    }

    /**
     * plot board into a 4x4 2D array
     * */
    public char[][] plotBoard(){
        char[][] plot = new char[4][4];
        for (int i = 0; i < board.length; i++) {
            plot[i / 4][i % 4] = board[i];
        }
        return plot;
    }

    /**
     * transfer a 4x4 2D array back to a valid state
     * */
    public State toState(char[][] plot){
        char[] tempBoard = new char[16];
        int index = 0;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                tempBoard[index] = plot[r][c];
                index++;
            }
        }
        State tempState = new State(tempBoard);
        return tempState;
    }

    /**
     * get the opponent player
     * */
    public char oppoPlayer(char player){
        if(player == '1'){
            return '2';
        }
        else {
            return '1';
        }
    }


    /**
     * get a list of successors of the current state
     * */
    public State[] getSuccessors(char player) {

        // TO DO: get all successors and return them in proper order
        List<State> successorsList = new ArrayList<>();
        //plot the board in 2D array
        char[][] plot = plotBoard();
        //8 different directions to check for every spot in the board
        int rowDirection[] = {-1,0,1,1,1,0,-1,-1};
        int colDirection[] = {1,1,1,0,-1,-1,-1,0};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //once find a blank space, check 8 directions of the blank space
                if(plot[i][j] == '0'){
                    boolean found = false;
                    for (int k = 0; k < 8 && !found; k++) {
                        //check one direction at a time
                        int curRow = i + rowDirection[k];
                        int curCol = j + colDirection[k];
                        //if find a spot with valid row and column and with the color of opponent player
                        while(curRow + rowDirection[k] >= 0 && curRow + rowDirection[k] < 4 && curCol + colDirection[k] >= 0
                                && curCol + colDirection[k] < 4 && plot[curRow][curCol] == oppoPlayer(player))
                        {
                            //continue check along the direction
                            //call modifyState() to flip along the direction
                            curRow += rowDirection[k];
                            curCol += colDirection[k];
                            if(plot[curRow][curCol] == player){
                                boolean duplicate = false;
                                found = true;
                                char[][] tempPlot = plotBoard();
                                tempPlot = modifyState(tempPlot,i,j,player);
                                State successor = toState(tempPlot);
                                //check if the state already exist in the list
                                for (State s: successorsList
                                        ) {
                                    if(s.equals(successor)){
                                        duplicate = true;
                                        break;
                                    }
                                }
                                //if not, add to the list
                                if(!duplicate){
                                    successorsList.add(successor);
                                }
                            }
                        }
                    }
                }
            }
        }
        State[] successors = new State[successorsList.size()];
        successors = successorsList.toArray(successors);
        return successors;
    }

    /**
     * when get a valid successor, check the 8 directions again to explore more flips.
     * */
    public char[][] modifyState(char[][] plot, int x, int y, char player){
        char[][] tempPlot = plot;
        tempPlot[x][y] = player;
        int rowDirection[] = {-1,0,1,1,1,0,-1,-1};
        int colDirection[] = {1,1,1,0,-1,-1,-1,0};
        //check 8 directions
        for (int i = 0; i < rowDirection.length; i++) {
            int curRow = x + rowDirection[i];
            int curCol = y + colDirection[i];
            while(curRow + rowDirection[i] >= 0 && curRow + rowDirection[i] < 4 && curCol + colDirection[i] >= 0
                    && curCol + colDirection[i] < 4 && tempPlot[curRow][curCol] == oppoPlayer(player))
            {
                //check along the direction to explore more flips
                curRow += rowDirection[i];
                curCol += colDirection[i];
                if(tempPlot[curRow][curCol] == player){
                    int flipRow = x + rowDirection[i];
                    int flipCol = y + colDirection[i];
                    while(flipRow != curRow || flipCol != curCol){
                        tempPlot[flipRow][flipCol] = player;
                        flipRow += rowDirection[i];
                        flipCol += colDirection[i];
                    }
                    break;
                }
            }
        }
        return tempPlot;
    }


    public void printState(int option, char player) {

        // TO DO: print a State based on option (flag)
        if(option == 1){
            State[] successors = this.getSuccessors(player);
            for (State s: successors
                    ) {
                System.out.println(s.getBoard());
            }
        }
        else if(option == 2){
            if(!isTerminal()){
                System.out.println("non-terminal");
            }
            else {
                System.out.println(getScore());
            }
        }

        else if(option == 3){
            Minimax game = new Minimax();
            System.out.println("result: "+ game.run(this, player));
            System.out.println("count: " + game.globalCounter);
        }

        else if(option == 4 || option == 6){
            State[] states = getSuccessors(player);
            int optimalValue;
            if(player == '1'){
                optimalValue = -2;
            }
            else optimalValue = 2;
            String optimalSuccessor = "";
            for (State s: states
                    ) {
                Minimax game = new Minimax();
                int result;
                if(option == 4) {
                    result = game.run(s, player);
                }
                else {
                    result = game.run_with_pruning(s, player);
                }
                if(player == '1') {
                    if (result == 1) {
//                        optimalValue = result;
                        optimalSuccessor = s.getBoard();
                        break;
                    }
                    if (result > optimalValue) {
                        optimalValue = result;
                        optimalSuccessor = s.getBoard();
                    }
                }
                else {
                    if (result == -1) {
//                        optimalValue = result;
                        optimalSuccessor = s.getBoard();
                        break;
                    }
                    if (result < optimalValue) {
                        optimalValue = result;
                        optimalSuccessor = s.getBoard();
                    }
                }
            }
            System.out.println(optimalSuccessor);
        }

        else if (option == 5){
            Minimax game = new Minimax();
            System.out.println(game.run_with_pruning(this,player));
            System.out.println(game.globalCounter);
        }

    }

    public String getBoard() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(this.board[i]);
        }
        return builder.toString().trim();
    }

    public boolean equals(State src) {
        for (int i = 0; i < 16; i++) {
            if (this.board[i] != src.board[i])
                return false;
        }
        return true;
    }
}

class Minimax {
    static int globalCounter = 0;
    private static int max_value(State curr_state) {

        // TO DO: implement Max-Value of the Minimax algorithm
        globalCounter++;
        if(curr_state.isTerminal()){
            return curr_state.getScore();
        }
        else{
            int alpha = Integer.MIN_VALUE;
            State[] successors = curr_state.getSuccessors('1');
            if(successors.length == 0){
                return min_value(curr_state);
            }
            for (State s: successors
                    ) {
                int curResult = min_value(s);
                if(curResult >= alpha){
                    alpha = curResult;
                }
            }
            return alpha;
        }
    }

    private static int min_value(State curr_state) {

        // TO DO: implement Min-Value of the Minimax algorithm
        globalCounter++;
        if(curr_state.isTerminal()){
            return curr_state.getScore();
        }
        else{
            int beta = Integer.MAX_VALUE;
            State[] successors = curr_state.getSuccessors('2');
            if(successors.length == 0){
                return max_value(curr_state);
            }
            for (State s: successors
                    ) {
                int curResult = max_value(s);
                if(curResult <= beta){
                    beta = curResult;
                }
            }
            return beta;
        }
    }

    private static int max_value_with_pruning(State curr_state, int alpha, int beta) {

        // TO DO: implement Max-Value of the alpha-beta pruning algorithm
        globalCounter++;
        if(curr_state.isTerminal()){
            return curr_state.getScore();
        }
        else{
            State[] successors = curr_state.getSuccessors('1');
            if(successors.length == 0){
                return min_value_with_pruning(curr_state, alpha, beta);
            }
            for (State s: successors
                    ) {
                int curResult = min_value_with_pruning(s, alpha, beta);
                if(curResult >= alpha){
                    alpha = curResult;
                }
                if(alpha >= beta) return beta;
            }
            return alpha;
        }

    }

    private static int min_value_with_pruning(State curr_state, int alpha, int beta) {

        // TO DO: implement Min-Value of the alpha-beta pruning algorithm
        globalCounter++;
        if(curr_state.isTerminal()){
            return curr_state.getScore();
        }
        else{
            State[] successors = curr_state.getSuccessors('2');
            if(successors.length == 0){
                return max_value_with_pruning(curr_state, alpha, beta);
            }
            for (State s: successors
                    ) {
                int curResult = max_value_with_pruning(s, alpha, beta);
                if(curResult <= beta){
                    beta = curResult;
                }
                if(alpha >= beta) return alpha;
            }
            return beta;
        }
    }

    public static int run(State curr_state, char player) {

        // TO DO: run the Minimax algorithm and return the game theoretic value
        if(player == '1'){
            return max_value(curr_state);
        }
        else return min_value(curr_state);
    }

    public static int run_with_pruning(State curr_state, char player) {

        // TO DO: run the alpha-beta pruning algorithm and return the game theoretic value
        if(player == '1'){
            return max_value_with_pruning(curr_state, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        else return min_value_with_pruning(curr_state, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}

public class Reversi {
    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        char[] board = new char[16];
        for (int i = 0; i < 16; i++) {
            board[i] = args[2].charAt(i);
        }
        int option = flag / 100;
        char player = args[1].charAt(0);
        if ((player != '1' && player != '2') || args[1].length() != 1) {
            System.out.println("Invalid Player Input");
            return;
        }
        State init = new State(board);
        init.printState(option, player);
    }
}
