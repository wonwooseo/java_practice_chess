package ChessLibrary;

import ChessLibrary.Pieces.*;
import ChessLibrary.Util.IntPair;

import java.util.ArrayList;
import java.util.List;

/**
 * ChessLibrary.ChessBoard -- Representation of a chess board using 2D array.
 * @author Wonwoo Seo (wonwooseo@hotmail.com)
 */
public class ChessBoard {
    private int BOARD_ROWS;
    private int BOARD_COLUMNS;
    private ChessPiece[][] BOARD_PIECES;

    /**
     * ChessLibrary.ChessBoard Constructor. Creates ChessLibrary.ChessBoard of specified size and sets up pieces.
     * Coordinate (0, 0) of ChessLibrary.Pieces.ChessPiece 2D array is top-left.
     * @param rows Number of rows for chessboard.
     * @param columns Number of columns for chessboard.
     * @param customPiece Whether custom pieces should be used.
     */
    public ChessBoard(int rows, int columns, boolean customPiece) {
        BOARD_ROWS = rows;
        BOARD_COLUMNS = columns;
        BOARD_PIECES = new ChessPiece[BOARD_ROWS][BOARD_COLUMNS];
        setupPieces(customPiece);
    }

    /**
     * Gets ChessLibrary.Pieces.ChessPiece object in given position.
     * @param xCoordinate
     * @param yCoordinate
     * @return ChessLibrary.Pieces.ChessPiece on position (xCoordinate, yCoordinate). Null if position is empty.
     */
    public ChessPiece getChessPiece(int xCoordinate, int yCoordinate) {
        return BOARD_PIECES[yCoordinate][xCoordinate];
    }

    /**
     * Finds ChessLibrary.Pieces.ChessPiece object of given identifier and returns it.
     * @param id Identifier of the piece to look for.
     * @return ChessLibrary.Pieces.ChessPiece with given identifier. Null if none exists.
     */
    public ChessPiece getChessPieceById(String id) {
        for(int index_y = 0; index_y < BOARD_ROWS; index_y++) {
            for(int index_x = 0; index_x < BOARD_COLUMNS; index_x++) {
                ChessPiece piece = getChessPiece(index_x, index_y);
                if(piece != null && piece.getIdentifier().equals(id)) {
                    return piece;
                }
            }
        }
        return null;
    }

    /**
     * Sets ChessLibrary.Pieces.ChessPiece object in given position.
     * @param piece ChessLibrary.Pieces.ChessPiece to set position
     * @param xCoordinate Destination x-coordinate
     * @param yCoordinate Destination y-coordinate
     * @return No return value.
     */
    public void setChessPiece(ChessPiece piece, int xCoordinate, int yCoordinate) {
        BOARD_PIECES[yCoordinate][xCoordinate] = piece;
        piece.setPosition(xCoordinate, yCoordinate);
    }

    /**
     * Deletes ChessLibrary.Pieces.ChessPiece in given position.
     * @param xCoordinate
     * @param yCoordinate
     * @return No return value.
     */
    public void deleteChessPiece(int xCoordinate, int yCoordinate) {
        BOARD_PIECES[yCoordinate][xCoordinate] = null;
    }

    /**
     * Clears all cells of chessboard to null.
     * @return no value.
     */
    public void clearChessBoard() {
        for(int index_y = 0; index_y < BOARD_ROWS; index_y++) {
            for(int index_x = 0; index_x < BOARD_COLUMNS; index_x++) {
                deleteChessPiece(index_x, index_y);
            }
        }
    }

