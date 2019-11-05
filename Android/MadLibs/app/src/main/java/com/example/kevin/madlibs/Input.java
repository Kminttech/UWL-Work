package com.example.kevin.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 3/5/2018.
 */

public class Input extends AppCompatActivity {

    ArrayList<String> story;
    ArrayList<String> types;
    ArrayList<String> values;

    int curIn = 0;
    TextView count;
    TextView descriptor;
    Button submit;
    EditText userIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Intent intent = getIntent();
        story = intent.getStringArrayListExtra("story");
        types = intent.getStringArrayListExtra("types");
        values = new ArrayList<>();
        count = findViewById(R.id.inputCount);
        descriptor = findViewById(R.id.inputDesciptor);
        submit = findViewById(R.id.inputConfirm);
        userIn = findViewById(R.id.textInput);
        count.setText(types.size() + " word(s) left");
        descriptor.setText("please type a/an " + types.get(curIn));
        userIn.setHint(types.get(curIn));
    }

    public void acceptInput(View view){
        values.add(userIn.getText().toString());
        curIn++;
        if(curIn>=types.size()){
            moveToOutput();
        }else {
            count.setText(types.size() - curIn + " word(s) left");
            descriptor.setText("please type a/an " + types.get(curIn));
            userIn.setHint(types.get(curIn));
            userIn.setText("");
        }
    }

    public void moveToOutput(){
        Intent intent = new Intent(this, Output.class);
        intent.putStringArrayListExtra("story", story);
        intent.putStringArrayListExtra("values", values);
        startActivity(intent);
    }

}
