package com.example.assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.Goertzel;

public class Task2v2 extends AppCompatActivity {
    private TextView text_task2;
    private Button button2;
    private Button button22;
    private static final int samplerate = 44100;
    //The frequency array we want, we can hear 2Hz-20000Hz
    //The devices can detect about 300-20000Hz
    private static double[] FREQUENCIES = new double[18];
    private AudioDispatcher dispatcher;
    private AudioProcessor task2AudioProcessor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {  // request permission RECORD_AUDIO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1234);
        }
        init();

        button2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Task2v2.this, MainActivity.class);
                startActivity(intent);
                dispatcher.removeAudioProcessor(task2AudioProcessor);
                dispatcher.stop();
                Task2v2.this.finish();
            }
        });

        int buffersize = AudioRecord.getMinBufferSize(samplerate, 1, 2);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(samplerate, buffersize, 0);
        task2AudioProcessor = new Goertzel(samplerate, buffersize, FREQUENCIES, new Goertzel.FrequenciesDetectedHandler() {
            @Override
            public void handleDetectedFrequencies(double[] frequencies, double[] powers, double[] allFrequencies, double[] allPowers) {
                if(frequencies.length ==1 ){      //it just finds single tone
                    //text_task2.setText(String.valueOf(frequencies[0])+ "Hz");
                    Log.i("Frequency:", String.valueOf(frequencies[0]));
                    //Audible: 1:400-500, 2:500-600, 3:600-700, 4:700-800, 5:800-900, 6:900-1000, 7:1000-1100,
                    // 8:1100-1200, 9:1200-1300
                    if(frequencies[0]==450){
                        text_task2.setText("Audible version number: 1");
                    }
                    else if(frequencies[0]==550) {
                        text_task2.setText("Audible version number: 2");
                    }
                    else if(frequencies[0]==650) {
                        text_task2.setText("Audible version number: 3");
                    }
                    else if(frequencies[0]==750) {
                        text_task2.setText("Audible version number: 4");
                    }
                    else if(frequencies[0]==850) {
                        text_task2.setText("Audible version number: 5");
                    }
                    else if(frequencies[0]==950) {
                        text_task2.setText("Audible version number: 6");
                    }
                    else if(frequencies[0]==1050) {
                        text_task2.setText("Audible version number: 7");
                    }
                    else if(frequencies[0]==1150) {
                        text_task2.setText("Audible version number: 8");
                    }
                    else if(frequencies[0]==1250) {
                        text_task2.setText("Audible version number: 9");
                    }
                    //Inaudible: 1:16100-16200, 2:16200-16300, 3:16300-16400, 4:16400-16500, 5:16500-16600, 6:16600-16700, 7:16700-16800,
                    //8:16800-16900, 9:16900-17000
                    else if(frequencies[0] == 13150){
                        text_task2.setText("Inaudible version number: 1");
                    }
                    else if(frequencies[0] == 13250) {
                        text_task2.setText("Inaudible version number: 2");
                    }
                    else if(frequencies[0]==13350) {
                        text_task2.setText("Inaudible version number: 3");
                    }
                    else if(frequencies[0]==13450) {
                        text_task2.setText("Inaudible version number: 4");
                    }
                    else if(frequencies[0]==13550) {
                        text_task2.setText("Inaudible version number: 5");
                    }
                    else if(frequencies[0]==13650) {
                        text_task2.setText("Inaudible version number: 6");
                    }
                    else if(frequencies[0]==13750) {
                        text_task2.setText("Inaudible version number: 7");
                    }
                    else if(frequencies[0]==13850) {
                        text_task2.setText("Inaudible version number: 8");
                    }
                    else if(frequencies[0]==13950) {
                        text_task2.setText("Inaudible version number: 9");
                    }

                }
                else {        //Finding many frequencies and extracting the largest powers frequency
                    double maxPowers = powers[0];
                    int index = 0;
                    for(int i=1; i<frequencies.length; i++){
                        if(maxPowers < powers[i]){
                            maxPowers = powers[i];
                            index = i;
                        }
                    }


                    if(powers[index] >= 35) {
                        //Only powers > 35dB can show
                        //text_task2.setText(String.valueOf(frequencies[index]) + "Hz");
                        Log.i("Many frequencies Debug/Info:", String.valueOf(frequencies[index]));
                        Log.i("Power is :", String.valueOf(powers[index]));
                        Log.i("Frequency:", String.valueOf(frequencies[index]));
                        if(frequencies[index]==450){
                            text_task2.setText("Audible version number: 1");
                        }
                        else if(frequencies[index]==550) {
                            text_task2.setText("Audible version number: 2");
                        }
                        else if(frequencies[index]==650) {
                            text_task2.setText("Audible version number: 3");
                        }
                        else if(frequencies[index]==750) {
                            text_task2.setText("Audible version number: 4");
                        }
                        else if(frequencies[index]==850) {
                            text_task2.setText("Audible version number: 5");
                        }
                        else if(frequencies[index]==950) {
                            text_task2.setText("Audible version number: 6");
                        }
                        else if(frequencies[index]==1050) {
                            text_task2.setText("Audible version number: 7");
                        }
                        else if(frequencies[index]==1150) {
                            text_task2.setText("Audible version number: 8");
                        }
                        else if(frequencies[index]==1250) {
                            text_task2.setText("Audible version number: 9");
                        }
                        /*
                        //Audible: 1:400-500, 2:500-600, 3:600-700, 4:700-800, 5:800-900, 6:900-1000, 7:1000-1100,
                        // 8:1100-1200, 9:1200-1300
                        if(frequencies[index]>400 && frequencies[index]<=500){
                            text_task2.setText("Audible version number: 1");
                        }
                        else if(frequencies[index]>500 && frequencies[index]<=600) {
                            text_task2.setText("Audible version number: 2");
                        }
                        else if(frequencies[index]>600 && frequencies[index]<=700) {
                            text_task2.setText("Audible version number: 3");
                        }
                        else if(frequencies[index]>700 && frequencies[index]<=800) {
                            text_task2.setText("Audible version number: 4");
                        }
                        else if(frequencies[index]>800 && frequencies[index]<=900) {
                            text_task2.setText("Audible version number: 5");
                        }
                        else if(frequencies[index]>900 && frequencies[index]<=1000) {
                            text_task2.setText("Audible version number: 6");
                        }
                        else if(frequencies[index]>1000 && frequencies[index]<=1100) {
                            text_task2.setText("Audible version number: 7");
                        }
                        else if(frequencies[index]>1100 && frequencies[index]<=1200) {
                            text_task2.setText("Audible version number: 8");
                        }
                        else if(frequencies[index]>1200 && frequencies[index]<=1300) {
                            text_task2.setText("Audible version number: 9");
                        }

                        //Inaduible: 1:16100-16200, 2:16200-16300, 3:16300-16400, 4:16400-16500, 5:16500-16600, 6:16600-16700, 7:16700-16800,
                        //8:16800-16900, 9:16900-17000
                        else if(frequencies[index]>16100 && frequencies[index]<=16200){
                            text_task2.setText("Inaudible version number: 1");
                        }
                        else if(frequencies[index]>16200 && frequencies[index]<=16300) {
                            text_task2.setText("Inaudible version number: 2");
                        }
                        else if(frequencies[index]>16300 && frequencies[index]<=16400) {
                            text_task2.setText("Inaudible version number: 3");
                        }
                        else if(frequencies[index]>16400 && frequencies[index]<=16500) {
                            text_task2.setText("Inaudible version number: 4");
                        }
                        else if(frequencies[index]>16500 && frequencies[index]<=16600) {
                            text_task2.setText("Inudible version number: 5");
                        }
                        else if(frequencies[index]>16600 && frequencies[index]<=16700) {
                            text_task2.setText("Inaudible version number: 6");
                        }
                        else if(frequencies[index]>16700 && frequencies[index]<=16800) {
                            text_task2.setText("Inaudible version number: 7");
                        }
                        else if(frequencies[index]>16800 && frequencies[index]<=16900) {
                            text_task2.setText("Inaudible version number: 8");
                        }
                        else if(frequencies[index]>16900 && frequencies[index]<=17000) {
                            text_task2.setText("Inudible version number: 9");
                        }
                        */
                        else if(frequencies[index] == 13150){
                            text_task2.setText("Inaudible version number: 1");
                        }
                        else if(frequencies[index] == 13250) {
                            text_task2.setText("Inaudible version number: 2");
                        }
                        else if(frequencies[index]==13350) {
                            text_task2.setText("Inaudible version number: 3");
                        }
                        else if(frequencies[index]==13450) {
                            text_task2.setText("Inaudible version number: 4");
                        }
                        else if(frequencies[index]==13550) {
                            text_task2.setText("Inaudible version number: 5");
                        }
                        else if(frequencies[index]==13650) {
                            text_task2.setText("Inaudible version number: 6");
                        }
                        else if(frequencies[index]==13750) {
                            text_task2.setText("Inaudible version number: 7");
                        }
                        else if(frequencies[index]==13850) {
                            text_task2.setText("Inaudible version number: 8");
                        }
                        else if(frequencies[index]==13950) {
                            text_task2.setText("Inaudible version number: 9");
                        }
                    }

                }

            }
        });

        //dispatcher.addAudioProcessor(task2AudioProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatcher.addAudioProcessor(task2AudioProcessor);
            }
        });
    }

    private void init(){
        text_task2 = (TextView)findViewById(R.id.tv2);
        button2 = (Button)findViewById(R.id.button2);
        button22 = (Button)findViewById(R.id.button22);
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
