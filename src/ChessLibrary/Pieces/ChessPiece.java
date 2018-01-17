package ChessLibrary.Pieces;

import ChessLibrary.Util.IntPair;

/**
 * ChessLibrary.Pieces.ChessPiece -- Representation of each chess piece.
 * @author       Wonwoo Seo (wseo2@illinois.edu)
 */
public abstract class ChessPiece {
    private int SIDE;
    private int xCoordinate;
    private int yCoordinate;
    /**
     * Identifier is an unique 3-character string given to each piece.
     * Follows format side / type / number.
     * Example: Identifier for #3 black pawn would be "0p3".
     * See subclasses for type identifier character.
     */
    private String identifier;

    /**
     * Gets which side this piece is on.
     * @return 0 if black, 1 if white.
     */
    public int getSide() {
        return SIDE;
    }

    /**
     * Gets position of the piece.
     * @return Current position of the piece.
     */
    public IntPair getPosition() {
        IntPair currentPosition = new IntPair(xCoordinate, yCoordinate);
        return currentPosition;
    }

    /**
     * Gets identifier of the piece.
     * @return Identifier of the piece.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets which side this piece is on.
     * @param side 0 if black, 1 if white.
     * @return No return value.
     */
    public void setSide(int side) {
        SIDE = side;
    }

    /**
     * Sets position of the piece.
     * @param x Destination x-coordinate
     * @param y Destination y-coordinate
     * @return No return value.
     */
    public void setPosition(int x, int y) {
        xCoordinate = x;
        yCoordinate = y;
    }

    /**
     * Sets identifier of the piece to given String.
     * @param id New identifier value in String.
     * @return No return value.
     */
    protected void setIdentifier(String id) {
        identifier = id;
    }
}