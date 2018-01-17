package test;

import ChessLibrary.*;
import ChessLibrary.Pieces.*;
import junit.framework.TestCase;

/**
 * Tests stalemate detection method for different cases of stalemate.
 */
public class StaleMateTest extends TestCase {
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLUMNS = 8;
    private ChessBoard testBoard;

    /**
     * Sets up testBoard with status of stalemate.
     */
    public void testBoardSetup() {
        testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        testBoard.clearChessBoard();
        ChessPiece testBlackKing = new King(0, 0, 7, 0);
        ChessPiece testWhiteKing = new King(1, 0, 5, 1);
        ChessPiece testWhiteQueen = new Queen(1, 0, 6, 2);
        testBoard.setChessPiece(testBlackKing, 7, 0);
        testBoard.setChessPiece(testWhiteKing, 5, 1);
        testBoard.setChessPiece(testWhiteQueen, 6,2);
    }

    /**
     * Sets up testBoard with second case of stalemate.
     */
    public void testBoard2Setup() {
        testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        testBoard.clearChessBoard();
        ChessPiece testBlackKing = new King(0, 0, 5, 0);
        ChessPiece testWhiteKing = new King(1, 0, 5, 2);
        ChessPiece testWhitePawn = new Pawn(1, 0, 5, 1);
        testBoard.setChessPiece(testBlackKing, 5, 0);
        testBoard.setChessPiece(testWhiteKing, 5, 2);
        testBoard.setChessPiece(testWhitePawn, 5,1);
    }

    /**
     * Sets up testBoard with third case of stalemate.
     */
    public void testBoard3Setup() {
        testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        testBoard.clearChessBoard();
        ChessPiece testBlackKing = new King(0, 0, 0, 0);
        ChessPiece testBlackBishop = new Bishop(0, 0, 1, 0);
        ChessPiece testWhiteKing = new King(1, 0, 1, 2);
        ChessPiece testWhiteRook = new Rook(1, 0, 7, 0);
        testBoard.setChessPiece(testBlackKing, 0, 0);
        testBoard.setChessPiece(testBlackBishop, 1, 0);
        testBoard.setChessPiece(testWhiteKing, 1, 2);
        testBoard.setChessPiece(testWhiteRook, 7,0);
    }

    /**
     * Runs test with 3 different cases of stalemate.
     */
    public void testCheckStaleMate() {
        testBoardSetup();
        assertTrue(testBoard.checkStaleMate(0));
        testBoard2Setup();
        assertTrue(testBoard.checkStaleMate(0));
        testBoard3Setup();
        assertTrue(testBoard.checkStaleMate(0));
    }
}