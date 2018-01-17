package ChessLibrary.Pieces;

/**
 * Charger subclass.
 * Charger is a custom chess piece which can move in any direction until it hits something.
 * If piece on same side blocks its path, charger stops in front of that piece.
 * If enemy piece is on path, charger captures that piece and stops on that position.
 * If nothing is on path, charger proceeds until it hits the border.
 */
public class Charger extends ChessPiece {
    /**
     * Charger constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public Charger(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "c" + id);
    }
}
