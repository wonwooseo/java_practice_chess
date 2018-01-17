package ChessLibrary.Pieces;

/**
 * Pawn subclass.
 */
public class Pawn extends ChessPiece {
    /**
     * Pawn constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public Pawn(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "p" + id);
    }
}
