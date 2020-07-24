package com.example.etappspiral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StatisticalAnalysis extends AppCompatActivity {

    TextView textView3;
    Button button7;
    ArrayList<Double> StaticEntropy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_analysis);
        StaticEntropy = (ArrayList<Double>) getIntent().getSerializableExtra("StaticEntropy");
        textView3 = (TextView) findViewById(R.id.textView3);
        button7 = (Button)findViewById(R.id.button7);
        String data3 = "Approximate Entropy: "+ (StaticEntropy.get(0))  + ", "  + "Sample Entropy: " + (StaticEntropy.get(1));
        textView3.setText(data3);

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatisticalAnalysis.this, Result.class);
                i.putExtra("StaticEntropy", StaticEntropy );
                startActivity(i);
                finish();

            }
        });

    }

}
