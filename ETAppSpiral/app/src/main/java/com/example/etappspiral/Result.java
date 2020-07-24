package com.example.etappspiral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    Button button1, button2, button3, button4;
    ArrayList<Double> StaticEntropy;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        StaticEntropy = (ArrayList<Double>) getIntent().getSerializableExtra("StaticEntropy");
        setContentView(R.layout.activity_result);
        button1 = (Button)findViewById(R.id.button5);
        button2 = (Button)findViewById(R.id.button4);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button6);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Result.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if (StaticEntropy.get(0)<0.15){
                   button3.setTextColor(-65536);
                   button3.setText("Severe");
               }
               else{
                   button3.setTextColor(-16711936);
                   button3.setText("Mild");
               }


            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Result.this, StatisticalAnalysis.class);
                i.putExtra("StaticEntropy", StaticEntropy );
                startActivity(i);
                finish();

            }
        });






    }






}
