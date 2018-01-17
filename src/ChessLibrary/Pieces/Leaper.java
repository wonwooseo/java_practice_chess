package ChessLibrary.Pieces;

/**
 * Leaper subclass.
 * Leaper is a custom chess piece which can only capture pieces 2-cells away from it in N/E/S/W direction.
 * It can leap over pieces while capturing, but only can move 1-cell in N/E/S/W if not capturing.
 */
public class Leaper extends ChessPiece{
    /**
     * Leaper constructor.
     * @param side Which side this object is on.
     * @param id Identifier number.
     * @param x X-coordinate of position.
     * @param y Y-coordinate of position.
     */
    public Leaper(int side, int id, int x, int y) {
        setSide(side);
        setPosition(x, y);
        setIdentifier(side + "l" + id);
    }
}
