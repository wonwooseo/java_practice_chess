package test;

import ChessLibrary.Game;
import ChessLibrary.Interface.InterfaceController;
import ChessLibrary.Pieces.ChessPiece;
import ChessLibrary.Util.IntPair;
import ChessLibrary.Util.TurnData;
import junit.framework.TestCase;

import java.util.List;

/**
 * Tests methods of interfaceController.
 */
public class InterfaceTest extends TestCase {
    private static int TURN_BLACK = 0;
    private static int TURN_WHITE = 1;

    private InterfaceController testControl;

    /**
     * Tests if new game is created properly without affecting scores.
     */
    public void testNewGame() {
        testControl = new InterfaceController();
        testControl.startNewGame(8, 8, false);
        assertEquals(0, testControl.getBlackScore());
        assertEquals(0, testControl.getWhiteScore());
    }

    /**
     * Tests if setting and getting player name of black sides work properly.
     */
    public void testBlackName() {
        testControl = new InterfaceController();
        testControl.setBlackName("testBlackName");
        assertEquals("testBlackName", testControl.getBlackName());
    }

    /**
     * Tests if setting and getting player name of white side work properly.
     */
    public void testWhiteName() {
        testControl = new InterfaceController();
        testControl.setWhiteName("testWhiteName");
        assertEquals("testWhiteName", testControl.getWhiteName());
    }

    /**
     * Tests if score of white goes up if black surrenders.
     */
    public void testBlackSurrender() {
        testControl = new InterfaceController();
        testControl.forfeit(8, 8, TURN_BLACK, false);
        assertEquals(0, testControl.getBlackScore());
        assertEquals(1, testControl.getWhiteScore());
    }

    /**
     * Tests if score of black goes up if white surrenders.
     */
    public void testWhiteSurrender() {
        testControl = new InterfaceController();
        testControl.forfeit(8, 8, TURN_WHITE, false);
        assertEquals(0, testControl.getWhiteScore());
        assertEquals(1, testControl.getBlackScore());
    }

    /**
     * Tests if undo works properly when called by black side.
     */
    public void testBlackUndo() {
        testControl = new InterfaceController();
        // Setup test game and make moves
        Game testGame = new Game(8, 8, false);
        // Make first white / black move
        moveEachSide(testGame);
        // Second white move
        ChessPiece testPiece = testGame.getBoard().getChessPieceById("1p1");
        IntPair currPosition = testPiece.getPosition();
        List<IntPair> movesList = testGame.getMovesInterface("1p1");
        testGame.movePieceInterface(0, movesList, "1p1");
        TurnData testWhiteMove = new TurnData(currPosition, movesList.get(0), testPiece, null);
        testControl.pushWhiteStack(testWhiteMove);
        testGame.nextTurn();
        // Call undo
        testControl.undo(TURN_BLACK, testGame.getBoard());
        assertTrue(testControl.isBlackStackEmpty());
        assertEquals("1p1", testGame.getBoard().getChessPiece(1, 6).getIdentifier());
        assertNull(testGame.getBoard().getChessPiece(1, 5));
        assertEquals("0p0", testGame.getBoard().getChessPiece(0, 1).getIdentifier());
        assertNull(testGame.getBoard().getChessPiece(0, 2));
    }

    /**
     * Tests if undo works properly when called by white side.
     */
    public void testWhiteUndo() {
        testControl = new InterfaceController();
        // Setup test game and make moves
        Game testGame = new Game(8, 8, false);
        // Make first white / black move
        moveEachSide(testGame);
        // Call undo
        testControl.undo(TURN_WHITE, testGame.getBoard());
        assertTrue(testControl.isWhiteStackEmpty());
        assertEquals("1p0", testGame.getBoard().getChessPiece(0, 6).getIdentifier());
        assertNull(testGame.getBoard().getChessPiece(0, 5));
        assertEquals("0p0", testGame.getBoard().getChessPiece(0, 1).getIdentifier());
        assertNull(testGame.getBoard().getChessPiece(0, 2));
    }

    /**
     * Tests if resetting stacks work properly.
     */
    public void testResetStacks() {
        testControl = new InterfaceController();
        for(int count = 0; count < 10; count++) {
            TurnData testData = new TurnData();
            testControl.pushBlackStack(testData);
            testControl.pushWhiteStack(testData);
        }
        testControl.resetStacks();
        assertTrue(testControl.isBlackStackEmpty());
        assertTrue(testControl.isWhiteStackEmpty());
    }

    /**
     * Helper function to make moves.
     * @param testGame Game object to retrieve chessboard and make moves.
     */
    private void moveEachSide(Game testGame) {
        // First white move
        ChessPiece testPiece = testGame.getBoard().getChessPieceById("1p0");
        IntPair currPosition = testPiece.getPosition();
        List<IntPair> movesList = testGame.getMovesInterface("1p0");
        testGame.movePieceInterface(0, movesList, "1p0");
        TurnData testWhiteMove = new TurnData(currPosition, movesList.get(0), testPiece, null);
        testControl.pushWhiteStack(testWhiteMove);
        testGame.nextTurn();
        // First black move
        testPiece = testGame.getBoard().getChessPieceById("0p0");
        currPosition = testPiece.getPosition();
        movesList = testGame.getMovesInterface("0p0");
        testGame.movePieceInterface(0, movesList, "0p0");
        TurnData testBlackMove = new TurnData(currPosition, movesList.get(0), testPiece, null);
        testControl.pushBlackStack(testBlackMove);
        testGame.nextTurn();
    }
}
