package com.example.kevin.shogitest1.game;

import com.example.kevin.shogitest1.grid.Location;
import com.example.kevin.shogitest1.grid.ShogiBoard;
import com.example.kevin.shogitest1.move.IllegalMoveException;
import com.example.kevin.shogitest1.move.InCheckException;
import com.example.kevin.shogitest1.move.InvalidLocationException;
import com.example.kevin.shogitest1.move.Move;
import com.example.kevin.shogitest1.pieces.Bishop;
import com.example.kevin.shogitest1.pieces.GoldGeneral;
import com.example.kevin.shogitest1.pieces.King;
import com.example.kevin.shogitest1.pieces.Knight;
import com.example.kevin.shogitest1.pieces.Lance;
import com.example.kevin.shogitest1.pieces.Pawn;
import com.example.kevin.shogitest1.pieces.Piece;
import com.example.kevin.shogitest1.pieces.Rook;
import com.example.kevin.shogitest1.pieces.SilverGeneral;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Manages the user interface and the back-end.
 * @author Kevin
 */
public class Game {
    // Pieces and grid information
    private ArrayList<Piece> pieces;
    private Piece captured;
    private ShogiBoard grid;
    private HashSet<Location> white_locs, black_locs;

    // Move and turn information
    private boolean whiteTurn;

    // Castling information
    private King K, k;

    // Moves when in check
    private HashSet<Move> escape_moves;

    /**
     * Creates a new game on the given board.
     *
     * @param grid The board on which the game will be played.
     */
    public Game(ShogiBoard grid) {
        super();
        this.grid = grid;
        this.black_locs = new HashSet<Location>(32);
        this.white_locs = new HashSet<Location>(32);
        this.whiteTurn = true;
        defaultPosition();
        updateControlledLocations();
        for (Piece p : pieces) {
            p.update();
        }
    }

    /**
     * @return All active, non-captured pieces still on the board.
     */
    public ArrayList<Piece> getActivePieces() {
        return pieces;
    }

    /**
     * @return The grid on which this game is played.
     */
    public ShogiBoard getGrid() {
        return grid;
    }

    /**
     * @return The set of all locations that are attacked by a black piece.
     * Locations occupied by a black piece but not defended by another piece
     * are not included in this set.
     */
    public HashSet<Location> getLocsControlledByBlack() {
        return black_locs;
    }

    /**
     * @return The set of all locations that are attacked by a white piece.
     * Locations occupied by a white piece but not defended by another piece
     * are not included in this set.
     */
    public HashSet<Location> getLocsControlledByWhite() {
        return white_locs;
    }

    public Piece getCaptured(){
        return captured;
    }

    public boolean isCheckmate() {
        return isInCheck() && getEscapeMoves().isEmpty();
    }

    /**
     * @return True if it's white's turn; false for black.
     */
    public boolean isWhitesTurn() {
        return whiteTurn;
    }

    /**
     * Moves the piece at the source location to the specified destination. <br>
     * Precondition: There exists a piece at location <code>src</code>.
     *
     * @param src  The starting location of the piece.
     * @param dest The desired location to move the piece.
     * @return True if the enemy king is put in check; false otherwise.
     * @throws InvalidLocationException If either location is not in range.
     * @throws IllegalMoveException     If the attempted move is not valid.
     */
    public boolean move(Location src, Location dest) throws InvalidLocationException, IllegalMoveException {
        if (!grid.isValid(src))
            throw new InvalidLocationException(src);
        if (!grid.isValid(dest))
            throw new InvalidLocationException(dest);

        Piece p = grid.get(src);

        if (!p.canMove(dest))
            throw new IllegalMoveException(p, dest);
        Move move = new Move(src, dest);


        if (isInCheck() && !escape_moves.contains(move)) {
            throw new InCheckException(escape_moves);
        }

        captured = grid.get(dest);

        // Time to actually move the piece.
        Piece target = grid.put(dest, p);
        if (target != null) {
            pieces.remove(target);
        }
        updateControlledLocations();

        // Cannot make moves that put your own king in check.
        if (isInCheck()) {
            grid.put(src, p);
            if (target != null) {
                grid.put(dest, target);
                pieces.add(target);
            } else
                grid.remove(dest);
            updateControlledLocations();
            throw new IllegalMoveException("Moving " + p + " will place the king in check.", p, dest);
        }

        if(captured != null){
            captured.demote();
            if(captured.isWhite()){
                captured.flipSide();
            }else{
                captured.flipSide();
            }
            pieces.remove(captured);
        }

        if((p.isWhite() && p.getLocation().getRank() >= 6) || (!p.isWhite() && p.getLocation().getRank() <= 2)){
            p.promote();
            p.update();
        }
        // Switch turns.
        whiteTurn = !whiteTurn;

        boolean inCheck = isInCheck();
        for (Piece piece : pieces)
            piece.update();

        if (inCheck) {
            escape_moves = getEscapeMoves();
        } else {
            escape_moves = null;
            updateAllowedMoves();
        }

        return inCheck;
    }

    /**
     * @param grid Swaps out this game's shogi for the given board.
     */
    public void setGrid(ShogiBoard grid) {
        this.grid = grid;
        pieces.clear();
        for (Entry<Location, Piece> e : grid.getEntrySet()) {
            e.getValue().setLocation(e.getKey());
            pieces.add(e.getValue());
        }
    }

    public boolean addPiece(Piece p, Location loc){
        if(!pieces.contains(p)) {
            pieces.add(p);
            grid.put(loc, p);
            whiteTurn = !whiteTurn;
            return true;
        }
        return false;
    }

