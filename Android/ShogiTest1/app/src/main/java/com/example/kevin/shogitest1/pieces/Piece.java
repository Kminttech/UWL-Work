package com.example.kevin.shogitest1.pieces;


import com.example.kevin.shogitest1.game.Game;
import com.example.kevin.shogitest1.grid.Grid;
import com.example.kevin.shogitest1.grid.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Represents a chess piece or pawn.
 * @author Kevin
 */
public abstract class Piece {
	private boolean color;
	private Game game;
	private Location location;
	private ArrayList<Location> moves;
	boolean promoted;
	
	/**
	 * Create a new chess piece.
	 * @param isWhite The color of this piece. True if it is white; false if it
	 * is black.
	 * //@param grid The chessboard on which this piece resides.
	 * @param game The game manager for the current game.
	 * @param location The piece's location on its grid.
	 */
	public Piece(boolean isWhite, Game game, Location location) {
		super();
		if(game == null)
			throw new IllegalArgumentException("Invalid game object.");
		this.color = isWhite;
		this.game = game;
		this.location = location;
		if(location != null)
			this.moves = getMoveLocations();
	}
	
	/**
	 * Determines whether or not this piece can validly move to that location.
	 * @param loc The location in question.
	 * @return True if this piece can move to that location; false otherwise.
	 */
	public boolean canMove(Location loc) {
		moves = getMoveLocations();
		return moves.contains(loc);
	}
	
	/**
	 * @return A copy of this piece.
	 */
	public abstract Piece copy();
	
	/**
	 * Two pieces are equal if and only if they are the same color and occupy
	 * the same location.
	 * @param other The object to compare to.
	 * @return True if <code>other</code> is a matching piece; false otherwise.
	 */
	@Override
	public boolean equals(Object other) {
		if(other == null || !(other instanceof Piece)) {
			return false;
		}
		
		Piece p = (Piece) other;
		if(location == null)
			return p.location == null && color == p.color;
		else
			return color == p.color && location.equals(p.location);
	}
	
	/**
	 * @return All locations attacked by this piece.
	 */
	public abstract ArrayList<Location> getAttackedLocations();
	
	/**
	 * @return The game this piece is taking part in.
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * @return The grid on which this piece lies.
	 */
	public Grid<Piece> getGrid() {
		return game.getGrid();
	}
	
	/**
	 * @return The location this piece is located on.
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * @return A list of moves that this piece can move to, to its best
	 * knowledge. If this piece has no location, this method returns null.
	 */
	public ArrayList<Location> getMoves() {
		return moves;
	}
	
	/**
	 * @return A short, two-character String describing this piece briefly.
	 */
	public String getPieceCode() {
		StringBuilder sb = new StringBuilder(2);
		sb.append(isWhite() ? 'w' : 'b');
		sb.append(Character.toUpperCase(toFEN()));
		return sb.toString();
	}
	
	/**
	 * @return True if this is a white piece; false if it is black.
	 */
	public boolean isWhite() {
		return color;
	}
	
	/**
	 * Sets the location of this piece. Note that this method will allow a
	 * change to location even if the piece cannot legally move there.
	 * @param loc This piece's new location.
	 */
	public void setLocation(Location loc) {
		location = loc;
	}

	/**
	 * @return The FEN abbreviation for this piece.
	 */
	public abstract char toFEN();

	public abstract String type();
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		if(location != null) {
			sb.append(" at ");
			sb.append(location.toString());
		}
		return sb.toString();
	}
	
	public void update() {
		moves = getMoveLocations();
	}
	
	/**
	 * @return An ArrayList containing all the valid locations on this piece's
	 * grid that it can move to.
	 */
	protected ArrayList<Location> getMoveLocations(){
		ArrayList<Location> locs = getAttackedLocations();
		Iterator<Location> i = locs.iterator();
		while(i.hasNext()) {
			Location loc = i.next();
			if(getGrid().isOccupied(loc) && isSameColor(getGrid().get(loc)))
				i.remove();
		}
		return locs;
	}
	
	/**
	 * @param p Another piece.
	 * @return True if the two pieces are the same color; false otherwise.
	 */
	boolean isSameColor(Piece p) {
		return color == p.color;
	}

	public void promote(){ promoted = true; }

	public void demote(){ promoted = false; }

	public boolean isPromoted(){ return promoted; }

	public void flipSide(){ color = !color; }
}