package com.example.kevin.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 3/5/2018.
 */

public class Output extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);
        Intent intent = getIntent();
        ArrayList<String> storyParts = intent.getStringArrayListExtra("story");
        ArrayList<String> values = intent.getStringArrayListExtra("values");
        TextView output = findViewById(R.id.textOutput);
        String story = storyParts.get(0);
        boolean nextVal = true;
        int curPart = 1, curVal = 0;
        for(int i = 1; i < storyParts.size() + values.size(); i++){
            if(nextVal){
                story = story + " " + values.get(curVal++);
            }else{
                story = story + " " + storyParts.get(curPart++);
            }
            nextVal = !nextVal;
        }
        output.setText(story);
    }


    public void reset(View view){
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
    }

}
