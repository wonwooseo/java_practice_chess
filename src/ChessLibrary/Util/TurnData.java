package ChessLibrary.Util;

import ChessLibrary.Pieces.ChessPiece;

/**
 * Class to hold information about moved / captured pieces of turn. Used for undo feature.
 * @author Wonwoo Seo (wonwooseo@hotmail.com)
 */
public class TurnData {
    private IntPair originalPosition;
    private IntPair newPosition;
    private ChessPiece movedPiece;
    private ChessPiece capturedPiece;

    /**
     * Default constructor. Only use this for testing purpose!
     */
    public TurnData() {
        originalPosition = null;
        newPosition = null;
        movedPiece = null;
        capturedPiece = null;
    }

    /**
     * Constructor to create TurnData object.
     * @param origPosition Original position of the moving piece.
     * @param newPosition New position of the moving piece.
     * @param movedPiece Moving piece.
     * @param capturedPiece Piece captured by moving piece. Null if nothing captured.
     */
    public TurnData(IntPair origPosition, IntPair newPosition, ChessPiece movedPiece, ChessPiece capturedPiece) {
        originalPosition = origPosition;
        this.newPosition = newPosition;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
    }

    /**
     * originalPosition getter.
     * @return originalPosition value.
     */
    public IntPair getOriginalPosition() {
        return originalPosition;
    }

    /**
     * currentPosition getter.
     * @return currentPosition value.
     */
    public IntPair getNewPosition() {
        return newPosition;
    }

    /**
     * movedPiece getter.
     * @return movedPiece ChessPiece object.
     */
    public ChessPiece getMovedPiece() {
        return movedPiece;
    }

    /**
     * capturedPiece getter.
     * @return capturedPiece ChessPiece object. Null if nothing captured.
     */
    public ChessPiece getCapturedPiece() {
        return capturedPiece;
    }
}
