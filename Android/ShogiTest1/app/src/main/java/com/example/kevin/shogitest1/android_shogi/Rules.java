package com.example.kevin.shogitest1.android_shogi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.kevin.shogitest1.R;

/**
 * Created by Kevin on 4/23/2016.
 */
public class Rules extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_page);
    }

    /**
     * Takes user to Main activity.
     * @param view
     */
    public void backToStart(View view) {
        onBackPressed();
    }
}
