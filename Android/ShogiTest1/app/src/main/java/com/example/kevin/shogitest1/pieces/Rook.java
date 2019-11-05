package com.example.kevin.shogitest1.pieces;


import com.example.kevin.shogitest1.game.Game;
import com.example.kevin.shogitest1.grid.Location;

import java.util.ArrayList;

/**
 * Rooks can move in any direction vertically or horizontally
 * Promoted rooks can move diagnolly a single space as well
 * @author Kevin
 */
public class Rook extends Piece {

	public Rook(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
		promoted = false;
	}

	@Override
	public String type() {
		return "Rook";
	}
	
	@Override
	public Piece copy() {
		return  new Rook(isWhite(), getGame(), getLocation().copy());
	}
	
	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		if(promoted){
			for(int dir = 45; dir < 360; dir += 90) {
				Location l = getLocation().getAdjacentLocation(dir);
				if(getGrid().isValid(l)) {
					locs.add(l);
				}
			}
		}
		for(int dir = 0; dir < 360; dir += 90) {
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

	@Override
	public char toFEN() {
		return isWhite() ? 'R' : 'r';
	}
}