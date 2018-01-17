package test;

import ChessLibrary.*;
import ChessLibrary.Pieces.ChessPiece;
import ChessLibrary.Pieces.King;
import ChessLibrary.Pieces.Queen;
import ChessLibrary.Util.IntPair;
import junit.framework.TestCase;

import java.util.List;

/**
 * Tests move interface and game end conditions.
 * Currently using temporary text interface;
 * tests for error cases will be added after GUI is implemented.
 */
public class GameTest extends TestCase {
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLUMNS = 8;
    Game testGame;

    /**
     * Sets up game to default settings.
     */
    public void testGameSetup() {
        testGame = new Game(BOARD_ROWS, BOARD_COLUMNS, false);
    }

    /**
     * Tests if getTurn properly returns current turn.
     */
    public void testGetTurn() {
        testGameSetup();
        assertEquals(1, testGame.getTurn());
    }

    /**
     * Tests if getMovesInterface method properly returns possible moves.
     */
    public void testGetMovesInterface() {
        testGameSetup();
        List<IntPair> movesList = testGame.getMovesInterface("1p0");
        assertEquals(0, movesList.get(0).left());
        assertEquals(5, movesList.get(0).right());
        assertEquals(0, movesList.get(1).left());
        assertEquals(4, movesList.get(1).right());
    }

    /**
     * Tests if movePieceInterface properly sets piece on destination and empties original position.
     */
    public void testMovePieceInterface() {
        testGameSetup();
        List<IntPair> movesList = testGame.getMovesInterface("1k0");
        testGame.movePieceInterface(0, movesList, "1k0");
        ChessPiece testPiece = testGame.getBoard().getChessPieceById("1k0");
        assertEquals(2, testPiece.getPosition().left());
        assertEquals(5, testPiece.getPosition().right());
        assertNull(testGame.getBoard().getChessPiece(1, 7));
    }

    /**
     * Tests if game is normally continuing. (No check / checkmate / stalemate)
     */
    public void testCheckStatusNothing() {
        testGameSetup();
        List<IntPair> movesList = testGame.getMovesInterface("1p4");
        testGame.movePieceInterface(1, movesList, "1p4");
        testGame.nextTurn();
        movesList = testGame.getMovesInterface("0p3");
        testGame.movePieceInterface(0, movesList, "0p3");
        testGame.nextTurn();
        assertEquals(0, testGame.getCheckStatus());
        assertEquals(3, testGame.checkGameEnd());
    }

    /**
     * Tests if check condition is properly detected and game continues.
     */
    public void testCheckStatusCheck() {
        testGameSetup();
        List<IntPair> movesList = testGame.getMovesInterface("1p4");
        testGame.movePieceInterface(1, movesList, "1p4");
        testGame.nextTurn();
        movesList = testGame.getMovesInterface("0p3");
        testGame.movePieceInterface(0, movesList, "0p3");
        testGame.nextTurn();
        movesList = testGame.getMovesInterface("1b1");
        testGame.movePieceInterface(3, movesList, "1b1");
        testGame.nextTurn();
        testGame.updateCheckStatus();
        assertEquals(1, testGame.getCheckStatus());
        assertEquals(3, testGame.checkGameEnd());
    }

    /**
     * Tests if checkmate condition is properly detected in checkmate condition and game properly ends.
     */
    public void testCheckStatusCheckmate() {
        testGameSetup();
        List<IntPair> movesList = testGame.getMovesInterface("1p5");
        testGame.movePieceInterface(0, movesList, "1p5");
        testGame.nextTurn();
        movesList = testGame.getMovesInterface("0p4");
        testGame.movePieceInterface(1, movesList, "0p4");
        testGame.nextTurn();
        movesList = testGame.getMovesInterface("1p6");
        testGame.movePieceInterface(1, movesList, "1p6");
        testGame.nextTurn();
        movesList = testGame.getMovesInterface("0Q0");
        testGame.movePieceInterface(3, movesList, "0Q0");
        testGame.nextTurn();
        testGame.updateCheckStatus();
        assertEquals(2, testGame.getCheckStatus());
        assertEquals(0, testGame.checkGameEnd());
    }

    /**
     * Tests if stalemate condition is properly detected and game properly ends.
     */
    public void testCheckStatusStalemate() {
        ChessBoard testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        testBoard.clearChessBoard();
        ChessPiece testBlackKing = new King(0, 0, 7, 0);
        ChessPiece testWhiteKing = new King(1, 0, 5, 1);
        ChessPiece testWhiteQueen = new Queen(1, 0, 6, 2);
        testBoard.setChessPiece(testBlackKing, 7, 0);
        testBoard.setChessPiece(testWhiteKing, 5, 1);
        testBoard.setChessPiece(testWhiteQueen, 6,2);
        testGame = new Game(0, testBoard);
        testGame.updateCheckStatus();
        assertEquals(3, testGame.getCheckStatus());
        assertEquals(2, testGame.checkGameEnd());
    }
}