package com.example.assignment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class Task2 extends AppCompatActivity {
    private TextView text_task2;
    private Button button2;
    private Button button22, button23;
    private AudioRecord mAudioRecord;
    private static final int samplerate = 44100;
    private double[] data;
    private Goertzel myGoertzel;
    private Thread audioThread;
    private boolean isRecording = false;
    private short[] data_from_device;
    public static float[] FREQUENCIES = new float[18];
    public static double[] powers = new double[18];
    private double thresold = 250000D;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {  // request permission RECORD_AUDIO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1234);
        }
        init();
        //Log.i("DTMF range:", String.valueOf(DTMF.DTMF_FREQUENCIES[7]));

        final int buffersize = AudioRecord.getMinBufferSize(samplerate, 1, 2);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, samplerate, 1, AudioFormat.ENCODING_PCM_16BIT, buffersize);
        data = new double[buffersize];
        mAudioRecord.startRecording();
        isRecording = true;
        myGoertzel = new Goertzel(samplerate, buffersize);


        button2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Task2.this, MainActivity.class);
                startActivity(intent);
                if(mAudioRecord != null){
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    //it must be set null
                    mAudioRecord = null;
                    isRecording = false;
                }
                Task2.this.finish();
            }
        });

        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(isRecording){
                            if(mAudioRecord != null){
                                data_from_device = new short[buffersize];
                                int read_data = mAudioRecord.read(data_from_device, 0, buffersize);
                                //	zero or the positive number of shorts that were read, or one of the following error codes.
                                //ERROR_INVALID_OPERATION if the object isn't properly initialized
                                //ERROR_BAD_VALUE if the parameters don't resolve to valid data and indexes
                                //ERROR_DEAD_OBJECT if the object is not valid anymore and needs to be recreated. The dead object error code is not returned if some data was successfully transferred. In this case, the error is returned at the next read()
                                //ERROR in case of other error
                                //The value of return won't be larger than buffersize

                                //There is no error
                                if(AudioRecord.ERROR_INVALID_OPERATION != read_data)
                                {
                                    for(int i=0; i<read_data;i++){
                                        data[i] = (double) data_from_device[i];
                                    }
                                }

                                //finding the right frequencies
                                for(int j=0; j<FREQUENCIES.length; j++)
                                {
                                    //initiating Goertzel
                                    myGoertzel.initGoertzel(FREQUENCIES[j]);

                                    //getting the Q0,Q1,Q2 to calculate the Power
                                    for(double sample: data){
                                        myGoertzel.processSample(sample);
                                    }

                                    //Now we can get the power of corresponding frequency
                                    powers[j] = Math.sqrt(myGoertzel.getMagnitudeSquared());
                                    Log.i("The frequencies are -------", String.valueOf(Math.sqrt(myGoertzel.getMagnitudeSquared())));

                                }

                                if (Arrays.stream(powers).max().getAsDouble() >= thresold) {
                                    int index = 0;
                                    for (int i = 0; i < powers.length; i ++ ) {
                                        if (powers[i] == Arrays.stream(powers).max().getAsDouble()) {
                                            index = i;
                                            break;
                                        }
                                    }

                                    Log.i("The frequencies are -------", String.valueOf(Math.sqrt(FREQUENCIES[index])));
                                    if(FREQUENCIES[index]==450){
                                        text_task2.setText("Audible version number: 1");
                                    }
                                    else if(FREQUENCIES[index]==550) {
                                        text_task2.setText("Audible version number: 2");
                                    }
                                    else if(FREQUENCIES[index]==650) {
                                        text_task2.setText("Audible version number: 3");
                                    }
                                    else if(FREQUENCIES[index]==750) {
                                        text_task2.setText("Audible version number: 4");
                                    }
                                    else if(FREQUENCIES[index]==850) {
                                        text_task2.setText("Audible version number: 5");
                                    }
                                    else if(FREQUENCIES[index]==950) {
                                        text_task2.setText("Audible version number: 6");
                                    }
                                    else if(FREQUENCIES[index]==1050) {
                                        text_task2.setText("Audible version number: 7");
                                    }
                                    else if(FREQUENCIES[index]==1150) {
                                        text_task2.setText("Audible version number: 8");
                                    }
                                    else if(FREQUENCIES[index]==1250) {
                                        text_task2.setText("Audible version number: 9");
                                    }
                                    else if(FREQUENCIES[index] == 13150){
                                        text_task2.setText("Inaudible version number: 1");
                                    }
                                    else if(FREQUENCIES[index] == 13250) {
                                        text_task2.setText("Inaudible version number: 2");
                                    }
                                    else if(FREQUENCIES[index]==13350) {
                                        text_task2.setText("Inaudible version number: 3");
                                    }
                                    else if(FREQUENCIES[index]==13450) {
                                        text_task2.setText("Inaudible version number: 4");
                                    }
                                    else if(FREQUENCIES[index]==13550) {
                                        text_task2.setText("Inaudible version number: 5");
                                    }
                                    else if(FREQUENCIES[index]==13650) {
                                        text_task2.setText("Inaudible version number: 6");
                                    }
                                    else if(FREQUENCIES[index]==13750) {
                                        text_task2.setText("Inaudible version number: 7");
                                    }
                                    else if(FREQUENCIES[index]==13850) {
                                        text_task2.setText("Inaudible version number: 8");
                                    }
                                    else if(FREQUENCIES[index]==13950) {
                                        text_task2.setText("Inaudible version number: 9");
                                    }
                                }
                            }
                        }
                    }
                });
                audioThread.start();
            }
        });

        button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAudioRecord != null){
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    //it must be set null
                    mAudioRecord = null;
                }
            }
        });
    }

    private void init(){
        text_task2 = (TextView)findViewById(R.id.tv2);
        button2 = (Button)findViewById(R.id.button2);
        button22 = (Button)findViewById(R.id.button22);
        button23 = (Button) findViewById(R.id.button23);
        for(int i = 0; i<FREQUENCIES.length; i++){
            if(i<9)
            {
                FREQUENCIES[i] = 450 + i * 100;
            }
            if(i>=9){
                FREQUENCIES[i] = 13150 + (i-9)*100;
            }
        }
    }


}
