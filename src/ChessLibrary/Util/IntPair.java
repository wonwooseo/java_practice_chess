package ChessLibrary.Util;

/**
 * ChessLibrary.Util.IntPair -- Very simple class to represent a pair of integers.
 * @author    Wonwoo Seo (wonwooseo@hotmail.com)
 */
public class IntPair {
    private int LEFT;
    private int RIGHT;

    /**
     * ChessLibrary.Util.IntPair Constructor. Creates ChessLibrary.Util.IntPair with two given values.
     * @param left Left pair value.
     * @param right Right pair value.
     */
    public IntPair(int left, int right) {
        LEFT = left;
        RIGHT = right;
    }

    /**
     * Returns left value of the pair.
     * @return Left pair value.
     */
    public int left() {
        return LEFT;
    }

    /**
     * Returns right value of the pair.
     * @return Right pair value.
     */
    public int right() {
        return RIGHT;
    }

    /**
     * Checks if given IntPair has equal value to this IntPair.
     * @param target Another IntPair to compare value.
     * @return True if equal, false if not.
     */
    public boolean equals(IntPair target) {
        if(LEFT == target.left() && RIGHT == target.right()) {
            return true;
        } else {
            return false;
        }
    }
}
