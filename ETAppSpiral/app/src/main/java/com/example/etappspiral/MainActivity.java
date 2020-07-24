package com.example.etappspiral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    ImageView imageView;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm@#";
    Button button1, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        button1 = (Button) findViewById(R.id.button1);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        button2 = (Button) findViewById(R.id.button2);




        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button2.setVisibility(View.VISIBLE);

                textView.setText("Test ID: "+ generateString(8));

                button1.setEnabled(false);


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, StaticSpiral.class);
                myIntent.putExtra("text", textView.getText().toString());
                MainActivity.this.startActivity(myIntent);

            }
        });

    }

    private String generateString(final int sizeOfRandomString){

        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();



    }

}
