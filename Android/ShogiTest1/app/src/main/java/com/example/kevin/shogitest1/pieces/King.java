package com.example.kevin.shogitest1.pieces;

import com.example.kevin.shogitest1.game.Game;
import com.example.kevin.shogitest1.grid.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;



/**
 * The King is able to move one space in any direction
 * The loss of the king is the loss of the game
 * @author Kevin
 */
public class King extends Piece {

	public King(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
		promoted = true;
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'K' : 'k';
	}

	@Override
	public String type() {
		return "King";
	}
	
	@Override
	public Piece copy() {
		return new King(isWhite(), getGame(), getLocation().copy());
	}
	
	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>(8);
		for(int dir = 0; dir < 360; dir += 45) {
			Location l = getLocation().getAdjacentLocation(dir);
			if(getGrid().isValid(l)) {
				locs.add(l);
			}
		}
		return locs;
	}

	/**
	 * @return An ArrayList containing all the valid locations on this piece's
	 * grid that it can move to.
	 */
	@Override
	protected ArrayList<Location> getMoveLocations(){
		ArrayList<Location> locs = getAttackedLocations();
		HashSet<Location> invalid = isWhite() ? getGame().getLocsControlledByBlack() : getGame().getLocsControlledByWhite();
		Iterator<Location> i = locs.iterator();
		while(i.hasNext()) {
			Location loc = i.next();
			if(invalid.contains(loc) || (getGrid().isOccupied(loc) && isSameColor(getGrid().get(loc))))
				i.remove();
		}
		return locs;
	}
}