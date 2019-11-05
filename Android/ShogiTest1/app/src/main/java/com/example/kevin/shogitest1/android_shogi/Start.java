package com.example.kevin.shogitest1.android_shogi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.kevin.shogitest1.R;

public class Start extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
	}

	/**
	 * Takes user to Shogi game activity.
	 * @param view
	 */
	public void playGame(View view) {
		Intent intent = new Intent(this, PlayShogi.class);
		startActivity(intent);
	}

	/**
	 * Takes user to Rules activity.
	 * @param view
	 */
	public void showRules(View view) {
		Intent intent = new Intent(this, Rules.class);
		startActivity(intent);
	}
}