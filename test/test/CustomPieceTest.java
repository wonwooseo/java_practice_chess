package test;

import ChessLibrary.ChessBoard;
import ChessLibrary.Pieces.*;
import ChessLibrary.Util.IntPair;
import junit.framework.TestCase;

import java.util.List;

/**
 * Tests behavior of custom chess pieces.
 */
public class CustomPieceTest extends TestCase {
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLUMNS = 8;
    private ChessBoard testBoard;

    /**
     * Sets up testBoard.
     */
    public void testBoardSetup() {
        testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        testBoard.clearChessBoard();
        ChessPiece testBlackCharger = new Charger(0, 0, 3, 3);
        ChessPiece testWhiteLeaper = new Leaper(1, 0, 0, 0);
        ChessPiece testBlackPawn = new Pawn(0, 0, 0, 1);
        ChessPiece testBlackPawn1 = new Pawn(0, 1, 2, 2);
        ChessPiece testBlackPawn2 = new Pawn(0, 2, 4, 2);
        ChessPiece testBlackPawn3 = new Pawn(0, 3, 2, 4);
        ChessPiece testBlackPawn4 = new Pawn(0, 5, 3, 0);
        ChessPiece testWhiteRook = new Rook(1, 0, 1, 0);
        ChessPiece testBlackQueen = new Queen(0, 0, 0, 2);
        ChessPiece testWhitePawn = new Pawn(1, 0, 3, 7);
        ChessPiece testWhiteQueen = new Queen(1, 0, 7, 3);
        testBoard.setChessPiece(testBlackCharger, 3, 3);
        testBoard.setChessPiece(testWhiteLeaper, 0, 0);
        testBoard.setChessPiece(testBlackPawn, 0, 1);
        testBoard.setChessPiece(testBlackPawn1, 2, 2);
        testBoard.setChessPiece(testBlackPawn2, 4, 2);
        testBoard.setChessPiece(testBlackPawn3, 2, 4);
        testBoard.setChessPiece(testBlackPawn4, 3, 0);
        testBoard.setChessPiece(testWhiteRook, 1, 0);
        testBoard.setChessPiece(testBlackQueen, 0, 2);
        testBoard.setChessPiece(testWhitePawn, 3, 7);
        testBoard.setChessPiece(testWhiteQueen, 7, 3);
    }

    /**
     * Tests if movesListCreator doesn't return out-of-border moves for charger.
     */
    public void testChargerBorderMoves() {
        testBoardSetup();
        ChessPiece testCharger = testBoard.getChessPieceById("0c0");
        List<IntPair> movesList = testBoard.getMoves(testCharger);
        assertEquals(5, movesList.size());
        assertEquals(7, movesList.get(1).left());
        assertEquals(7, movesList.get(1).right());
        assertEquals(0, movesList.get(2).left());
        assertEquals(3, movesList.get(2).right());
    }

    /**
     * Tests if movesListCreator properly returns capture moves for charger.
     */
    public void testChargerCaptureMoves() {
        testBoardSetup();
        ChessPiece testCharger = testBoard.getChessPieceById("0c0");
        List<IntPair> movesList = testBoard.getMoves(testCharger);
        assertEquals(5, movesList.size());
        assertEquals(7, movesList.get(0).left());
        assertEquals(3, movesList.get(0).right());
        assertEquals(3, movesList.get(3).left());
        assertEquals(7, movesList.get(3).right());
    }

    /**
     * Tests if movesListCreator properly handles collision with allied piece.
     */
    public void testChargerOccupiedMoves() {
        testBoardSetup();
        ChessPiece testCharger = testBoard.getChessPieceById("0c0");
        List<IntPair> movesList = testBoard.getMoves(testCharger);
        assertEquals(5, movesList.size());
        assertEquals(3, movesList.get(4).left());
        assertEquals(1, movesList.get(4).right());
    }

    /**
     * Tests if movesListCreator returns proper moves for leaper custom piece.
     */
    public void testLeaperMovesList() {
        testBoardSetup();
        ChessPiece testLeaper = testBoard.getChessPieceById("1l0");
        List<IntPair> movesList = testBoard.getMoves(testLeaper);
        assertEquals(1, movesList.size());
        assertEquals(0, movesList.get(0).left());
        assertEquals(2, movesList.get(0).right());
    }
}
