package com.example.kevin.shogitest1.pieces;

import com.example.kevin.shogitest1.game.Game;
import com.example.kevin.shogitest1.grid.Location;

import java.util.ArrayList;


/**
 * The knight moves forward 2 and over one left or right
 * Promotion to move like Gold General
 * @author Kevin
 */
public class Knight extends Piece {

	public Knight(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
		promoted = false;
	}
	
	@Override
	public Piece copy() {
		return new Knight(isWhite(), getGame(), getLocation().copy());
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'N' : 'n';
	}

	@Override
	public String type() {
		return "Knight";
	}

	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>(8);
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
			if(getGrid().isValid(l)){
				locs.add(l);
			}
		}else {
			int dir = isWhite() ? 0 : 180;
			Location l = getLocation().getAdjacentLocation(dir).getAdjacentLocation(dir);
			Location l1 = l.getAdjacentLocation(dir + 90);
			Location l2 = l.getAdjacentLocation(dir - 90);

			if(getGrid().isValid(l1)){
				locs.add(l1);
			}
			if(getGrid().isValid(l2)){
				locs.add(l2);
			}
		}
		return locs;
	}
}