    /**
     * Checks if given side is on check status and returns list of attackers.
     * @param side Side to check if being checked.
     * @return List of attackers.
     */
    public List<ChessPiece> checkIfCheck(int side) {
        List<ChessPiece> attackerList = new ArrayList<>();
        // Search all cells and look for attackers
        for(int index_y = 0; index_y < BOARD_ROWS; index_y++) {
            for(int index_x = 0; index_x < BOARD_COLUMNS; index_x++) {
                ChessPiece attacker = getChessPiece(index_x, index_y); // attacker piece
                // King is checked in MoveListCreator class
                if(attacker != null && !(attacker instanceof King) && attacker.getSide() != side) {
                    List<IntPair> movesList = getMoves(attacker);
                    for(int count = 0; count < movesList.size(); count++) {
                        ChessPiece victimPiece = getChessPiece(movesList.get(count).left(), movesList.get(count).right());
                        if(victimPiece instanceof King && victimPiece.getSide() == side) {
                            attackerList.add(attacker);
                        }
                    }
                }
            }
        }
        return attackerList;
    }

    /**
     * Checks if check can be resolved by capturing attacker or blocking attacker's path.
     * @param side Side being checked(attacked).
     * @return True if yes, false if no.
     */
    public boolean checkDefendAttack(int side) {
        for (int index_y = 0; index_y < BOARD_ROWS; index_y++) {
            for (int index_x = 0; index_x < BOARD_COLUMNS; index_x++) {
                ChessPiece defender = getChessPiece(index_x, index_y);
                if(defender != null && defender.getSide() == side) {
                    List<IntPair> movesList = getMoves(defender);
                    IntPair currentPosition = defender.getPosition();
                    for(int count = 0; count < movesList.size(); count++) {
                        IntPair destination = movesList.get(count);
                        // Temporarily move piece to destination
                        deleteChessPiece(currentPosition.left(), currentPosition.right());
                        ChessPiece deleted = getChessPiece(destination.left(), destination.right());
                        setChessPiece(defender, destination.left(), destination.right());
                        // Check if moving defender resolved check status
                        List<ChessPiece> attackerList = checkIfCheck(defender.getSide());
                        // Undo move
                        deleteChessPiece(destination.left(), destination.right());
                        if(deleted != null) {
                            setChessPiece(deleted, destination.left(), destination.right());
                        }
                        setChessPiece(defender, currentPosition.left(), currentPosition.right());
                        if(attackerList.size() == 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if current board is in stalemate state.
     * @param side Which side's king to examine.
     * @return True if stalemate, false otherwise.
     */
    public boolean checkStaleMate(int side) {
        MovesListCreator creator = new MovesListCreator(this, BOARD_ROWS, BOARD_COLUMNS);
        ChessPiece king;
        if(side == 0) {
            king = getChessPieceById("0K0");
        } else {
            king = getChessPieceById("1K0");
        }
        List<IntPair> movesList = creator.createKingMovesWithoutOccupyCheck(king);
        if(movesList.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks if given side is on checked / checkmated / stalemated status in current setting of chessboard.
     * @param side Side to check status.
     * @return 0 if nothing, 1 if check, 2 if checkmate, 3 if stalemate.
     */
    public int checkCheckStatus(int side) {
        List<ChessPiece> attackerList = checkIfCheck(side);
        if(attackerList.size() == 0) { // Not in check status
            if(checkStaleMate(side)) {
                return 3;
            }
            return 0;
        }
        ChessPiece king;
        if(side == 0) {
            king = getChessPieceById("0K0");
        } else {
            king = getChessPieceById("1K0");
        }

        if(getMoves(king).size() > 0 || checkDefendAttack(side)) {
            return 1;
        }
        if(getMoves(king).size() == 0 && !checkDefendAttack(side)) {
            return 2;
        }
        return -1; // ERROR
    }

    /**
     * Gets currently available moves of a given piece. Verification of occupied/capture/blocked case is done here.
     * @param piece Target chess piece to retrieve available moves.
     * @return List of available destinations in ChessLibrary.Util.IntPair.
     */
    public List<IntPair> getMoves(ChessPiece piece) {
        List<IntPair> movesList = null;
        MovesListCreator creator = new MovesListCreator(this, BOARD_ROWS, BOARD_COLUMNS);
        char pieceType = piece.getIdentifier().charAt(1);
        switch(pieceType) {
            case 'K': {
                movesList = creator.createKingMoves(piece);
                break;
            }
            case 'Q': {
                movesList = creator.createQueenMoves(piece);
                break;
            }
            case 'r': {
                movesList = creator.createRookMoves(piece);
                break;
            }
            case 'b': {
                movesList = creator.createBishopMoves(piece);
                break;
            }
            case 'k': {
                movesList = creator.createKnightMoves(piece);
                break;
            }
            case 'p': {
                movesList = creator.createPawnMoves(piece);
                break;
            }
            case 'l': {
                movesList = creator.createLeaperMoves(piece);
                break;
            }
            case 'c': {
                movesList = creator.createChargerMoves(piece);
                break;
            }
        }
        return movesList;
    }

    /**
     * Fills up the ChessLibrary.ChessBoard with ChessLibrary.Pieces.ChessPiece objects on default position.
     * ChessLibrary.Pieces.ChessPiece.SIDE = 0 stands for black, 1 for white.
     * @returns No return value.
     */
    public void setupPieces(boolean customPiece) {
        // ChessLibrary.Pieces.Pawn setup
        for(int count = 0; count < 8; count++) {
            ChessPiece blackPawn = new Pawn(0, count, count, 1);
            BOARD_PIECES[1][count] = blackPawn;
            ChessPiece whitePawn = new Pawn(1, count, count, 6);
            BOARD_PIECES[6][count] = whitePawn;
        }
        // ChessLibrary.Pieces.Rook setup
        int id = 0; // id for pairs of rook, bishop and knight
        for(int count = 0; count <= 7; count += 7) {
            ChessPiece blackRook = new Rook(0, id, count, 0);
            BOARD_PIECES[0][count] = blackRook;
            ChessPiece whiteRook = new Rook(1, id, count, 7);
            BOARD_PIECES[7][count] = whiteRook;
            id++;
        }
        // ChessLibrary.Pieces.Bishop setup
        id = 0;
        for(int count = 2; count <= 5; count += 3) {
            ChessPiece blackBishop = new Bishop(0, id, count, 0);
            BOARD_PIECES[0][count] = blackBishop;
            ChessPiece whiteBishop = new Bishop(1, id, count, 7);
            BOARD_PIECES[7][count] = whiteBishop;
            id++;
        }
        // if customPiece is true, setup leapers. Else, setup knights.
        id = 0;
        if(!customPiece) {
            for(int count = 1; count <= 6; count += 5) {
                ChessPiece blackKnight = new Knight(0, id, count, 0);
                BOARD_PIECES[0][count] = blackKnight;
                ChessPiece whiteKnight = new Knight(1, id, count, 7);
                BOARD_PIECES[7][count] = whiteKnight;
                id++;
            }
        } else {
            for(int count = 1; count <= 6; count += 5) {
                ChessPiece blackLeaper = new Leaper(0, id, count, 0);
                BOARD_PIECES[0][count] = blackLeaper;
                ChessPiece whiteLeaper = new Leaper(1, id, count, 7);
                BOARD_PIECES[7][count] = whiteLeaper;
                id++;
            }
        }
        // if customPiece is true, setup chargers. Else, setup queens.
        if(!customPiece) {
            ChessPiece blackQueen = new Queen(0, 0, 3, 0);
            BOARD_PIECES[0][3] = blackQueen;
            ChessPiece whiteQueen = new Queen(1, 0, 3, 7);
            BOARD_PIECES[7][3] = whiteQueen;
        } else {
            ChessPiece blackCharger = new Charger(0, 0, 3, 0);
            BOARD_PIECES[0][3] = blackCharger;
            ChessPiece whiteCharger = new Charger(1, 0, 3, 7);
            BOARD_PIECES[7][3] = whiteCharger;
        }
        // ChessLibrary.Pieces.King setup
        ChessPiece blackKing = new King(0, 0, 4, 0);
        BOARD_PIECES[0][4] = blackKing;
        ChessPiece whiteKing = new King(1, 0, 4, 7);
        BOARD_PIECES[7][4] = whiteKing;
    }
}
