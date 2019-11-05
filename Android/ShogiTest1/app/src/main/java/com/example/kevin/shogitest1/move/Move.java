package com.example.kevin.shogitest1.move;


import com.example.kevin.shogitest1.grid.Location;

/**
 * Represents a move on a chessboard.
 * @author Kevin
 */
public class Move {
	private Location src, dest;

	/**
	 * Creates an object to represent a single move. The special field
	 * indicates a special type of move.
	 * @param src The source location of the piece moving.
	 * @param dest Its destination location.
	 */
	public Move(Location src, Location dest) {
		super();
		this.src = src;
		this.dest = dest;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Move))
			return false;
		
		Move m = (Move) o;
		return m.src.equals(src) && m.dest.equals(dest);
	}
	
	/**
	 * @return The destination location.
	 */
	public Location getDestination() {
		return dest;
	}

	/**
	 * @return The source location.
	 */
	public Location getSource() {
		return src;
	}

	@Override
	public String toString() {
		return getSource() + " to " + getDestination();
	}

	@Override
	public int hashCode(){
		return src.hashCode() + dest.hashCode();
	}
} 