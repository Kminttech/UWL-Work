package com.example.kevin.shogitest1.android_shogi;

import android.content.Context;
import android.widget.ImageView;

import com.example.kevin.shogitest1.pieces.Piece;

import java.util.ArrayList;

/**
 * Created by Kevin on 4/23/2016.
 */
public class Placer extends ImageView{
    private boolean isSelected;
    private ArrayList<Piece> pieces;

    public Placer(Context context) {
        super(context);
        pieces = new ArrayList<Piece>();
        this.setClickable(true);
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public Piece getPiece(){
        return this.pieces.remove(0);
    }

    public boolean hasPieces(){
        return !pieces.isEmpty();
    }

    public void addPiece(Piece p){ this.pieces.add(p); }

    public void toggleSelected(){
        isSelected = !isSelected;
    }
}
