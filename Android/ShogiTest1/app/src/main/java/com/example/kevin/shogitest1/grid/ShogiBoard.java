package com.example.kevin.shogitest1.grid;

import com.example.kevin.shogitest1.pieces.Piece;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Represents an 9x9 shogiboard, made of locations that hold Shogi pieces.
 * @author Kevin
 */
public class ShogiBoard implements Grid<Piece> {
	private HashMap<Location, Piece> map;
	private boolean inverted;
	
	/**
	 * Create a new empty Shogiboard.
	 */
	public ShogiBoard() {
		super();
		map = new HashMap<Location, Piece>(40);
	}
	
	public ShogiBoard(Set<Entry<Location, Piece>> entrySet) {
		super();
		map = new HashMap<Location, Piece>(40);
		for(Entry<Location, Piece> e : entrySet) {
			map.put(e.getKey().copy(), e.getValue().copy());
		}
	}
	
	/**
	 * @return A set containing all of the mappings in the chessboard.
	 */
	public Set<Entry<Location, Piece>> getEntrySet() {
		return map.entrySet();
	}
	
	@Override
	public void invert() {
		inverted = !inverted;
	}
	
	@Override
	public boolean isInverted() {
		return inverted;
	}

	@Override
	public Piece get(Location loc) {
		return map.get(loc);
	}

	@Override
	public int getCols() {
		return 9;
	}

	@Override
	public int getRows() {
		return 9;
	}

	@Override
	public boolean isOccupied(Location loc) {
		return map.containsKey(loc);
	}

	@Override
	public boolean isValid(Location loc) {
		return (loc.getFile() >= 0 && loc.getFile() < 9) && (loc.getRank() >= 0 && loc.getRank() < 9);
	}
	
	@Override
	public Piece put(Location loc, Piece piece) {
		if(piece == null)
			throw new IllegalArgumentException("Piece cannot be null.");
		
		if(piece.getLocation() != null && !piece.getLocation().equals(loc)) {
			Location prev = piece.getLocation();
			map.remove(prev);
		}
		
		Piece other = map.get(loc);
		piece.setLocation(loc);
		if(other != null)
			other.setLocation(null);
		map.put(loc, piece);
		
		return other;
	}
	
	@Override
	public void remove(Location loc) {
		map.remove(loc);
	}
}