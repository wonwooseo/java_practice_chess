package test;

import ChessLibrary.*;
import ChessLibrary.Pieces.*;
import ChessLibrary.Util.IntPair;
import junit.framework.TestCase;

import java.util.List;

/**
 * Tests check / checkmate detection related methods.
 */
public class CheckMateTest extends TestCase {
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLUMNS = 8;
    private ChessBoard testBoard;

    /**
     * Sets up testBoard with status of black being checkmated.
     */
    public void testBoardSetup() {
        testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        testBoard.clearChessBoard();
        ChessPiece testBlackKing = new King(0, 0, 7, 3);
        ChessPiece testWhiteKing = new King(1, 0, 5, 3);
        ChessPiece testWhiteRook = new Rook(1, 0, 7, 7);
        testBoard.setChessPiece(testBlackKing, 7, 3);
        testBoard.setChessPiece(testWhiteKing, 5, 3);
        testBoard.setChessPiece(testWhiteRook, 7,7);
    }

    /**
     * Sets up testBoard with second case of checkmate.
     */
    public void testBoard2Setup() {
        testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        testBoard.clearChessBoard();
        ChessPiece testBlackKing = new King(0, 0, 7, 1);
        ChessPiece testBlackPawn = new Pawn(0, 0, 6, 1);
        ChessPiece testWhiteKnight = new Knight(1, 0, 4, 1);
        ChessPiece testWhiteRook = new Rook(1, 0, 7, 3);
        testBoard.setChessPiece(testBlackKing, 7, 1);
        testBoard.setChessPiece(testBlackPawn, 6, 1);
        testBoard.setChessPiece(testWhiteKnight, 4, 1);
        testBoard.setChessPiece(testWhiteRook, 7,3);
    }

    /**
     * Sets up game to fool's mate condition and returns current ChessBoard.
     */
    public ChessBoard foolsMateSetup() {
        Game testGame = new Game(BOARD_ROWS, BOARD_COLUMNS, false);
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
        return testGame.getBoard();
    }

    /**
     * Tests if testCheckIfCheck method returns expected list of attackers.
     */
    public void testCheckIfCheck() {
        testBoardSetup();
        List<ChessPiece> attackerList = testBoard.checkIfCheck(0);
        assertTrue(attackerList.size() > 0);
        assertEquals(testBoard.getChessPiece(7, 7), attackerList.get(0));
    }

    /**
     * Tests if testCheckDefendAttack method returns false.
     */
    public void testCheckDefendAttack() {
        testBoardSetup();
        assertFalse(testBoard.checkDefendAttack(0));
    }

    /**
     * Test of checkCheckStatus method in other checkmate conditions.
     */
    public void testCheckCheckStatus() {
        testBoardSetup();
        assertEquals(2, testBoard.checkCheckStatus(0));
        foolsMateSetup();
        assertEquals(2, testBoard.checkCheckStatus(0));
        testBoard2Setup();
        assertEquals(2, testBoard.checkCheckStatus(0));
    }

}
