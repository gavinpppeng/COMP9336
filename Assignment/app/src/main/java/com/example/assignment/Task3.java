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

public class Task3 extends AppCompatActivity {
    private TextView text_task3;
    private Button button3;
    private Button button32, button33;
    private AudioRecord mAudioRecord;
    private static final int samplerate = 44100;
    //The frequency array we want, we can hear 20Hz-20000Hz
    private double[] data;
    private Goertzel myGoertzel;
    private Thread audioThread;
    private boolean isRecording = false;
    private short[] data_from_device;
    public static float[] DTFMfrequencies = new float[]{697F, 770F, 852F, 1209F, 1336F, 1477F};
    public static double[] powers = new double[6];
    private double thresold = 100000D;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task3);
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


        button3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Task3.this, MainActivity.class);
                startActivity(intent);
                if(mAudioRecord != null){
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    //it must be set null
                    mAudioRecord = null;
                    isRecording = false;
                }
                Task3.this.finish();
            }
        });

        button32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(isRecording){
                            if(mAudioRecord != null){
                                data_from_device = new short[buffersize];
                                int read_data = mAudioRecord.read(data_from_device, 0, buffersize);
                                //	zero or the positive number of shorts that were read, or one of the following error codes. The number of shorts will be a multiple of the channel count not to exceed sizeInShorts.
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
                                for(int j=0; j<DTFMfrequencies.length; j++)
                                {
                                    //initiating Goertzel
                                    myGoertzel.initGoertzel(DTFMfrequencies[j]);

                                    //getting the Q0,Q1,Q2 to calculate the Power
                                    for(double sample: data){
                                        myGoertzel.processSample(sample);
                                    }

                                    //Now we can get the power of corresponding frequency
                                    powers[j] = Math.sqrt(myGoertzel.getMagnitudeSquared());
                                    Log.i("The powers are -------", String.valueOf(Math.sqrt(myGoertzel.getMagnitudeSquared())));

                                }

                                /*
                                int first_thresold_index = -1;
                                for (int k=0; k<powers.length; k++)
                                {
                                    if(powers[k] >= thresold){
                                        first_thresold_index = k;
                                    }
                                }

                                if(first_thresold_index!=-1)
                                System.out.println("-----------------------"+ DTFMfrequencies[first_thresold_index]);

                              //  if(first_thresold_index >= 0) {
                                /*
                                    double maxPowers = powers[first_thresold_index];
                                    int index = first_thresold_index;
                                    //getting the maximum value of powers
                                    for (int i = first_thresold_index; i < powers.length; i++) {
                                        if (maxPowers < powers[i]) {
                                            maxPowers = powers[i];
                                            index = i;
                                        }
                                    }

                                    maxPowers = powers[first_thresold_index];
                                    int sec_index = first_thresold_index;
                                    //getting the second maximum value of powers
                                    for (int i = 0; i < powers.length; i++) {
                                        if (maxPowers < powers[i] && i != index && DTFMfrequencies[i] != DTFMfrequencies[index]) {
                                            maxPowers = powers[i];
                                            sec_index = i;
                                        }
                                    }
                                    */

                                 if (Arrays.stream(powers).max().getAsDouble() >= thresold) {
                                    int index = 0;
                                    System.out.print(Arrays.stream(powers).max().getAsDouble());
                                    //finding the maximum value of powers corresponding to frequencies
                                    for (int i = 0; i < powers.length; i ++ ) {
                                        if (powers[i] == Arrays.stream(powers).max().getAsDouble()) {
                                            index = i;
                                            break;
                                        }
                                    }

                                    //finding the second maximum value of powers corresponding to frequencies
                                     int sec_index = 0;
                                     double max = 0;
                                     for (int i = 0; i < powers.length; i ++ ) {
                                         if (i != index && powers[i] > max) {
                                             max = powers[i];
                                             sec_index = i;
                                         }
                                     }

                                    Log.i("index and the second index", String.valueOf(index)+ "  "+ String.valueOf(sec_index));
                                    //since the result of combination of DTMF is only one, the sum of max and sec_max can get the number
                                    float sum_DTFM = DTFMfrequencies[index] + DTFMfrequencies[sec_index];
                                    if (sum_DTFM == 1906) {
                                        text_task3.setText("Number is 1");
                                    } else if (sum_DTFM == 2033) {
                                        text_task3.setText("Number is 2");
                                    } else if (sum_DTFM == 2174) {
                                        text_task3.setText("Number is 3");
                                    } else if (sum_DTFM == 1979) {
                                        text_task3.setText("Number is 4");
                                    } else if (sum_DTFM == 2106) {
                                        text_task3.setText("Number is 5");
                                    } else if (sum_DTFM == 2247) {
                                        text_task3.setText("Number is 6");
                                    } else if (sum_DTFM == 2061) {
                                        text_task3.setText("Number is 7");
                                    } else if (sum_DTFM == 2188) {
                                        text_task3.setText("Number is 8");
                                    } else if (sum_DTFM == 2329) {
                                        text_task3.setText("Number is 9");
                                    }

                                }
                            }
                        }
                    }
                });
                audioThread.start();
            }
        });

        button33.setOnClickListener(new View.OnClickListener() {
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
        text_task3 = (TextView)findViewById(R.id.tv3);
        button3 = (Button)findViewById(R.id.button3);
        button32 = (Button)findViewById(R.id.button32);
        button33 = (Button) findViewById(R.id.button33);
    }
}
