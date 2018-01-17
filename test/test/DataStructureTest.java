package test;

import ChessLibrary.*;
import ChessLibrary.Pieces.ChessPiece;
import ChessLibrary.Pieces.King;
import ChessLibrary.Pieces.Rook;
import ChessLibrary.Util.IntPair;
import junit.framework.TestCase;

/**
 * Test Class to check data structure related functions of ChessLibrary.
 */
public class DataStructureTest extends TestCase {
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_COLUMNS = 8;
    private ChessBoard testBoard;

    /**
     * Sets up testBoard with 2 Kings and 1 Rook present.
     */
    public void testBoardSetup() {
        testBoard = new ChessBoard(BOARD_ROWS, BOARD_COLUMNS, false);
        for(int index_y = 0; index_y < BOARD_ROWS; index_y++) {
            for(int index_x = 0; index_x < BOARD_COLUMNS; index_x++) {
                testBoard.deleteChessPiece(index_x, index_y);
            }
        }
        ChessPiece testBlackKing = new King(0, 0, 7, 3);
        ChessPiece testWhiteKing = new King(1, 0, 2, 3);
        ChessPiece testWhiteRook = new Rook(1, 0, 7, 7);
        testBoard.setChessPiece(testBlackKing, 7, 3);
        testBoard.setChessPiece(testWhiteKing, 5, 3);
        testBoard.setChessPiece(testWhiteRook, 7,7);
    }

    /**
     * Tests if getChessPiece method returns expected result.
     */
    public void testGetChessPiece() throws Exception {
        testBoardSetup();
        ChessPiece answerPiece = testBoard.getChessPiece(7, 3);
        assertEquals(0, answerPiece.getSide());
        assertEquals("0K0", answerPiece.getIdentifier());
        assertEquals(7, answerPiece.getPosition().left());
        assertEquals(3, answerPiece.getPosition().right());
    }

    /**
     * Tests if calling getChessPiece method on empty position returns null.
     */
    public void testGetChessPieceFromNullPosition() throws Exception {
        testBoardSetup();
        ChessPiece answerPiece = testBoard.getChessPiece(0, 0);
        assertNull(answerPiece);
    }

    /**
     * Tests if getChessPieceById method returns expected result.
     */
    public void testGetChessPieceById() throws Exception {
        testBoardSetup();
        ChessPiece answerPiece = testBoard.getChessPieceById("0K0");
        assertEquals(0, answerPiece.getSide());
        assertEquals("0K0", answerPiece.getIdentifier());
        assertEquals(7, answerPiece.getPosition().left());
        assertEquals(3, answerPiece.getPosition().right());
    }

    /**
     * Tests if calling getChessPieceById with wrong id returns null.
     */
    public void testGetChessPieceByWrongId() throws Exception {
        testBoardSetup();
        ChessPiece answerPiece = testBoard.getChessPieceById("abc");
        assertNull(answerPiece);
    }

    /**
     * Tests if setChessPiece method modifies ChessBoard as intended.
     */
    public void testSetChessPiece() throws Exception {
        testBoardSetup();
        ChessPiece movingPiece = testBoard.getChessPiece(7, 7);
        testBoard.setChessPiece(movingPiece, 0, 7);
        ChessPiece answerPiece = testBoard.getChessPiece(0,7);
        assertEquals(1, answerPiece.getSide());
        assertEquals("1r0", answerPiece.getIdentifier());
        assertEquals(0, answerPiece.getPosition().left());
        assertEquals(7, answerPiece.getPosition().right());
    }

    /**
     * Tests if calling setNullChessPiece with null ChessPiece throws NullPointerException.
     */
    public void testSetNullChessPiece() throws Exception {
        testBoardSetup();
        ChessPiece movingPiece = testBoard.getChessPiece(0, 0);
        try {
            testBoard.setChessPiece(movingPiece, 1, 1);
            fail("Exception should have been thrown");
        } catch(NullPointerException e) {
        }
    }

    /**
     * Tests if IntPair.equals() works properly.
     */
    public void testIntPairEquals() {
        IntPair testPair1 = new IntPair(2, 2);
        IntPair testPair2 = new IntPair(2, 2);
        IntPair testPair3 = new IntPair(1, 1);
        assertNotSame(testPair1, testPair2);
        assertTrue(testPair1.equals(testPair2));
        assertFalse(testPair1.equals(testPair3));
    }
}