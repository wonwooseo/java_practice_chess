package ChessLibrary.Pieces;

/**
 * King subclass.
 */
public class King extends ChessPiece {
    /**
     * King constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public King(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "K" + id);
    }
}
