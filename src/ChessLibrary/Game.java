package ChessLibrary;

import ChessLibrary.Pieces.ChessPiece;
import ChessLibrary.Util.IntPair;

import java.util.List;

/**
 * ChessLibrary.Game --- Implementation of general game related features(turn, check of game end)
 * @author Wonwoo Seo (wonwooseo@hotmail.com)
 */
public class Game {
    private int turn;
    private static int BLACK = 0;
    private static int WHITE = 1;

    /**
     * checkStatus is a value returned from ChessBoard.checkCheckStatus;
     * 0 for nothing, 1 for check, 2 for checkmate and 3 for stalemate.
     */
    private int checkStatus;
    private ChessBoard BOARD;

    static int BLACK_WIN = 0;
    static int WHITE_WIN = 1;
    static int DRAW = 2;
    static int CONTINUE = 3;

    /**
     * ChessLibrary.Game Constructor. Sets up the game to initial state.
     * @param rows    Number of rows of the chess board.
     * @param columns Number of columns of the chess board.
     * @param customPiece Whether custom pieces should be used.
     */
    public Game(int rows, int columns, boolean customPiece) {
        turn = WHITE;
        BOARD = new ChessBoard(rows, columns, customPiece);
    }

    /**
     * Constructor to create game from existing chessboard.
     * @param turn   Current turn.
     * @param source Existing chessboard to start game on.
     */
    public Game(int turn, ChessBoard source) {
        this.turn = turn;
        BOARD = source;
    }

    /**
     * Returns current ChessBoard object.
     * @return Current ChessBoard object.
     */
    public ChessBoard getBoard() {
        return BOARD;
    }

    /**
     * Returns value of current turn. 0 if Black's turn, 1 if White's turn.
     * @return Current turn.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Returns value of checkStatus.
     * @return Current value of checkStatus
     */
    public int getCheckStatus() {
        return checkStatus;
    }

    /**
     * Checks if game has ended.
     * @return 0 - Black wins,
     * 1 - White wins,
     * 2 - Draw,
     * 3 - Game is not finished
     */
    public int checkGameEnd() {
        if (checkStatus == 2) {
            if (turn == 0) {
                return WHITE_WIN;
            } else {
                return BLACK_WIN;
            }
        } else if (checkStatus == 3) {
            return DRAW;
        }
        return CONTINUE;
    }

    /**
     * Sets checkStatus value.
     * @param status value to set.
     * @return No return value.
     */
    public void setCheckStatus(int status) {
        checkStatus = status;
    }

    /**
     * Changes turn value. Currently flips between 0(black) and 1(white).
     * @return No return value.
     */
    public void nextTurn() {
        if (turn == WHITE) {
            turn = BLACK;
        } else {
            turn = WHITE;
        }
    }

    /**
     * Interface for getting possible moves of given piece. Moves that result in being checked are filtered out here.
     * @param id Unique identifier of the ChessLibrary.Pieces.ChessPiece object.
     * @return List of possible moves of ChessLibrary.Pieces.ChessPiece object with given identifier.
     */
    public List<IntPair> getMovesInterface(String id) {
        ChessPiece selectedPiece = BOARD.getChessPieceById(id);
        if (selectedPiece == null) {
            return null;
        }
        if (selectedPiece.getSide() != turn) {
            return null;
        }
        List<IntPair> movesList = BOARD.getMoves(selectedPiece);
        if (movesList.size() == 0) {
            return null;
        }
        for (int count = 0; count < movesList.size(); count++) {
            IntPair currentPosition = selectedPiece.getPosition();
            IntPair destination = movesList.get(count);
            // Temporarily move piece to destination
            BOARD.deleteChessPiece(currentPosition.left(), currentPosition.right());
            ChessPiece deleted = BOARD.getChessPiece(destination.left(), destination.right());
            BOARD.setChessPiece(selectedPiece, destination.left(), destination.right());
            // Get list of attackers
            List<ChessPiece> attackerList = BOARD.checkIfCheck(selectedPiece.getSide());
            // Undo move
            BOARD.deleteChessPiece(destination.left(), destination.right());
            if(deleted != null) {
                BOARD.setChessPiece(deleted, destination.left(), destination.right());
            }
            BOARD.setChessPiece(selectedPiece, currentPosition.left(), currentPosition.right());
            // Remove illegal moves
            if(attackerList.size() > 0) {
                movesList.remove(count);
                count -= 1;
                continue;
            }
        }

        return movesList;
    }

    /**
     * Interface for moving pieces.
     * @param entry     Index of destination in movesList.
     * @param movesList List of possible moves returned by getMovesInterface method.
     * @param id        Unique identifier of the ChessLibrary.Pieces.ChessPiece object to move.
     * @return No return value.
     */
    public void movePieceInterface(int entry, List<IntPair> movesList, String id) {
        ChessPiece selectedPiece = BOARD.getChessPieceById(id);
        IntPair currentPosition = selectedPiece.getPosition();
        IntPair targetPosition = movesList.get(entry);
        BOARD.deleteChessPiece(currentPosition.left(), currentPosition.right());
        BOARD.setChessPiece(selectedPiece, targetPosition.left(), targetPosition.right());
    }

    /**
     * Updates checkStatus value. Must be called after nextTurn().
     * @return No return value.
     */
    public void updateCheckStatus() {
        setCheckStatus(BOARD.checkCheckStatus(turn));
    }
}