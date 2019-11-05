package com.example.kevin.shogitest1.pieces;


import com.example.kevin.shogitest1.game.Game;
import com.example.kevin.shogitest1.grid.Location;

import java.util.ArrayList;

/**
 * Bishops move along diagonals and single spaces vertially when promoted
 * @author Kevin
 */
public class Bishop extends Piece {

	public Bishop(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
		promoted = false;
	}
	
	@Override
	public Piece copy() {
		return new Bishop(isWhite(), getGame(), getLocation().copy());
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'B' : 'b';
	}

	@Override
	public String type() {
		return "Bishop";
	}

	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		if(promoted){
			if(promoted){
				for(int dir = 0; dir < 360; dir += 90) {
					Location l = getLocation().getAdjacentLocation(dir);
					if(getGrid().isValid(l)) {
						locs.add(l);
					}
				}
			}
		}
		for(int i = 0; i < 4; i++) {
			int dir = 45 + 90*i;
			Location l = getLocation().getAdjacentLocation(dir);
			while(getGrid().isValid(l)) {
				locs.add(l);
				if(getGrid().isOccupied(l))
					break;
				l = l.getAdjacentLocation(dir);
			}
		}
		return locs;
	}
}