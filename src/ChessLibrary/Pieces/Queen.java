package ChessLibrary.Pieces;

/**
 * Queen subclass.
 */
public class Queen extends ChessPiece {
    /**
     * Queen constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public Queen(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "Q" + id);
    }
}
