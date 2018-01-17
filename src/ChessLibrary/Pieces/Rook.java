package ChessLibrary.Pieces;

/**
 * Rook subclass.
 */
public class Rook extends ChessPiece {
    /**
     * Rook constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public Rook(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "r" + id);
    }
}
