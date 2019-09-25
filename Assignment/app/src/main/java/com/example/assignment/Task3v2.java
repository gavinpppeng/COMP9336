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
import be.tarsos.dsp.pitch.DTMF;
import be.tarsos.dsp.pitch.Goertzel;

public class Task3v2 extends AppCompatActivity {
    private TextView text_task3;
    private Button button3;
    private Button button32;
    private static final int samplerate = 44100;
    //The frequency array we want, we can hear 20Hz-20000Hz
    private AudioDispatcher dispatcher;
    private AudioProcessor task3AudioProcessor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task3);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {  // request permission RECORD_AUDIO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1234);
        }
        init();
        //Log.i("DTMF range:", String.valueOf(DTMF.DTMF_FREQUENCIES[7]));

        button3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Task3v2.this, MainActivity.class);
                startActivity(intent);
                dispatcher.removeAudioProcessor(task3AudioProcessor);
                dispatcher.stop();
                Task3v2.this.finish();
            }
        });


        int buffersize = AudioRecord.getMinBufferSize(samplerate, 1, 2);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(samplerate, buffersize, 0);
        task3AudioProcessor = new Goertzel(samplerate, buffersize, DTMF.DTMF_FREQUENCIES, new Goertzel.FrequenciesDetectedHandler() {
            @Override
            public void handleDetectedFrequencies(double[] frequencies, double[] powers, double[] allFrequencies, double[] allPowers) {
                //Message frequency = new Message();
                Log.i("length of frequencies is ", String.valueOf(frequencies.length));
                if(frequencies.length ==2 ) {      //Finding the dual tone
                    Log.i("Frequencies have: !!!!!!!!", String.valueOf(frequencies[0]) + " " + String.valueOf(frequencies[1]));
                    //rowIndex represents 697Hz, 770Hz, 852Hz, 941Hz
                    //colIndex represents 1209Hz, 1336Hz, 1477Hz, 1633Hz
                    int rowIndex = -1;
                    int colIndex = -1;
                    for (int i = 0; i < 3; i++) {     //DTMF.DTMF_FREQUENCIES[0-3] includes 697Hz(0), 770Hz(1), 852Hz(2), 941Hz(3)(don't need in this task)
                        if (frequencies[0] == DTMF.DTMF_FREQUENCIES[i] || frequencies[1] == DTMF.DTMF_FREQUENCIES[i])
                            rowIndex = i;
                    }
                    for (int i = 4; i < DTMF.DTMF_FREQUENCIES.length - 1; i++) { //DTMF.DTMF_FREQUENCIES[4-7] includes 1209Hz(4), 1336Hz(5), 1477Hz(6), 1633Hz(7)(don't need in this task)
                        if (frequencies[0] == DTMF.DTMF_FREQUENCIES[i] || frequencies[1] == DTMF.DTMF_FREQUENCIES[i])
                            colIndex = i - 4;
                    }
                    if (rowIndex >= 0 && colIndex >= 0) {
                        Log.i("DTMF number is ", String.valueOf(DTMF.DTMF_CHARACTERS[rowIndex][colIndex]));
                        text_task3.setText("Number is " + String.valueOf(DTMF.DTMF_CHARACTERS[rowIndex][colIndex]));
                    }
                }

            }
        });

        //dispatcher.addAudioProcessor(task3AudioProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
        button32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatcher.addAudioProcessor(task3AudioProcessor);
            }
        });
    }

    private void init(){
        text_task3 = (TextView)findViewById(R.id.tv3);
        button3 = (Button)findViewById(R.id.button3);
        button32 = (Button)findViewById(R.id.button32);
    }
}
