package com.example.kevin.shogitest1.android_shogi;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.kevin.shogitest1.R;
import com.example.kevin.shogitest1.game.Game;
import com.example.kevin.shogitest1.grid.Location;
import com.example.kevin.shogitest1.grid.ShogiBoard;
import com.example.kevin.shogitest1.move.IllegalMoveException;
import com.example.kevin.shogitest1.move.InCheckException;
import com.example.kevin.shogitest1.move.Move;
import com.example.kevin.shogitest1.pieces.Bishop;
import com.example.kevin.shogitest1.pieces.GoldGeneral;
import com.example.kevin.shogitest1.pieces.King;
import com.example.kevin.shogitest1.pieces.Knight;
import com.example.kevin.shogitest1.pieces.Lance;
import com.example.kevin.shogitest1.pieces.Pawn;
import com.example.kevin.shogitest1.pieces.Piece;
import com.example.kevin.shogitest1.pieces.Rook;
import com.example.kevin.shogitest1.pieces.SilverGeneral;

public class PlayShogi extends Activity {
	private Square[][] squares;
	private Placer[][] whitePlacers;
	private Placer[][] blackPlacers;
	private ImageView selected;
	private Location selectedLocation;
	private Game game;
	private LinearLayout linearLayout;
	private TableLayout table;
	private TableRow tr;
	private TableLayout whiteCapturedDisplay;
	private TableLayout blackCapturedDisplay;
	private ArrayList<Location> possibleMoves;
	private Display mDisplay;
	private int displayHeight, displayWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDisplay = this.getWindowManager().getDefaultDisplay();
		displayHeight = mDisplay.getHeight();
		setContentView(R.layout.shogiboard);
		squares = new Square[9][9];
		whitePlacers = new Placer[2][4];
		blackPlacers = new Placer[2][4];
		generateBoard();
		game = new Game(new ShogiBoard());
	}

	public void reset(){
		squares = new Square[9][9];
		whitePlacers = new Placer[2][4];
		blackPlacers = new Placer[2][4];
		generateBoard();
		game = new Game(new ShogiBoard());
	}

	/**
	 * Renders the entire board to the screen.
	 */
	private void renderBoard() {
		ShogiBoard board = game.getGrid();
		for (int rank = 8; rank >= 0; rank--) {
			for (int file = 0; file <= 8; file++) {
				Square s = squares[rank][file];
				Location loc = new Location(rank, file);
				if (board.isOccupied(loc)) {
					Piece p = board.get(loc);
					if (p instanceof Pawn) {
						if(p.isPromoted()) {
							s.setImageResource(p.isWhite() ? R.drawable.promotedwhitepawn : R.drawable.promotedblackpawn);
						}else{
							s.setImageResource(p.isWhite() ? R.drawable.whitepawn : R.drawable.blackpawn);
						}
					} else if (p instanceof GoldGeneral) {
						s.setImageResource(p.isWhite() ? R.drawable.whitegoldgeneral : R.drawable.blackgoldgeneral);
					} else if (p instanceof SilverGeneral) {
						if(p.isPromoted()){
							s.setImageResource(p.isWhite() ? R.drawable.promotedwhitesilvergeneral : R.drawable.promotedblacksilvergeneral);
						}else {
							s.setImageResource(p.isWhite() ? R.drawable.whitesilvergeneral : R.drawable.blacksilvergeneral);
						}
					} else if (p instanceof Knight) {
						if(p.isPromoted()){
							s.setImageResource(p.isWhite() ? R.drawable.promotedwhiteknight : R.drawable.promotedblackknight);
						}else {
							s.setImageResource(p.isWhite() ? R.drawable.whiteknight : R.drawable.blackknight);
						}
					} else if (p instanceof Lance) {
						if(p.isPromoted()){
							s.setImageResource(p.isWhite() ? R.drawable.promotedwhitelance : R.drawable.promotedblacklance);
						}else {
							s.setImageResource(p.isWhite() ? R.drawable.whitelance : R.drawable.blacklance);
						}
					} else if (p instanceof Bishop) {
						if(p.isPromoted()){
							s.setImageResource(p.isWhite() ? R.drawable.promotedwhitebishop : R.drawable.promotedblackbishop);
						}else {
							s.setImageResource(p.isWhite() ? R.drawable.whitebishop : R.drawable.blackbishop);
						}
					} else if (p instanceof Rook) {
						if(p.isPromoted()){
							s.setImageResource(p.isWhite() ? R.drawable.promotedwhiterook : R.drawable.promotedblackrook);
						}else {
							s.setImageResource(p.isWhite() ? R.drawable.whiterook : R.drawable.blackrook);
						}
					} else if (p instanceof King) {
						s.setImageResource(p.isWhite() ? R.drawable.whiteking : R.drawable.blackking);
					} else
						System.out.println("Found an unknown piece");
				} else
					s.setImageResource(R.drawable.transparent);
			}
		}
	}

	/**
	 * Generates the board and adds click listeners to each view.
	 */
	private void generateBoard() {
		linearLayout = (LinearLayout) this.findViewById(R.id.shogiboard);
		linearLayout.removeAllViews();
		linearLayout.invalidate();
		linearLayout.refreshDrawableState();
		table = new TableLayout(this);
		table.removeAllViews();
		table.invalidate();
		table.refreshDrawableState();
		whiteCapturedDisplay = new TableLayout(this);
		blackCapturedDisplay = new TableLayout(this);
		whiteCapturedDisplay.removeAllViews();
		blackCapturedDisplay.removeAllViews();
		whiteCapturedDisplay.invalidate();
		blackCapturedDisplay.invalidate();
		whiteCapturedDisplay.refreshDrawableState();
		blackCapturedDisplay.refreshDrawableState();
		linearLayout.addView(blackCapturedDisplay);
		linearLayout.addView(table);
		linearLayout.addView(whiteCapturedDisplay);
		blackCapturedDisplay.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3.0f));
		table.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		whiteCapturedDisplay.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3.0f));
		table.setGravity(Gravity.CENTER);
		table.setStretchAllColumns(true);
		table.setOrientation(LinearLayout.VERTICAL);

		setupBoard();

		for (int i = 0; i < 2; i++) {
			tr = new TableRow(this);
			tr.removeAllViews();
			tr.invalidate();
			tr.refreshDrawableState();
			table.addView(tr);
			tr.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < 4; j++) {
				final int x = i, y = j;
				Placer im = blackPlacers[i][j];
				im.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!game.isWhitesTurn()) {
							if (selected != null) {
								if (selected instanceof Square) {
									Square temp = (Square) selected;
									temp.setBackgroundColor(temp.isWhite() ? Color.WHITE : Color.GRAY);
									for(int i = 0; i < squares.length; i++){
										for(int j = 0; j < squares[i].length; j++){
											if(possibleMoves.contains(squares[i][j].getLocation())){
												squares[i][j].setBackgroundColor(squares[i][j].isWhite() ? Color.WHITE : Color.GRAY);
											}
										}
									}
									possibleMoves = null;
									selected = null;
									selectedLocation = null;
								} else {
									if(selected.equals(blackPlacers[x][y])){
										selected.setBackgroundColor(Color.TRANSPARENT);
										selected = null;
									}else {
										selected.setBackgroundColor(Color.TRANSPARENT);
										selected = blackPlacers[x][y];
										selected.setBackgroundColor(Color.RED);
									}
								}
							} else {
								if (blackPlacers[x][y].hasPieces()) {
									selected = blackPlacers[x][y];
									selected.setBackgroundColor(Color.RED);
								} else {
									Toast.makeText(getBaseContext(), "You have none of this piece to place", Toast.LENGTH_SHORT).show();
								}
							}
						}else{
							Toast.makeText(getBaseContext(), "You may not place your opponents pieces", Toast.LENGTH_SHORT).show();
						}
					}
				});
				if (i == 0) {
					if (j == 0) {
						im.setImageResource(R.drawable.transparent);
					} else if (j == 1) {
						im.setImageResource(R.drawable.blackrook);
					} else if (j == 2) {
						im.setImageResource(R.drawable.blackbishop);
					} else if (j == 3) {
						im.setImageResource(R.drawable.blackgoldgeneral);
					}
				} else if (i == 1) {
					if (j == 0) {
						im.setImageResource(R.drawable.blacksilvergeneral);
					} else if (j == 1) {
						im.setImageResource(R.drawable.blackknight);
					} else if (j == 2) {
						im.setImageResource(R.drawable.blacklance);
					} else if (j == 3) {
						im.setImageResource(R.drawable.blackpawn);
					}
				}
				im.setAdjustViewBounds(true);
				im.setMinimumHeight(displayHeight / 8);
				im.setMinimumWidth(20);
				im.setMaxHeight(displayHeight / 8);
				im.setMaxWidth(20);
				im.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
				tr.addView(im);
			}
		}

		// this is 1-9
		for (int r = 8; r >= 0; r--) {
			tr = new TableRow(this);
			tr.removeAllViews();
			tr.invalidate();
			tr.refreshDrawableState();
			table.addView(tr);
			tr.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			// this is 1-9
			for (int c = 0; c <= 8; c++) {
				final int x = r, y = c;
				Square im = squares[r][c];
				im.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (selected != null) {
							if (selected instanceof Square) {
								if (squares[x][y] == selected) {
									squares[x][y].setBackgroundColor(squares[x][y].isWhite() ? Color.WHITE : Color.GRAY);
									for(int i = 0; i < squares.length; i++){
										for(int j = 0; j < squares[i].length; j++){
											if(possibleMoves.contains(squares[i][j].getLocation())){
												squares[i][j].setBackgroundColor(squares[i][j].isWhite() ? Color.WHITE : Color.GRAY);
											}
										}
									}
									possibleMoves = null;
									selected = null;
									return;
								} else {
									Location src = selectedLocation;
									Location dest = new Location(x, y);
									try {
										boolean inCheck = game.move(src, dest);
										Piece captured = game.getCaptured();
										if (inCheck) {
											Toast.makeText(getBaseContext(), "Check!", Toast.LENGTH_LONG).show();
										}
										if(game.isCheckmate()){
											AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayShogi.this);
											alertDialog.setTitle("Game Over");
											alertDialog.setMessage(game.isWhitesTurn() ? "White Wins" : "Black Wins");

											alertDialog.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													reset();
												}
											});

											alertDialog.setNegativeButton("Return to Title", new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int which) {
													onBackPressed();
												}
											});
											alertDialog.show();
										}
										if (captured != null) {
											if (captured instanceof Pawn) {
												if (captured.isWhite()) {
													whitePlacers[0][0].addPiece(captured);
												} else {
													blackPlacers[1][3].addPiece(captured);
												}
											} else if (captured instanceof Lance) {
												if (captured.isWhite()) {
													whitePlacers[0][1].addPiece(captured);
												} else {
													blackPlacers[1][2].addPiece(captured);
												}
											} else if (captured instanceof Knight) {
												if (captured.isWhite()) {
													whitePlacers[0][2].addPiece(captured);
												} else {
													blackPlacers[1][1].addPiece(captured);
												}
											} else if (captured instanceof SilverGeneral) {
												if (captured.isWhite()) {
													whitePlacers[0][3].addPiece(captured);
												} else {
													blackPlacers[1][0].addPiece(captured);
												}
											} else if (captured instanceof GoldGeneral) {
												if (captured.isWhite()) {
													whitePlacers[1][0].addPiece(captured);
												} else {
													blackPlacers[0][3].addPiece(captured);
												}
											} else if (captured instanceof Bishop) {
												if (captured.isWhite()) {
													whitePlacers[1][1].addPiece(captured);
												} else {
													blackPlacers[0][2].addPiece(captured);
												}
											} else if (captured instanceof Rook) {
												if (captured.isWhite()) {
													whitePlacers[1][2].addPiece(captured);
												} else {
													blackPlacers[0][1].addPiece(captured);
												}
											}
										}
										renderBoard();
									} catch (IllegalMoveException e) {
										StringBuilder sb = new StringBuilder();
										sb.append(e.getPiece() + " cannot move to " + e.getLocation() + ".");
										sb.append('\n');

										ArrayList<Location> locs = e.getPiece().getMoves();
										if (locs.isEmpty())
											sb.append("This piece has no available moves.");
										else {
											sb.append(e.getPiece() + " can move to: ");
											for (Location loc : e.getPiece().getMoves()) {
												sb.append(loc);
												sb.append(' ');
											}
										}
										Toast.makeText(getBaseContext(), sb.toString(), Toast.LENGTH_LONG).show();
										return;
									} catch (InCheckException e) {
										StringBuilder sb = new StringBuilder();
										sb.append("Unable to make the move " + src.toString() + " to " + dest.toString() + " while in check.\n");
										sb.append("You can make the following moves:");
										for (Move available : e.getAvailableMoves()) {
											sb.append(available.toString() + ", ");
										}
										Toast.makeText(getBaseContext(), sb.toString(), Toast.LENGTH_LONG).show();
										return;
									} finally {
										if (selected instanceof Square) {
											Square temp = (Square) selected;
											temp.setBackgroundColor(temp.isWhite() ? Color.WHITE : Color.GRAY);
											for(int i = 0; i < squares.length; i++){
												for(int j = 0; j < squares[i].length; j++){
													if(possibleMoves.contains(squares[i][j].getLocation())){
														squares[i][j].setBackgroundColor(squares[i][j].isWhite() ? Color.WHITE : Color.GRAY);
													}
												}
											}
											possibleMoves = null;
											selected = null;
											selectedLocation = null;
										}
									}
								}
							} else if (selected instanceof Placer) {
								Placer toPlace = (Placer) selected;
								game.addPiece(toPlace.getPiece(), new Location(x, y));
								selected.setBackgroundColor(Color.TRANSPARENT);
								selected = null;
								renderBoard();
							}
						} else {
							Location loc = new Location(x, y);
							if (game.getGrid().get(loc) == null) {
								return;
							}

							Piece p = game.getGrid().get(loc);
							if (p.isWhite() != game.isWhitesTurn()) {
								Toast.makeText(getBaseContext(), "You can only move your own pieces.\nIt's "
												+ (game.isWhitesTurn() ? "white's" : "black's") + " turn.",
										Toast.LENGTH_SHORT).show();
								return;
							}
							// select this piece
							squares[x][y].toggleSelected();
							selected = squares[x][y];
							possibleMoves = p.getMoves();
							for(int i = 0; i < squares.length; i++){
								for(int j = 0; j < squares[i].length; j++){
									if(possibleMoves.contains(squares[i][j].getLocation())){
										squares[i][j].setBackgroundColor(Color.BLUE);
									}
								}
							}
							selectedLocation = new Location(x, y);
							squares[x][y].setBackgroundColor(Color.RED);
						}
					}
				});

				// black back row
				if (r == 8) {
					// lance
					if (c == 0 || c == 8) {
						im.setImageResource(R.drawable.blacklance);
					}
					// knight
					else if (c == 1 || c == 7) {
						im.setImageResource(R.drawable.blackknight);
					}
					// silver general
					else if (c == 2 || c == 6) {
						im.setImageResource(R.drawable.blacksilvergeneral);
					}
					// gold general
					else if (c == 3 || c == 5) {
						im.setImageResource(R.drawable.blackgoldgeneral);
					}
					// king
					else if (c == 4) {
						im.setImageResource(R.drawable.blackking);
					}
				} else if (r == 7) {
					//Rook
					if (c == 1) {
						im.setImageResource(R.drawable.blackrook);
					}
					//Bishop
					else if (c == 7) {
						im.setImageResource(R.drawable.blackbishop);
					} else {
						im.setImageResource(R.drawable.transparent);
					}
				}
				// black pawns
				else if (r == 6) {
					im.setImageResource(R.drawable.blackpawn);
				}
				// white pawns
				else if (r == 2) {
					im.setImageResource(R.drawable.whitepawn);
				} else if (r == 1) {
					//white Bishop
					if (c == 1) {
						im.setImageResource(R.drawable.whitebishop);
					}
					//white Rook
					else if (c == 7) {
						im.setImageResource(R.drawable.whiterook);
					} else {
						im.setImageResource(R.drawable.transparent);
					}
				}
				// white back row
				else if (r == 0) {
					// lance
					if (c == 0 || c == 8) {
						im.setImageResource(R.drawable.whitelance);
					}
					// knight
					else if (c == 1 || c == 7) {
						im.setImageResource(R.drawable.whiteknight);
					}
					// silver general
					else if (c == 2 || c == 6) {
						im.setImageResource(R.drawable.whitesilvergeneral);
					}
					// gold general
					else if (c == 3 || c == 5) {
						im.setImageResource(R.drawable.whitegoldgeneral);

					}
					// king
					else if (c == 4) {
						im.setImageResource(R.drawable.whiteking);
					}
				} else {
					im.setImageResource(R.drawable.transparent);
				}
				im.setAdjustViewBounds(true);
				im.setMinimumHeight(displayHeight / 8);
				im.setMinimumWidth(20);
				im.setMaxHeight(displayHeight / 8);
				im.setMaxWidth(20);
				if (squares[r][c].isWhite()) {
					im.setBackgroundColor(Color.WHITE);
				} else {
					im.setBackgroundColor(Color.GRAY);
				}

				im.setLayoutParams(new TableRow.LayoutParams(
						TableRow.LayoutParams.WRAP_CONTENT,
						TableRow.LayoutParams.WRAP_CONTENT));
				tr.addView(im);
			}
		}

		for (int i = 0; i < 2; i++) {
			tr = new TableRow(this);
			tr.removeAllViews();
			tr.invalidate();
			tr.refreshDrawableState();
			table.addView(tr);
			tr.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			for (int j = 0; j < 4; j++) {
				final int x = i, y = j;
				Placer im = whitePlacers[i][j];
				im.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (game.isWhitesTurn()) {
							if (selected != null) {
								if (selected instanceof Square) {
									Square temp = (Square) selected;
									temp.setBackgroundColor(temp.isWhite() ? Color.WHITE : Color.GRAY);
									for(int i = 0; i < squares.length; i++){
										for(int j = 0; j < squares[i].length; j++){
											if(possibleMoves.contains(squares[i][j].getLocation())){
												squares[i][j].setBackgroundColor(squares[i][j].isWhite() ? Color.WHITE : Color.GRAY);
											}
										}
									}
									possibleMoves = null;
									selected = null;
									selectedLocation = null;
								} else {
									if (whitePlacers[x][y].hasPieces()) {
										selected.setBackgroundColor(Color.TRANSPARENT);
										if(selected.equals(whitePlacers[x][y])){
											selected = null;
										}else {
											selected = whitePlacers[x][y];
											selected.setBackgroundColor(Color.RED);
										}
									} else {
										selected.setBackgroundColor(Color.TRANSPARENT);
										selected = null;
										Toast.makeText(getBaseContext(), "You have none of this piece to place", Toast.LENGTH_SHORT).show();
									}
								}
							} else {
								if (whitePlacers[x][y].hasPieces()) {
									selected = whitePlacers[x][y];
									selected.setBackgroundColor(Color.RED);
								} else {
									Toast.makeText(getBaseContext(), "You have none of this piece to place", Toast.LENGTH_SHORT).show();
								}
							}
						}else{
							Toast.makeText(getBaseContext(), "You may not place your opponents pieces", Toast.LENGTH_SHORT).show();
						}
					}
				});
				if (i == 0) {
					if (j == 0) {
						im.setImageResource(R.drawable.whitepawn);
					} else if (j == 1) {
						im.setImageResource(R.drawable.whitelance);
					} else if (j == 2) {
						im.setImageResource(R.drawable.whiteknight);
					} else if (j == 3) {
						im.setImageResource(R.drawable.whitesilvergeneral);
					}
				} else if (i == 1) {
					if (j == 0) {
						im.setImageResource(R.drawable.whitegoldgeneral);
					} else if (j == 1) {
						im.setImageResource(R.drawable.whitebishop);
					} else if (j == 2) {
						im.setImageResource(R.drawable.whiterook);
					} else if (j == 3) {
						im.setImageResource(R.drawable.transparent);
					}
				}
				im.setAdjustViewBounds(true);
				im.setMinimumHeight(displayHeight / 8);
				im.setMinimumWidth(20);
				im.setMaxHeight(displayHeight / 8);
				im.setMaxWidth(20);
				im.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
				tr.addView(im);
			}
			setContentView(linearLayout);
		}
		return;
	}

	private void setupBoard() {
		for (int x = 8; x >= 0; x--) {
			for (int y = 0; y <= 8; y++) {
				final int r = x;
				final int c = y;
				Location location = new Location(r,c);
				squares[r][c] = new Square(this, (((r + c) % 2) != 0) ? true : false, location);
			}
		}

		for(int x = 1; x >= 0; x--){
			for (int y = 0; y <= 3; y++) {
				final int r = x;
				final int c = y;
				blackPlacers[r][c] = new Placer(this);
			}
		}

		for(int x = 1; x >= 0; x--){
			for (int y = 0; y <= 3; y++) {
				final int r = x;
				final int c = y;
				whitePlacers[r][c] = new Placer(this);
			}
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Closing Activity")
		        .setMessage("Are you sure you want to quit this game? The game will not be saved.")
		        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			                @Override
			                public void onClick(DialogInterface dialog,
			                        int which) { finish(); }

		                }).setNegativeButton("No", null).show();
	}
}