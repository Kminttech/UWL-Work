package com.example.kmint.biggernumbergame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmint.biggernumbergame.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random rndNumGen;
    Button leftButton;
    Button rightButton;
    TextView scoreDis;
    int leftValue;
    int rightValue;
    int scoreVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rndNumGen = new Random();
        leftButton = findViewById(R.id.numB1);
        rightButton = findViewById(R.id.numB2);
        scoreDis = findViewById(R.id.scoreView);
        leftValue = rndNumGen.nextInt(10);
        rightValue = rndNumGen.nextInt(10);
        while (leftValue == rightValue){
            rightValue = rndNumGen.nextInt(10);
        }
        leftButton.setText(""+leftValue);
        rightButton.setText(""+rightValue);
        scoreVal = 0;
    }

    public void leftClick(View view) {
        if(leftValue > rightValue){
            scoreVal++;
            Toast.makeText(this, "Correct", Toast.LENGTH_LONG).show();
        }else{
            scoreVal--;
            Toast.makeText(this, "Incorrect", Toast.LENGTH_LONG).show();
        }
        nextValues();
    }

    public void rightClick(View view) {
        if(rightValue > leftValue){
            scoreVal++;
            Toast.makeText(this, "Correct", Toast.LENGTH_LONG).show();
        }else{
            scoreVal--;
            Toast.makeText(this, "Incorrect", Toast.LENGTH_LONG).show();
        }
        nextValues();
    }

    public void nextValues(){
        leftValue = rndNumGen.nextInt(10);
        rightValue = rndNumGen.nextInt(10);
        while (leftValue == rightValue){
            rightValue = rndNumGen.nextInt(10);
        }
        leftButton.setText(""+leftValue);
        rightButton.setText(""+rightValue);
        scoreDis.setText("Score: " + scoreVal);
    }
}
