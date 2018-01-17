package ChessLibrary.Pieces;

/**
 * Knight subclass.
 */
public class Knight extends ChessPiece {
    /**
     * Knight constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public Knight(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "k" + id);
    }
}
