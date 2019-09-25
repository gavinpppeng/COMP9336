package com.example.assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity{
    private Button button_task1;
    private Button button_task2;
    private Button button_task3;
    private Button button_task4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        button_task1.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //Create a new Intent
                Intent intent = new Intent();
                //Set the transforming class of intent
                intent.setClass(MainActivity.this, Task1.class);
                //Start a new Activity
                startActivity(intent);
                //Close this activity
                MainActivity.this.finish();

            }
        });

        button_task2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //Create a new Intent
                Intent intent = new Intent();
                //Set the transforming class of intent
                intent.setClass(MainActivity.this, Task2.class);
                //Start a new Activity
                startActivity(intent);
                //Close this activity
                MainActivity.this.finish();
            }
        });

        button_task3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //Create a new Intent
                Intent intent = new Intent();
                //Set the transforming class of intent
                intent.setClass(MainActivity.this, Task3.class);
                //Start a new Activity
                startActivity(intent);
                //Close this activity
                MainActivity.this.finish();

            }
        });

        button_task4.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //Create a new Intent
                Intent intent = new Intent();
                //Set the transforming class of intent
                intent.setClass(MainActivity.this, Task4.class);
                //Start a new Activity
                startActivity(intent);
                //Close this activity
                MainActivity.this.finish();

            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {  // request permission RECORD_AUDIO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1234);
        }
    }

    private void init(){
        button_task1 = (Button)findViewById(R.id.button_task1);
        button_task2 = (Button)findViewById(R.id.button_task2);
        button_task3 = (Button)findViewById(R.id.button_task3);
        button_task4 = (Button)findViewById(R.id.button_task4);
    }

}

