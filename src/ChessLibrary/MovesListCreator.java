package ChessLibrary;

import ChessLibrary.Pieces.ChessPiece;
import ChessLibrary.Pieces.King;
import ChessLibrary.Util.IntPair;

import java.util.ArrayList;
import java.util.List;

/**
 * ChessLibrary.MovesListCreator -- Contains methods to return list of possible moves of given a piece.
 *                     ChessLibrary.MovesListCreator must be initialized every turn.
 * @author Wonwoo Seo (wonwooseo@hotmail.com)
 */
public class MovesListCreator {
    private ChessBoard CURRENT_BOARD;
    private int BOARD_ROWS;
    private int BOARD_COLUMNS;

    /**
     * MoveListCreator constructor.
     * @param board ChessLibrary.ChessBoard object for MoveListCreator to examine.
     * @param rows Number of rows of chessboard.
     * @param columns Number of columns of chessboard.
     */
    public MovesListCreator(ChessBoard board, int rows, int columns) {
        CURRENT_BOARD = board;
        BOARD_ROWS = rows;
        BOARD_COLUMNS = columns;
    }

    /**
     * Checks if given position is occupied by other piece.
     * @param side SIDE value to determine which side is ally.
     * @param position Position to check.
     * @return 0 if not occupied, 1 if ally occupies given position, 2 if enemy occupies given position.
     */
    public int checkOccupied(int side, IntPair position) {
        ChessPiece target = CURRENT_BOARD.getChessPiece(position.left(), position.right());
        if(target == null) {
            return 0;
        } else if(target.getSide() == side) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * Checks if move to destination results in being checked.
     * This method is only used to determine illegal moves of ChessLibrary.Pieces.King piece.
     * @param piece ChessLibrary.Pieces.ChessPiece to move(ChessLibrary.Pieces.King).
     * @param destination Destination to move.
     * @return Whether move results in check.
     */
    public boolean checkNextMoveIsCheck(ChessPiece piece, IntPair destination) {
        IntPair currentPosition = piece.getPosition();
        // Temporarily move piece to destination
        CURRENT_BOARD.deleteChessPiece(currentPosition.left(), currentPosition.right());
        ChessPiece deleted = CURRENT_BOARD.getChessPiece(destination.left(), destination.right());
        CURRENT_BOARD.setChessPiece(piece, destination.left(), destination.right());
        List<ChessPiece> attackerList = CURRENT_BOARD.checkIfCheck(piece.getSide());
        // Check if enemy king can attack own king
        List<IntPair> movesList = createKingMovesOnlyBorderCheck(piece);
        for(int count = 0; count < movesList.size(); count++) {
            IntPair possibleMove = movesList.get(count);
            ChessPiece attacker = CURRENT_BOARD.getChessPiece(possibleMove.left(), possibleMove.right());
            if(attacker instanceof King && attacker.getSide() != piece.getSide()) {
                attackerList.add(attacker);
            }
        }
        // Undo move
        CURRENT_BOARD.deleteChessPiece(destination.left(), destination.right());
        if(deleted != null) {
            CURRENT_BOARD.setChessPiece(deleted, destination.left(), destination.right());
        }
        CURRENT_BOARD.setChessPiece(piece, currentPosition.left(), currentPosition.right());

        if(attackerList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.King piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createKingMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // Check if ChessLibrary.Pieces.King can move 1 block horizontally / vertically / diagonally.
        if(currentPosition.left() + 1 < BOARD_COLUMNS) { // 1 block East
            IntPair moveEast = new IntPair(currentPosition.left() + 1, currentPosition.right());
            int occupyCheckE = checkOccupied(piece.getSide(), moveEast);
            if(occupyCheckE == 0 || occupyCheckE == 2) { // target cell is empty or occupied with enemy piece
                if(!checkNextMoveIsCheck(piece, moveEast)) {
                    movesList.add(moveEast);
                }
            }
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South-East
                IntPair moveSouthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() + 1);
                int occupyCheckSE = checkOccupied(piece.getSide(), moveSouthEast);
                if(occupyCheckSE == 0 || occupyCheckSE == 2) {
                    if(!checkNextMoveIsCheck(piece, moveSouthEast)) {
                        movesList.add(moveSouthEast);
                    }
                }
            }
            if(currentPosition.right() - 1 >= 0) { // 1 block North-East
                IntPair moveNorthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() - 1);
                int occupyCheckNE = checkOccupied(piece.getSide(), moveNorthEast);
                if(occupyCheckNE == 0 || occupyCheckNE == 2) {
                    if(!checkNextMoveIsCheck(piece, moveNorthEast)) {
                        movesList.add(moveNorthEast);
                    }
                }
            }
        }
        if(currentPosition.left() - 1 >= 0) { // 1 block West
            IntPair moveWest = new IntPair(currentPosition.left() - 1, currentPosition.right());
            int occupyCheckW = checkOccupied(piece.getSide(), moveWest);
            if(occupyCheckW == 0 || occupyCheckW == 2) {
                if(!checkNextMoveIsCheck(piece, moveWest)) {
                    movesList.add(moveWest);
                }
            }
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South-West
                IntPair moveSouthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() + 1);
                int occupyCheckSW = checkOccupied(piece.getSide(), moveSouthWest);
                if(occupyCheckSW == 0 || occupyCheckSW == 2) {
                    if(!checkNextMoveIsCheck(piece, moveSouthWest)) {
                        movesList.add(moveSouthWest);
                    }
                }
            }
            if(currentPosition.right() - 1 >= 0) { // 1 block North-West
                IntPair moveNorthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() - 1);
                int occupyCheckNW = checkOccupied(piece.getSide(), moveNorthWest);
                if(occupyCheckNW == 0 || occupyCheckNW == 2) {
                    if(!checkNextMoveIsCheck(piece, moveNorthWest)) {
                        movesList.add(moveNorthWest);
                    }
                }
            }
        }
        if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South
            IntPair moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + 1);
            int occupyCheckS = checkOccupied(piece.getSide(), moveSouth);
            if(occupyCheckS == 0 || occupyCheckS == 2) {
                if(!checkNextMoveIsCheck(piece, moveSouth)) {
                    movesList.add(moveSouth);
                }
            }
        }
        if(currentPosition.right() - 1 >= 0) { // 1 block North
            IntPair moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - 1);
            int occupyCheckN = checkOccupied(piece.getSide(), moveNorth);
            if(occupyCheckN == 0 || occupyCheckN == 2) {
                if(!checkNextMoveIsCheck(piece, moveNorth)) {
                    movesList.add(moveNorth);
                }
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of possible moves of given ChessLibrary.Pieces.King piece with only check and out-of-border checked.
     * This method is for determination of stalemate.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece, with only out-of-border and check cases checked.
     */
    public List<IntPair> createKingMovesWithoutOccupyCheck(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // Check if ChessLibrary.Pieces.King can move 1 block horizontally / vertically / diagonally.
        if(currentPosition.left() + 1 < BOARD_COLUMNS) { // 1 block East
            IntPair moveEast = new IntPair(currentPosition.left() + 1, currentPosition.right());
            if(!checkNextMoveIsCheck(piece, moveEast)) {
                movesList.add(moveEast);
            }
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South-East
                IntPair moveSouthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() + 1);
                if(!checkNextMoveIsCheck(piece, moveSouthEast)) {
                    movesList.add(moveSouthEast);
                }
            }
            if(currentPosition.right() - 1 >= 0) { // 1 block North-East
                IntPair moveNorthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() - 1);
                if(!checkNextMoveIsCheck(piece, moveNorthEast)) {
                    movesList.add(moveNorthEast);
                }
            }
        }
        if(currentPosition.left() - 1 >= 0) { // 1 block West
            IntPair moveWest = new IntPair(currentPosition.left() - 1, currentPosition.right());
            if(!checkNextMoveIsCheck(piece, moveWest)) {
                movesList.add(moveWest);
            }
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South-West
                IntPair moveSouthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() + 1);
                if(!checkNextMoveIsCheck(piece, moveSouthWest)) {
                    movesList.add(moveSouthWest);
                }
            }
            if(currentPosition.right() - 1 >= 0) { // 1 block North-West
                IntPair moveNorthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() - 1);
                if(!checkNextMoveIsCheck(piece, moveNorthWest)) {
                    movesList.add(moveNorthWest);
                }
            }
        }
        if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South
            IntPair moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + 1);
            if(!checkNextMoveIsCheck(piece, moveSouth)) {
                movesList.add(moveSouth);
            }
        }
        if(currentPosition.right() - 1 >= 0) { // 1 block North
            IntPair moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - 1);
            if(!checkNextMoveIsCheck(piece, moveNorth)) {
                movesList.add(moveNorth);
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of possible moves of given ChessLibrary.Pieces.King piece with only out-of-border checked.
     * This method is for determination of checkmate.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece, with only out-of-border cases checked.
     */
    public List<IntPair> createKingMovesOnlyBorderCheck(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // Check if ChessLibrary.Pieces.King can move 1 block horizontally / vertically / diagonally.
        if(currentPosition.left() + 1 < BOARD_COLUMNS) { // 1 block East
            IntPair moveEast = new IntPair(currentPosition.left() + 1, currentPosition.right());
            movesList.add(moveEast);
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South-East
                IntPair moveSouthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() + 1);
                movesList.add(moveSouthEast);
            }
            if(currentPosition.right() - 1 >= 0) { // 1 block North-East
                IntPair moveNorthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() - 1);
                movesList.add(moveNorthEast);
            }
        }
        if(currentPosition.left() - 1 >= 0) { // 1 block West
            IntPair moveWest = new IntPair(currentPosition.left() - 1, currentPosition.right());
            movesList.add(moveWest);

            if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South-West
                IntPair moveSouthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() + 1);
                movesList.add(moveSouthWest);

            }
            if(currentPosition.right() - 1 >= 0) { // 1 block North-West
                IntPair moveNorthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() - 1);
                movesList.add(moveNorthWest);

            }
        }
        if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South
            IntPair moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + 1);
            movesList.add(moveSouth);
        }
        if(currentPosition.right() - 1 >= 0) { // 1 block North
            IntPair moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - 1);
            movesList.add(moveNorth);
        }
        return movesList;
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.Queen piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createQueenMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // Variables to determine if path is blocked
        int blockedEast = Integer.MAX_VALUE;
        int blockedSouthEast = Integer.MAX_VALUE;
        int blockedNorthEast = Integer.MAX_VALUE;
        int blockedWest = Integer.MAX_VALUE;
        int blockedSouthWest = Integer.MAX_VALUE;
        int blockedNorthWest = Integer.MAX_VALUE;
        int blockedNorth = Integer.MAX_VALUE;
        int blockedSouth = Integer.MAX_VALUE;
        // Check if ChessLibrary.Pieces.Queen can move horizontally / vertically / diagonally from 1 block to 7 blocks.
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() + count < BOARD_COLUMNS) { // East
                if(count < blockedEast) {
                    blockedEast = getBlockedEast(piece, movesList, currentPosition, blockedEast, count);
                }
                if(currentPosition.right() + count < BOARD_ROWS) { // South-East
                    if(count < blockedSouthEast) {
                        blockedSouthEast = getBlockedSouthEast(piece, movesList, currentPosition, blockedSouthEast, count);
                    }
                }
                if(currentPosition.right() - count >= 0) { // North-East
                    if(count < blockedNorthEast) {
                        blockedNorthEast = getBlockedNorthEast(piece, movesList, currentPosition, blockedNorthEast, count);
                    }
                }
            }
            if(currentPosition.left() - count >= 0) { // West
                if(count < blockedWest) {
                    blockedWest = getBlockedWest(piece, movesList, currentPosition, blockedWest, count);
                }
                if(currentPosition.right() + count < BOARD_ROWS) { // South-West
                    if(count < blockedSouthWest) {
                        blockedSouthWest = getBlockedSouthWest(piece, movesList, currentPosition, blockedSouthWest, count);
                    }
                }
                if(currentPosition.right() - count >= 0) { // North-West
                    if(count < blockedNorthWest) {
                        blockedNorthWest = getBlockedNorthWest(piece, movesList, currentPosition, blockedNorthWest, count);
                    }
                }
            }
            if(currentPosition.right() + count < BOARD_ROWS) { // South
                if(count < blockedSouth) {
                    blockedSouth = getBlockedSouth(piece, movesList, currentPosition, blockedSouth, count);
                }
            }
            if(currentPosition.right() - count >= 0) { // North
                if(count < blockedNorth) {
                    blockedNorth = getBlockedNorth(piece, movesList, currentPosition, blockedNorth, count);
                }
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.Rook piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createRookMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // Values to determine if path is blocked
        int blockedEast = Integer.MAX_VALUE;
        int blockedWest = Integer.MAX_VALUE;
        int blockedNorth = Integer.MAX_VALUE;
        int blockedSouth = Integer.MAX_VALUE;
        // Check if ChessLibrary.Pieces.Rook can move horizontally / vertically from 1 block to 7 blocks.
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() + count < BOARD_COLUMNS) { // East
                if(count < blockedEast) {
                    blockedEast = getBlockedEast(piece, movesList, currentPosition, blockedEast, count);
                }
            }
            if(currentPosition.left() - count >= 0) { // West
                if(count < blockedWest) {
                    blockedWest = getBlockedWest(piece, movesList, currentPosition, blockedWest, count);
                }
            }
            if(currentPosition.right() + count < BOARD_ROWS) { // South
                if(count < blockedSouth) {
                    blockedSouth = getBlockedSouth(piece, movesList, currentPosition, blockedSouth, count);
                }
            }
            if(currentPosition.right() - count >= 0) { // North
                if(count < blockedNorth) {
                    blockedNorth = getBlockedNorth(piece, movesList, currentPosition, blockedNorth, count);
                }
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.Bishop piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createBishopMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // Values to determine if path is blocked
        int blockedSouthEast = Integer.MAX_VALUE;
        int blockedNorthEast = Integer.MAX_VALUE;
        int blockedSouthWest = Integer.MAX_VALUE;
        int blockedNorthWest = Integer.MAX_VALUE;
        // Check if bishop can move diagonally from 1 block to 7 blocks.
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() + count < BOARD_COLUMNS) { // out of border check
                if(currentPosition.right() + count < BOARD_ROWS) { // South-East
                    if(count < blockedSouthEast) {
                        blockedSouthEast = getBlockedSouthEast(piece, movesList, currentPosition, blockedSouthEast, count);
                    }
                }
                if(currentPosition.right() - count >= 0) { // North-East
                    if(count < blockedNorthEast) {
                        blockedNorthEast = getBlockedNorthEast(piece, movesList, currentPosition, blockedNorthEast, count);
                    }
                }
            }
            if(currentPosition.left() - count >= 0) {
                if(currentPosition.right() + count < BOARD_ROWS) { // South-West
                    if(count < blockedSouthWest) {
                        blockedSouthWest = getBlockedSouthWest(piece, movesList, currentPosition, blockedSouthWest, count);
                    }
                }
                if(currentPosition.right() - count >= 0) { // North-West
                    if(count < blockedNorthWest) {
                        blockedNorthWest = getBlockedNorthWest(piece, movesList, currentPosition, blockedNorthWest, count);
                    }
                }
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.Knight piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createKnightMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        if(currentPosition.left() + 1 < BOARD_COLUMNS) { // out of border check
            if(currentPosition.right() + 2 < BOARD_ROWS) { // 1 block East, 2 blocks South
                IntPair moveEast1South2 = new IntPair(currentPosition.left() + 1, currentPosition.right() + 2);
                int occupyCheckE1S2 = checkOccupied(piece.getSide(), moveEast1South2);
                if(occupyCheckE1S2 == 0 || occupyCheckE1S2 == 2) {
                    movesList.add(moveEast1South2);
                }
            }
            if(currentPosition.right() - 2 >= 0) { // 1 block East, 2 blocks North
                IntPair moveEast1North2 = new IntPair(currentPosition.left() + 1, currentPosition.right() - 2);
                int occupyCheckE1N2 = checkOccupied(piece.getSide(), moveEast1North2);
                if(occupyCheckE1N2 == 0 || occupyCheckE1N2 == 2) {
                    movesList.add(moveEast1North2);
                }
            }
        }
        if(currentPosition.left() + 2 < BOARD_COLUMNS) {
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 2 blocks East, 1 block South
                IntPair moveEast2South1 = new IntPair(currentPosition.left() + 2, currentPosition.right() + 1);
                int occupyCheckE2S1 = checkOccupied(piece.getSide(), moveEast2South1);
                if(occupyCheckE2S1 == 0 || occupyCheckE2S1 == 2) {
                    movesList.add(moveEast2South1);
                }
            }
            if(currentPosition.right() - 1 >= 0) { // 2 blocks East, 1 block North
                IntPair moveEast2North1 = new IntPair(currentPosition.left() + 2, currentPosition.right() - 1);
                int occupyCheckE2N1 = checkOccupied(piece.getSide(), moveEast2North1);
                if(occupyCheckE2N1 == 0 || occupyCheckE2N1 == 2) {
                    movesList.add(moveEast2North1);
                }
            }
        }
        if(currentPosition.left() - 1 >= 0) {
            if(currentPosition.right() + 2 < BOARD_ROWS) { // 1 block West, 2 blocks South
                IntPair moveWest1South2 = new IntPair(currentPosition.left() - 1, currentPosition.right() + 2);
                int occupyCheckW1S2 = checkOccupied(piece.getSide(), moveWest1South2);
                if(occupyCheckW1S2 == 0 || occupyCheckW1S2 == 2) {
                    movesList.add(moveWest1South2);
                }
            }
            if(currentPosition.right() - 2 >= 0) { // 1 block West, 2 blocks North
                IntPair moveWest1North2 = new IntPair(currentPosition.left() - 1, currentPosition.right() - 2);
                int occupyCheckW1N2 = checkOccupied(piece.getSide(), moveWest1North2);
                if(occupyCheckW1N2 == 0 || occupyCheckW1N2 == 2) {
                    movesList.add(moveWest1North2);
                }
            }
        }
        if(currentPosition.left() - 2 >= 0) {
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 2 blocks West, 1 block South
                IntPair moveWest2South1 = new IntPair(currentPosition.left() - 2, currentPosition.right() + 1);
                int occupyCheckW2S1 = checkOccupied(piece.getSide(), moveWest2South1);
                if(occupyCheckW2S1 == 0 || occupyCheckW2S1 == 2) {
                    movesList.add(moveWest2South1);
                }
            }
            if(currentPosition.right() - 1 >= 0) { // 2 blocks West, 1 block North
                IntPair moveWest2North1 = new IntPair(currentPosition.left() - 2, currentPosition.right() - 1);
                int occupyCheckW2N1 = checkOccupied(piece.getSide(), moveWest2North1);
                if(occupyCheckW2N1 == 0 || occupyCheckW2N1 == 2) {
                    movesList.add(moveWest2North1);
                }
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.Pawn piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createPawnMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // ChessLibrary.Pieces.Pawn is on black side
        if(piece.getSide() == 0) {
            if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block to South
                IntPair moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + 1);
                int occupyCheckS = checkOccupied(piece.getSide(), moveSouth);
                if(occupyCheckS == 0) {
                    movesList.add(moveSouth);
                }
                if(currentPosition.left() + 1 < BOARD_COLUMNS) { // 1 block to South-East only if capturing
                    IntPair moveSouthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() + 1);
                    int occupyCheckSE = checkOccupied(piece.getSide(), moveSouthEast);
                    if(occupyCheckSE == 2) {
                        movesList.add(moveSouthEast);
                    }
                }
                if(currentPosition.left() - 1 >= 0) { // 1 block to South-West only if capturing
                    IntPair moveSouthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() + 1);
                    int occupyCheckSW = checkOccupied(piece.getSide(), moveSouthWest);
                    if(occupyCheckSW == 2) {
                        movesList.add(moveSouthWest);
                    }
                }
            }
            if(piece.getPosition().right() == 1) { // 2 blocks to South
                IntPair moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + 1);
                int occupyCheckS = checkOccupied(piece.getSide(), moveSouth);
                if(occupyCheckS == 0) { // path is not blocked
                    IntPair moveSouthTwo = new IntPair(currentPosition.left(), currentPosition.right() + 2);
                    int occupyCheckS2 = checkOccupied(piece.getSide(), moveSouthTwo);
                    if (occupyCheckS2 == 0) {
                        movesList.add(moveSouthTwo);
                    }
                }
            }
        }
        // ChessLibrary.Pieces.Pawn is on white side
        if(piece.getSide() == 1) {
            if(currentPosition.right() - 1 >= 0) { // 1 block to North
                IntPair moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - 1);
                int occupyCheckN = checkOccupied(piece.getSide(), moveNorth);
                if(occupyCheckN == 0) {
                    movesList.add(moveNorth);
                }
                if(currentPosition.left() + 1 < BOARD_COLUMNS) { // 1 block to North-East
                    IntPair moveNorthEast = new IntPair(currentPosition.left() + 1, currentPosition.right() - 1);
                    int occupyCheckNE = checkOccupied(piece.getSide(), moveNorthEast);
                    if(occupyCheckNE == 2) {
                        movesList.add(moveNorthEast);
                    }
                }
                if(currentPosition.left() - 1 >= 0) { // 1 block to North-West
                    IntPair moveNorthWest = new IntPair(currentPosition.left() - 1, currentPosition.right() - 1);
                    int occupyCheckNW = checkOccupied(piece.getSide(), moveNorthWest);
                    if(occupyCheckNW == 2) {
                        movesList.add(moveNorthWest);
                    }
                }
            }
            if(piece.getPosition().right() == 6) { // 2 blocks to North
                IntPair moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - 1);
                int occupyCheckN = checkOccupied(piece.getSide(), moveNorth);
                if(occupyCheckN == 0) { // path is not blocked
                    IntPair moveNorthTwo = new IntPair(currentPosition.left(), currentPosition.right() - 2);
                    int occupyCheckN2 = checkOccupied(piece.getSide(), moveNorthTwo);
                    if (occupyCheckN2 == 0) {
                        movesList.add(moveNorthTwo);
                    }
                }
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.Leaper piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createLeaperMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        if(currentPosition.left() + 1 < BOARD_COLUMNS) { // 1 block East
            IntPair moveEast1 = new IntPair(currentPosition.left() + 1, currentPosition.right());
            int occupyCheckE1 = checkOccupied(piece.getSide(), moveEast1);
            if(occupyCheckE1 == 0) { // capture only if 2 blocks away
                movesList.add(moveEast1);
            }
            if(currentPosition.left() + 2 < BOARD_COLUMNS) { // 2 blocks East
                IntPair moveEast2 = new IntPair(currentPosition.left() + 2, currentPosition.right());
                int occupyCheckE2 = checkOccupied(piece.getSide(), moveEast2);
                if(occupyCheckE2 == 2) { // moves only if capturing
                    movesList.add(moveEast2);
                }
            }
        }
        if(currentPosition.right() + 1 < BOARD_ROWS) { // 1 block South
            IntPair moveSouth1 = new IntPair(currentPosition.left(), currentPosition.right() + 1);
            int occupyCheckS1 = checkOccupied(piece.getSide(), moveSouth1);
            if(occupyCheckS1 == 0) { // capture only if 2 blocks away
                movesList.add(moveSouth1);
            }
            if(currentPosition.right() + 2 < BOARD_ROWS) { // 2 blocks South
                IntPair moveSouth2 = new IntPair(currentPosition.left(), currentPosition.right() + 2);
                int occupyCheckS2 = checkOccupied(piece.getSide(), moveSouth2);
                if(occupyCheckS2 == 2) { // moves only if capturing
                    movesList.add(moveSouth2);
                }
            }
        }
        if(currentPosition.left() - 1 >= 0) { // 1 block West
            IntPair moveWest1 = new IntPair(currentPosition.left() - 1, currentPosition.right());
            int occupyCheckW1 = checkOccupied(piece.getSide(), moveWest1);
            if(occupyCheckW1 == 0) { // capture only if 2 blocks away
                movesList.add(moveWest1);
            }
            if(currentPosition.left() - 2 >= 0) { // 2 blocks West
                IntPair moveWest2 = new IntPair(currentPosition.left() - 2, currentPosition.right());
                int occupyCheckW2 = checkOccupied(piece.getSide(), moveWest2);
                if(occupyCheckW2 == 2) { // moves only if capturing
                    movesList.add(moveWest2);
                }
            }
        }
        if(currentPosition.right() - 1 >= 0) { // 1 block North
            IntPair moveNorth1 = new IntPair(currentPosition.left(), currentPosition.right() - 1);
            int occupyCheckN1 = checkOccupied(piece.getSide(), moveNorth1);
            if(occupyCheckN1 == 0) { // capture only if 2 blocks away
                movesList.add(moveNorth1);
            }
            if(currentPosition.right() - 2 >= 0) { // 2 blocks North
                IntPair moveNorth2 = new IntPair(currentPosition.left(), currentPosition.right() - 2);
                int occupyCheckN2 = checkOccupied(piece.getSide(), moveNorth2);
                if(occupyCheckN2 == 2) { // moves only if capturing
                    movesList.add(moveNorth2);
                }
            }
        }
        return movesList;
    }

    /**
     * Creates and returns a list of currently possible moves of given ChessLibrary.Pieces.Charger piece.
     * If no move is available, method returns a list of size 0, not null.
     * @param piece ChessLibrary.Pieces.ChessPiece object to determine possible moves.
     * @return List of possible moves of given piece.
     */
    public List<IntPair> createChargerMoves(ChessPiece piece) {
        List<IntPair> movesList = new ArrayList<>();
        IntPair currentPosition = piece.getPosition();
        // Check if ChessLibrary.Pieces.Queen can move horizontally / vertically / diagonally from 1 block to 7 blocks.
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() + count < BOARD_COLUMNS) { // East
                IntPair moveEast = new IntPair(currentPosition.left() + count, currentPosition.right());
                int occupyCheckE = checkOccupied(piece.getSide(), moveEast);
                if(occupyCheckE == 2) {
                    movesList.add(moveEast);
                    break;
                } else if(occupyCheckE == 1) {
                    if(count == 1) {
                        break;
                    }
                    moveEast = new IntPair(currentPosition.left() + count - 1, currentPosition.right());
                    movesList.add(moveEast);
                    break;
                } else if(moveEast.left() == BOARD_COLUMNS - 1) {
                    movesList.add(moveEast);
                    break;
                }
            }
        }
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() + count < BOARD_COLUMNS) {
                if(currentPosition.right() + count < BOARD_ROWS) { // South-East
                    IntPair moveSouthEast = new IntPair(currentPosition.left() + count, currentPosition.right() + count);
                    int occupyCheckSE = checkOccupied(piece.getSide(), moveSouthEast);
                    if(occupyCheckSE == 2) {
                        movesList.add(moveSouthEast);
                        break;
                    } else if(occupyCheckSE == 1) {
                        if(count == 1) {
                            break;
                        }
                        moveSouthEast = new IntPair(currentPosition.left() + count - 1, currentPosition.right() + count - 1);
                        movesList.add(moveSouthEast);
                        break;
                    } else if(moveSouthEast.left() == BOARD_COLUMNS - 1 || moveSouthEast.right() == BOARD_ROWS - 1) {
                        movesList.add(moveSouthEast);
                        break;
                    }
                }
            }
        }
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() + count < BOARD_COLUMNS) {
                if(currentPosition.right() - count >= 0) { // North-East
                    IntPair moveNorthEast = new IntPair(currentPosition.left() + count, currentPosition.right() - count);
                    int occupyCheckNE = checkOccupied(piece.getSide(), moveNorthEast);
                    if(occupyCheckNE == 2) {
                        movesList.add(moveNorthEast);
                        break;
                    } else if(occupyCheckNE == 1) {
                        if(count == 1) {
                            break;
                        }
                        moveNorthEast = new IntPair(currentPosition.left() + count - 1, currentPosition.right() - count + 1);
                        movesList.add(moveNorthEast);
                        break;
                    } else if(moveNorthEast.left() == BOARD_COLUMNS - 1 || moveNorthEast.right() == 0) {
                        movesList.add(moveNorthEast);
                    }
                }
            }
        }
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() - count >= 0) { // West
                IntPair moveWest = new IntPair(currentPosition.left() - count, currentPosition.right());
                int occupyCheckW = checkOccupied(piece.getSide(), moveWest);
                if(occupyCheckW == 2) {
                    movesList.add(moveWest);
                    break;
                } else if(occupyCheckW == 1) {
                    if(count == 1) {
                        break;
                    }
                    moveWest = new IntPair(currentPosition.left() - count + 1, currentPosition.right());
                    movesList.add(moveWest);
                    break;
                } else if(moveWest.left() == 0) {
                    movesList.add(moveWest);
                }
            }
        }
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() - count >= 0) {
                if(currentPosition.right() + count < BOARD_ROWS) { // South-West
                    IntPair moveSouthWest = new IntPair(currentPosition.left() - count, currentPosition.right() + count);
                    int occupyCheckSW = checkOccupied(piece.getSide(), moveSouthWest);
                    if(occupyCheckSW == 2) {
                        movesList.add(moveSouthWest);
                        break;
                    } else if(occupyCheckSW == 1) {
                        if(count == 1) {
                            break;
                        }
                        moveSouthWest = new IntPair(currentPosition.left() - count + 1, currentPosition.right() + count - 1);
                        movesList.add(moveSouthWest);
                        break;
                    } else if(moveSouthWest.left() == 0 || moveSouthWest.right() == BOARD_ROWS - 1) {
                        movesList.add(moveSouthWest);
                    }
                }
            }
        }
        for(int count = 1; count <= 7; count++) {
            if(currentPosition.left() - count >= 0) {
                if(currentPosition.right() - count >= 0) { // North-West
                    IntPair moveNorthWest = new IntPair(currentPosition.left() - count, currentPosition.right() - count);
                    int occupyCheckNW = checkOccupied(piece.getSide(), moveNorthWest);
                    if(occupyCheckNW == 2) {
                        movesList.add(moveNorthWest);
                        break;
                    } else if(occupyCheckNW == 1) {
                        if(count == 1) {
                            break;
                        }
                        moveNorthWest = new IntPair(currentPosition.left() - count + 1, currentPosition.right() - count + 1);
                        movesList.add(moveNorthWest);
                        break;
                    } else if(moveNorthWest.left() == 0 || moveNorthWest.right() == 0) {
                        movesList.add(moveNorthWest);
                    }
                }
            }
        }
        for(int count = 1; count <= 7; count++) {
            if (currentPosition.right() + count < BOARD_ROWS) { // South
                IntPair moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + count);
                int occupyCheckS = checkOccupied(piece.getSide(), moveSouth);
                if (occupyCheckS == 2) {
                    movesList.add(moveSouth);
                    break;
                } else if(occupyCheckS == 1) {
                    if(count == 1) {
                        break;
                    }
                    moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + count - 1);
                    movesList.add(moveSouth);
                    break;
                } else if(moveSouth.right() == BOARD_ROWS - 1) {
                    movesList.add(moveSouth);
                }
            }
        }
        for(int count = 1; count <= 7; count++) {
            if (currentPosition.right() - count >= 0) { // North
                IntPair moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - count);
                int occupyCheckN = checkOccupied(piece.getSide(), moveNorth);
                if (occupyCheckN == 2) {
                    movesList.add(moveNorth);
                    break;
                } else if(occupyCheckN == 1) {
                    if(count == 1) {
                        break;
                    }
                    moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - count + 1);
                    movesList.add(moveNorth);
                    break;
                } else if(moveNorth.right() == 0) {
                    movesList.add(moveNorth);
                }
            }
        }
        return movesList;
    }

    /**
     * Helper function to check if piece can move north and updates blockedNorth if path is blocked.
     */
    private int getBlockedNorth(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedNorth, int count) {
        IntPair moveNorth = new IntPair(currentPosition.left(), currentPosition.right() - count);
        int occupyCheckN = checkOccupied(piece.getSide(), moveNorth);
        if(occupyCheckN == 0) {
            movesList.add(moveNorth);
        } else if(occupyCheckN == 1) {
            blockedNorth = count;
        } else {
            movesList.add(moveNorth);
            blockedNorth = count;
        }
        return blockedNorth;
    }

    /**
     * Helper function to check if piece can move south and updates blockedSouth if path is blocked.
     */
    private int getBlockedSouth(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedSouth, int count) {
        IntPair moveSouth = new IntPair(currentPosition.left(), currentPosition.right() + count);
        int occupyCheckS = checkOccupied(piece.getSide(), moveSouth);
        if(occupyCheckS == 0) {
            movesList.add(moveSouth);
        } else if(occupyCheckS == 1) {
            blockedSouth = count;
        } else {
            movesList.add(moveSouth);
            blockedSouth = count;
        }
        return blockedSouth;
    }

    /**
     * Helper function to check if piece can move northwest and updates blockedNorthWest if path is blocked.
     */
    private int getBlockedNorthWest(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedNorthWest, int count) {
        IntPair moveNorthWest = new IntPair(currentPosition.left() - count, currentPosition.right() - count);
        int occupyCheckNW = checkOccupied(piece.getSide(), moveNorthWest);
        if(occupyCheckNW == 0) {
            movesList.add(moveNorthWest);
        } else if(occupyCheckNW == 1) {
            blockedNorthWest = count;
        } else {
            movesList.add(moveNorthWest);
            blockedNorthWest = count;
        }
        return blockedNorthWest;
    }

    /**
     * Helper function to check if piece can move southwest and updates blockedSouthWest if path is blocked.
     */
    private int getBlockedSouthWest(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedSouthWest, int count) {
        IntPair moveSouthWest = new IntPair(currentPosition.left() - count, currentPosition.right() + count);
        int occupyCheckSW = checkOccupied(piece.getSide(), moveSouthWest);
        if(occupyCheckSW == 0) {
            movesList.add(moveSouthWest);
        } else if(occupyCheckSW == 1) {
            blockedSouthWest = count;
        } else {
            movesList.add(moveSouthWest);
            blockedSouthWest = count;
        }
        return blockedSouthWest;
    }

    /**
     * Helper function to check if piece can move west and updates blockedWest if path is blocked.
     */
    private int getBlockedWest(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedWest, int count) {
        IntPair moveWest = new IntPair(currentPosition.left() - count, currentPosition.right());
        int occupyCheckW = checkOccupied(piece.getSide(), moveWest);
        if(occupyCheckW == 0) {
            movesList.add(moveWest);
        } else if(occupyCheckW == 1) {
            blockedWest = count;
        } else {
            movesList.add(moveWest);
            blockedWest = count;
        }
        return blockedWest;
    }

    /**
     * Helper function to check if piece can move northeast and updates blockedNorthEast if path is blocked.
     */
    private int getBlockedNorthEast(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedNorthEast, int count) {
        IntPair moveNorthEast = new IntPair(currentPosition.left() + count, currentPosition.right() - count);
        int occupyCheckNE = checkOccupied(piece.getSide(), moveNorthEast);
        if(occupyCheckNE == 0) {
            movesList.add(moveNorthEast);
        } else if(occupyCheckNE == 1) {
            blockedNorthEast = count;
        } else {
            movesList.add(moveNorthEast);
            blockedNorthEast = count;
        }
        return blockedNorthEast;
    }

    /**
     * Helper function to check if piece can move southeast and updates blockedSouthEast if path is blocked.
     */
    private int getBlockedSouthEast(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedSouthEast, int count) {
        IntPair moveSouthEast = new IntPair(currentPosition.left() + count, currentPosition.right() + count);
        int occupyCheckSE = checkOccupied(piece.getSide(), moveSouthEast);
        if(occupyCheckSE == 0) {
            movesList.add(moveSouthEast);
        } else if(occupyCheckSE == 1) {
            blockedSouthEast = count;
        } else {
            movesList.add(moveSouthEast);
            blockedSouthEast = count;
        }
        return blockedSouthEast;
    }

    /**
     * Helper function to check if piece can move east and updates blockedEast if path is blocked.
     */
    private int getBlockedEast(ChessPiece piece, List<IntPair> movesList, IntPair currentPosition, int blockedEast, int count) {
        IntPair moveEast = new IntPair(currentPosition.left() + count, currentPosition.right());
        int occupyCheckE = checkOccupied(piece.getSide(), moveEast);
        if(occupyCheckE == 0) {
            movesList.add(moveEast);
        } else if(occupyCheckE == 1) {
            blockedEast = count;
        } else {
            movesList.add(moveEast);
            blockedEast = count;
        }
        return blockedEast;
    }
}
