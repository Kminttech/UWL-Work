package com.example.kevin.shogitest1.android_shogi;

import android.content.Context;
import android.widget.ImageView;

import com.example.kevin.shogitest1.grid.Location;
import com.example.kevin.shogitest1.pieces.Piece;


public class Square extends ImageView {
	private boolean isWhite, isSelected;
	private Piece piece;
	private Location location;
	
	public Square(Context context, Boolean isWhite, Location loc) {
		super(context);
		this.isWhite = isWhite;
		this.location = loc;
		this.setClickable(true);
	}
	
	public Location getLocation(){
		return location;
	}
	
	public boolean isWhite(){
		return this.isWhite;
	}

	public boolean isSelected(){
		return this.isSelected;
	}
	
	public Piece getPiece(){
		return this.piece;
	}
	
	public void addPiece(Piece p){ 
		this.piece = p;
	}
	
	public void removePiece(){
		if (this.piece == null){
			return;
		}
		this.piece = null;
		return;
	}
	
	public void toggleSelected(){
		isSelected = !isSelected;
	}
}

