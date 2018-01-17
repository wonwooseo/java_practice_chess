package ChessLibrary.Pieces;

/**
 * Bishop subclass.
 */
public class Bishop extends ChessPiece {
    /**
     * Bishop constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public Bishop(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "b" + id);
    }
}
