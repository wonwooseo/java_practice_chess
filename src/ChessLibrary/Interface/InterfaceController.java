package ChessLibrary.Interface;

import ChessLibrary.ChessBoard;
import ChessLibrary.Game;
import ChessLibrary.Pieces.ChessPiece;
import ChessLibrary.Util.IntPair;
import ChessLibrary.Util.TurnData;

import java.util.Stack;

/**
 * Class of application features that do not involve GUI manipulation inside their logic.
 * @author Wonwoo Seo (wonwooseo@hotmail.com)
 */
public class InterfaceController {
    // private variables for score managing.
    private String blackName;
    private String whiteName;
    private int blackScore;
    private int whiteScore;
    // stacks for undo feature
    private Stack<TurnData> blackStack;
    private Stack<TurnData> whiteStack;

    /**
     * Default constructor for InterfaceController.
     */
    public InterfaceController() {
        blackName = "Black";
        whiteName = "White";
        blackScore = 0;
        whiteScore = 0;
        blackStack = new Stack<>();
        whiteStack = new Stack<>();
    }

    /**
     * Gets player name of black side.
     * @return Player name of black side.
     */
    public String getBlackName() {
        return blackName;
    }

    /**
     * Gets player name of white side.
     * @return Player name of white side.
     */
    public String getWhiteName() {
        return whiteName;
    }

    /**
     * Sets player name of black side.
     * @param name Name to set.
     */
    public void setBlackName(String name) {
        blackName = name;
    }

    /**
     * Sets player name of white side.
     * @param name Name to set.
     */
    public void setWhiteName(String name) {
        whiteName = name;
    }

    /**
     * Gets current score of black side.
     * @return Score of black side.
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * Gets current score of white side.
     * @return Score of white side.
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /**
     * Increments score of black side by 1.
     */
    public void incrementBlackScore() {
        blackScore += 1;
    }

    /**
     * Increments score of white side by 1.
     */
    public void incrementWhiteScore() {
        whiteScore += 1;
    }

    /**
     * Push given TurnData item into blackStack.
     * @param item TurnData object to push in.
     */
    public void pushBlackStack(TurnData item) {
        blackStack.push(item);
    }

    /**
     * Push given TurnData item into whiteStack.
     * @param item TurnData object to push in.
     */
    public void pushWhiteStack(TurnData item) {
        whiteStack.push(item);
    }

    /**
     * Checks if blackStack is empty.
     * @return True if empty, false if not.
     */
    public boolean isBlackStackEmpty() {
        return blackStack.isEmpty();
    }

    /**
     * Checks if whiteStack is empty.
     * @return True if empty, false if not.
     */
    public boolean isWhiteStackEmpty() {
        return whiteStack.isEmpty();
    }

    /**
     * Resets stacks.
     */
    public void resetStacks() {
        blackStack = null;
        blackStack = new Stack<>();
        whiteStack = null;
        whiteStack = new Stack<>();
    }

    /**
     * Starts a new game. This method does not affect scores.
     * @param rows Number of rows of the chessboard.
     * @param cols Number of columns of the chessboard.
     * @param customPiece If game should use custom pieces.
     * @return New Game object.
     */
    public Game startNewGame(int rows, int cols, boolean customPiece) {
        Game newGame = new Game(rows, cols, customPiece);
        return newGame;
    }

    /**
     * Starts a new game. This method does affect scores.
     * Last game will be counted as loss of calling side.
     * @param rows Number of rows of the chessboard.
     * @param cols Number of columns of the chessboard.
     * @param side Side calling this method.
     * @param customPiece If game should use custom pieces.
     * @return New Game object.
     */
    public Game forfeit(int rows, int cols, int side, boolean customPiece) {
        if (side == 0) {
            incrementWhiteScore();
            return startNewGame(rows, cols, customPiece);
        } else {
            incrementBlackScore();
            return startNewGame(rows, cols, customPiece);
        }
    }

    /**
     * Returns current game state to last turn.
     * @param side Current side. (side calling undo)
     * @param board Current ChessBoard object.
     * @return
     */
    public void undo(int side, ChessBoard board) {
        int TURN_BLACK = 0;
        int TURN_WHITE = 1;
        TurnData turnItem;
        if(side == TURN_BLACK) {
            turnItem = whiteStack.pop();
            undoPieceSetter(turnItem, board);
            turnItem = blackStack.pop();
            undoPieceSetter(turnItem, board);
        } else {
            turnItem = blackStack.pop();
            undoPieceSetter(turnItem, board);
            turnItem = whiteStack.pop();
            undoPieceSetter(turnItem, board);
        }
    }

    /**
     * Helper function for undo method to set pieces on chessboard.
     * @param turnItem TurnData object which holds information about moved / captured pieces
     * @param currentBoard ChessBoard object.
     */
    private void undoPieceSetter(TurnData turnItem, ChessBoard currentBoard) {
        ChessPiece movedPiece = turnItem.getMovedPiece();
        ChessPiece capturedPiece = turnItem.getCapturedPiece();
        IntPair originalPosition = turnItem.getOriginalPosition();
        IntPair newPosition = turnItem.getNewPosition();
        currentBoard.deleteChessPiece(newPosition.left(), newPosition.right());
        if(capturedPiece != null) {
            currentBoard.setChessPiece(capturedPiece, newPosition.left(), newPosition.right());
        }
        currentBoard.setChessPiece(movedPiece, originalPosition.left(), originalPosition.right());
    }
}
