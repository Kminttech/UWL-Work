package com.example.kevin.madlibs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Scanner;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void startStory(View view){
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.madlib1));
        ArrayList<String> story = new ArrayList<>();
        story.add("");
        ArrayList<String> types = new ArrayList<>();
        int i = 0;
        while (scan.hasNext()){
            String temp = scan.next();
            int start = temp.indexOf('<');
            if(start!=-1){
                story.set(i, story.get(i) + " " + temp.substring(0,start));
                int end = temp.indexOf('>');
                while (end==-1){
                    temp = temp + " " + scan.next();
                    end = temp.indexOf('>');
                }
                types.add(temp.substring(start+1,end));
                if(temp.length() > end){
                    story.add(temp.substring(end+1));
                }else{
                    story.add("");
                }
                i++;
            }else{
                story.set(i, story.get(i) + " " + temp);
            }
        }
        Intent intent = new Intent(this, Input.class);
        intent.putStringArrayListExtra("story", story);
        intent.putStringArrayListExtra("types",types);
        startActivity(intent);
    }
}