    /**
     * Sets up a brand new game of Shogi. Creates a new pieces and sets up the
     * pieces on the grid.
     */
    private void defaultPosition() {
        pieces = new ArrayList<Piece>(32);
        // Kings
        K = new King(true, this, new Location(0, 4));
        k = new King(false, this, new Location(8, 4));
        pieces.add(k);
        pieces.add(K);

        // Pawns
        for (int i = 0; i < 9; i++) {
            Pawn p1 = new Pawn(true, this, new Location(2, i));
            Pawn p2 = new Pawn(false, this, new Location(6, i));
            pieces.add(p1);
            pieces.add(p2);
        }

        // Knights
        Knight n1, n2, n3, n4;
        n1 = new Knight(true, this, new Location(0, 1));
        n2 = new Knight(true, this, new Location(0, 7));
        n3 = new Knight(false, this, new Location(8, 1));
        n4 = new Knight(false, this, new Location(8, 7));
        pieces.add(n1);
        pieces.add(n2);
        pieces.add(n3);
        pieces.add(n4);

        // Bishops
        Bishop b1, b2;
        b1 = new Bishop(true, this, new Location(1, 1));
        b2 = new Bishop(false, this, new Location(7, 7));
        pieces.add(b1);
        pieces.add(b2);

        // Rooks
        Rook r1, r2;
        r1 = new Rook(true, this, new Location(1, 7));
        r2 = new Rook(false, this, new Location(7, 1));
        pieces.add(r1);
        pieces.add(r2);

        // Lances
        Lance l1, l2, l3, l4;
        l1 = new Lance(true, this, new Location(0, 0));
        l2 = new Lance(true, this, new Location(0, 8));
        l3 = new Lance(false, this, new Location(8, 0));
        l4 = new Lance(false, this, new Location(8, 8));
        pieces.add(l1);
        pieces.add(l2);
        pieces.add(l3);
        pieces.add(l4);

        // Silver Generals
        SilverGeneral s1, s2, s3, s4;
        s1 = new SilverGeneral(true, this, new Location(0, 2));
        s2 = new SilverGeneral(true, this, new Location(0, 6));
        s3 = new SilverGeneral(false, this, new Location(8, 2));
        s4 = new SilverGeneral(false, this, new Location(8, 6));
        pieces.add(s1);
        pieces.add(s2);
        pieces.add(s3);
        pieces.add(s4);

        // Gold Generals
        GoldGeneral g1, g2, g3, g4;
        g1 = new GoldGeneral(true, this, new Location(0, 3));
        g2 = new GoldGeneral(true, this, new Location(0, 5));
        g3 = new GoldGeneral(false, this, new Location(8, 3));
        g4 = new GoldGeneral(false, this, new Location(8, 5));
        pieces.add(g1);
        pieces.add(g2);
        pieces.add(g3);
        pieces.add(g4);

        for (Piece p : pieces) {
            grid.put(p.getLocation(), p);
        }
    }

    /**
     * @return A set of valid moves that takes the king out of check.
     */
    private HashSet<Move> getEscapeMoves(){
        HashSet<Move> result = new HashSet<Move>();
        ArrayList<Piece> all_pieces = new ArrayList<Piece>(pieces.size());
        for (Piece p : pieces)
            all_pieces.add(p);

        for (Piece p : all_pieces){
            if (p.isWhite() != isWhitesTurn())
                continue;
            Location prev = p.getLocation();
            for(Location loc : p.getMoves()){
                Piece captured = grid.put(loc, p);
                if (captured != null)
                    pieces.remove(captured);
                updateControlledLocations();
                if(!isInCheck())
                    result.add(new Move(prev, loc));
                grid.put(prev, p);
                if(captured != null){
                    grid.put(loc, captured);
                    pieces.add(captured);
                }else
                    grid.remove(loc);
                updateControlledLocations();
            }
        }
        return result;
    }

    /**
     * @return True if the current player moving is in check; false otherwise.
     */
    private boolean isInCheck() {
        return isWhitesTurn() ? getLocsControlledByBlack().contains(K.getLocation()) : getLocsControlledByWhite().contains(k.getLocation());
    }

    private void updateAllowedMoves() {
        HashSet<Location> enemy = isWhitesTurn() ? black_locs : white_locs;
        King our_king = isWhitesTurn() ? K : k;
        ArrayList<Piece> all_pieces = new ArrayList<Piece>(pieces.size());
        for (Piece p : pieces){
            all_pieces.add(p);
        }

        for (Piece p : all_pieces){
            if ((isWhitesTurn() != p.isWhite()) || p instanceof King)
                continue;

            Location prev = p.getLocation();
            Iterator<Location> i = p.getMoves().iterator();
            while (i.hasNext()) {
                Location loc = i.next();
                Piece captured = grid.put(loc, p);
                if (captured != null)
                    pieces.remove(captured);
                updateControlledLocations();
                if (enemy.contains(our_king.getLocation())) {
                    i.remove();
                }
                grid.put(prev, p);
                if (captured != null) {
                    grid.put(loc, captured);
                    pieces.add(captured);
                } else
                    grid.remove(loc);
                updateControlledLocations();
            }
        }
    }

    private void updateControlledLocations(){
        white_locs.clear();
        black_locs.clear();
        for (Piece p : pieces) {
            if (p.isWhite())
                white_locs.addAll(p.getAttackedLocations());
            else
                black_locs.addAll(p.getAttackedLocations());
        }
    }
}