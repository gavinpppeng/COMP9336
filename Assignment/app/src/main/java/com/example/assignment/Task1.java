package com.example.assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Handler;
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

public class Task1 extends AppCompatActivity {

    private TextView text_task1;
    private Button button1;
    private Button button12;
    private static final int samplerate = 44100;
    //The frequency array we want, we can hear 20Hz-20000Hz
    private static double[] FREQUENCIES = new double[10000];
    private AudioDispatcher dispatcher;
    private AudioProcessor task1AudioProcessor;
    private Handler handler;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task1);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {  // request permission RECORD_AUDIO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1234);
        }
        init();

        button1.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Task1.this, MainActivity.class);
                startActivity(intent);
                dispatcher.removeAudioProcessor(task1AudioProcessor);
                dispatcher.stop();
                Task1.this.finish();
            }
        });

        //AudioRecord: The AudioRecord class manages the audio resources for Java applications to record audio from the audio input hardware of the platform.
        // This is achieved by "pulling" (reading) the data from the AudioRecord object.
        //getMinBufferSize: Returns the minimum buffer size required for the successful creation of an AudioRecord object, in byte units.
        //Parameters:
        // sampleRateInHz	int: the sample rate expressed in Hertz.
        // channelConfig	int: describes the configuration of the audio channels. (1 is #CHANNEL_IN_DEFAULT)
        // audioFormat	int: the format in which the audio data is represented.(2 is AudioFormat#ENCODING_PCM_16BIT.)
        int buffersize = AudioRecord.getMinBufferSize(samplerate, 1, 2);

        //AudioDispatcherFactory: The Factory creates AudioDispatcher objects from various sources: the configured default microphone,
        // PCM wav files or PCM samples piped from a sub-process. It depends on the javax.sound.* packages and does not work on Android.

        //AudioDispatcher: This class plays a file and sends float arrays to registered AudioProcessor implementors. This class can be used to feed FFT's, pitch detectors,
        // audio players, ... Using a (blocking) audio player it is even possible to synchronize execution of AudioProcessors and sound. This behavior can be used for visualization.

        //public static AudioDispatcher fromDefaultMicrophone(int sampleRate,int audioBufferSize,int bufferOverlap)
        // Create a new AudioDispatcher connected to the default microphone. The default is defined by the Java runtime by calling
        // AudioSystem.getTargetDataLine(format)
        //.The microphone must support the format of the requested sample rate, 16bits mono, signed big endian.

        //sampleRate - The requested sample rate must be supported by the capture device. Nonstandard sample rates can be problematic!
        //audioBufferSize - The size of the buffer defines how much samples are processed in one step. Common values are 1024,2048.
        //bufferOverlap - How much consecutive buffers overlap (in samples). Half of the AudioBufferSize is common.
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(samplerate, buffersize, 0);


        //AudioProcessor: AudioProcessors are responsible for actual digital signal processing. The interface is simple: a process method that works on an AudioEvent object.
        // The AudioEvent contains a buffer with some floats and the same information in raw bytes.
        //AudioProcessors are meant to be chained e.g. execute an effect and then play the sound. The chain of audio processor can be interrupted by returning false in the process methods.

        //Goertzel: Contains an implementation of the Goertzel algorithm. It can be used to detect if one or more predefined frequencies are present in a signal. E.g. to do DTMF decoding.
        //FrequenciesDetectedHandler: An interface used to react on detected frequencies.
        // Parameters:
        //frequencies - A list of detected frequencies.
        //powers - A list of powers of the detected frequencies.
        //allFrequencies - A list of all frequencies that were checked.
        //allPowers - A list of powers of all frequencies that were checked.
        task1AudioProcessor = new Goertzel(samplerate, buffersize, FREQUENCIES, new Goertzel.FrequenciesDetectedHandler() {
            @Override
            public void handleDetectedFrequencies(double[] frequencies, double[] powers, double[] allFrequencies, double[] allPowers) {
                //Message frequency = new Message();
                Log.i("length of this ", String.valueOf(frequencies.length));
                if(frequencies.length ==1 ){      //it just finds single tone
                    text_task1.setText(String.valueOf(frequencies[0])+ "Hz");

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
                        text_task1.setText(String.valueOf(frequencies[index]) + "Hz");
                        Log.i("Many frequencies Debug/Info:", String.valueOf(frequencies[index]));
                    }

                }

            }
        });

        dispatcher.addAudioProcessor(task1AudioProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatcher.removeAudioProcessor(task1AudioProcessor);
                dispatcher.addAudioProcessor(task1AudioProcessor);
                text_task1.setText("No frequency has been detected yet.");
            }
        });

    }

    // @SuppressLint("HandlerLeak")
    private void init(){
        text_task1 = (TextView)findViewById(R.id.tv1);
        button1 = (Button)findViewById(R.id.button1);
        button12 = (Button)findViewById(R.id.button12);

        for(int i = 0; i<FREQUENCIES.length; i++){
            FREQUENCIES[i] = (double) (2*i);
        }

    }
}
