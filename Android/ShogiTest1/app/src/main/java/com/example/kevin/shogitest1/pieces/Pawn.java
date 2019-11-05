package com.example.kevin.shogitest1.pieces;


import com.example.kevin.shogitest1.game.Game;
import com.example.kevin.shogitest1.grid.Location;

import java.util.ArrayList;


/**
 * They can move one space forward.
 * Promotion to move like Gold General
 * @author Kevin
 */
public class Pawn extends Piece {

	public Pawn(boolean isWhite, Game game, Location loc) {
		super(isWhite, game, loc);
		promoted = false;
	}

	@Override
	public String type() {
		return "Pawn";
	}
	
	@Override
	public Piece copy() {
		return new Pawn(isWhite(), getGame(), getLocation().copy());
	}
	
	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>(1);
		if(promoted){
			int dir = isWhite() ? 0 : 180;
			for(int dirChange = 0; dirChange <= 90; dirChange += 45) {
				Location l1 = getLocation().getAdjacentLocation(dir+dirChange);
				if(getGrid().isValid(l1)) {
					locs.add(l1);
				}
				if(dirChange!=0) {
					Location l2 = getLocation().getAdjacentLocation(dir - dirChange);
					if(getGrid().isValid(l2)) {
						locs.add(l2);
					}
				}
			}
			dir = isWhite() ? 180 : 0;
			Location l = getLocation().getAdjacentLocation(dir);
			if(getGrid().isValid(l)) {
				locs.add(l);
			}
		}else {
			int dir = isWhite() ? 0 : 180;
			Location loc = getLocation().getAdjacentLocation(dir);
			if (getGrid().isValid(loc)) {
				locs.add(loc);
			}
		}
		return locs;
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'P' : 'p';
	}
	
	private void overrideLocation(Location loc) {
		super.setLocation(loc);
	}
}