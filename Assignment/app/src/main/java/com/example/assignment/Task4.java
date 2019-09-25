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

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.Goertzel;

public class Task4 extends AppCompatActivity {
    private TextView text_task4;
    private Button button4, button42, button43;
    private static final int samplerate = 44100;
    //The frequency array we want, we can hear 20Hz-20000Hz
    private AudioDispatcher dispatcher;
    private AudioProcessor task4AudioProcessor;
    private static double[] FREQUENCIES = new double[67];
    private ArrayList<Long> Timestamp = new ArrayList<>();
    private ArrayList<Integer> MQ = new ArrayList<>();
    private Thread MQThread;
    private static int lastbits = -1;
    private static long lasttime;
    //The first start flag
    private static boolean firstStartFlag = true;
    private static boolean secondStartFlag = true;
    private static boolean endFlag = true;
    private static int finishChar = 0;
    private static ArrayList<Integer> output_result = new ArrayList<Integer>();
    private static String msg = "";
    private static long upper_duration = 265;
    private static long lower_duration = 185;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task4);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {  // request permission RECORD_AUDIO
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1234);
        }

        init();
        QueueThread();
        button4.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Task4.this, MainActivity.class);
                startActivity(intent);
                dispatcher.removeAudioProcessor(task4AudioProcessor);
                dispatcher.stop();
                Task4.this.finish();
            }
        });


        int buffersize = AudioRecord.getMinBufferSize(samplerate, 1, 2);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(samplerate, buffersize, 0);
        task4AudioProcessor = new Goertzel(samplerate, buffersize, FREQUENCIES, new Goertzel.FrequenciesDetectedHandler() {
            @Override
            public void handleDetectedFrequencies(double[] frequencies, double[] powers, double[] allFrequencies, double[] allPowers) {
                Timestamp.add(System.currentTimeMillis());
                MQ.add((int)((frequencies[0])-400)/100);

                Log.i("Frequencies:",String.valueOf(frequencies[0]));
                Log.i("Frequencies:",String.valueOf(frequencies.length));
            }
        });

        //dispatcher.addAudioProcessor(task3AudioProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();

        button42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {               //put the button to start recording
                dispatcher.addAudioProcessor(task4AudioProcessor);
                MQ.clear();
                Timestamp.clear();
                msg = "";
                text_task4.setText("Prepare to receive the text...");
            }
        });

        button43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                //put the button to stop recording
                dispatcher.removeAudioProcessor(task4AudioProcessor);
                text_task4.setText("Already stop recording!");
            }
        });
    }

    private void QueueThread(){
        MQThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(Timestamp.size()!=0 && MQ.size() != 0){
                        // now getting the first time and the first message
                        long currentTimestamp = Timestamp.get(0);
                        //The first frequency including the preamble or data bits
                        int currentMsg = MQ.get(0);
                        Timestamp.remove(0);
                        MQ.remove(0);

                        if(currentMsg == 64)   //The preamble: the first staring flag
                        {
                            if(firstStartFlag){
                                //ending flag to true
                                endFlag = true;
                                //first start flag to false
                                firstStartFlag = false;
                                //using the flag to know whether getting the 12bits
                                finishChar = 0;
                                //the number of the last bits(now we don't get them)
                                lastbits = -1;
                                output_result.clear();
                                msg = "";
                                lasttime = currentTimestamp;
                                Log.i("Current time is :", String.valueOf(currentTimestamp));
                            }
                        }
                        else if(currentMsg == 66)  //preparing starting
                        {
                            if(!firstStartFlag) {
                                Log.i("wefind!!!!","Second start!!!");
                                secondStartFlag = false;
                                //ending flag to true
                                endFlag = true;
                                //using the flag to know whether getting the 12bits
                                finishChar = 0;
                                //the number of the last bits(now we don't get them)
                                lastbits = -1;
                                msg = "";
                                lasttime = currentTimestamp;
                            }
                        }
                        else if(currentMsg == 65)   //finishing flag
                        {
                            if(endFlag){
                                endFlag = false;
                                //it is more than 12 bits
                                if(output_result.size()!= 0){
                                    msg += "?";
                                    text_task4.setText(msg);
                                    Log.i("Last finish message", msg);
                                }

                                //all put the original state
                                output_result.clear();
                                lastbits = -1;
                                firstStartFlag = true;
                                secondStartFlag = true;
                                msg = "";
                            }

                        }

                        else {
                            if(!firstStartFlag && !secondStartFlag && endFlag) {
                                // Data bits
                                //set a duration to get the bits from transmitter
                                if (currentMsg != lastbits || (lower_duration <= currentTimestamp - lasttime && currentTimestamp - lasttime <= upper_duration)) {
                                    String Binary = Integer.toBinaryString(Integer.valueOf(currentMsg));
                                    Log.i("Debug:!!!!", String.valueOf(currentMsg));
                                    //satisfy the transmitting format from the sender (6 bits binary as high bits and 6 bits binary as low bits)
                                    Binary = String.format("%06d", Integer.valueOf(Binary));

                                    //getting the bits one by one from String type
                                    String[] BinaryFromDec = Binary.split("");

                                    //since the ascii, the first bit always equal to 0, we can ignore it
                                    for (int m = 1; m < BinaryFromDec.length; m++) {           // add into symbol
                                        output_result.add(Integer.parseInt(BinaryFromDec[m]));
                                    }

                                    //set the last bits
                                    lastbits = currentMsg;
                                    Log.i("The message we receive is :", String.valueOf(lastbits));

                                    //set the current time
                                    lasttime = currentTimestamp;

                                    //we just need to get 12bits, and this is 6 bits, if we get the second one, the finshChar will become 2
                                    finishChar = finishChar + 1;
                                    if (finishChar == 2) {                 // already have 12 bits
                                        String bit = error_correct(output_result);
                                        //if we get the result
                                        if (bit != null) {
                                            msg += bit;
                                        } else {
                                            msg += "?";
                                        }

                                        Log.i("receive message: ", msg);
                                        text_task4.setText(msg);

                                        output_result.clear();
                                        finishChar = 0;
                                    }
                                }
                            }

                        }

                    }
                }
            }
        });
        MQThread.start();
    }

    //Task 5
    private String error_correct(ArrayList<Integer> decoding_bits){
        if (decoding_bits.size() != 12)
            return null;

        int check_1 = even_one(decoding_bits.get(2) + decoding_bits.get(4) + decoding_bits.get(6) + decoding_bits.get(8) + decoding_bits.get(10));
        int check_2 = even_one(decoding_bits.get(2) + decoding_bits.get(5) + decoding_bits.get(6) + decoding_bits.get(9) + decoding_bits.get(10));
        int check_3 = even_one(decoding_bits.get(4) + decoding_bits.get(5) + decoding_bits.get(6) + decoding_bits.get(11));
        int check_4 = even_one(decoding_bits.get(8) + decoding_bits.get(9) + decoding_bits.get(10) + decoding_bits.get(11));

        //setting the error list which can find the error
        ArrayList<Integer> errorfinding = new ArrayList<Integer>();

        if (check_4 != decoding_bits.get(7)) {
            errorfinding.add(1);
        }
        else if(check_4 == decoding_bits.get(7))
        {
            errorfinding.add(0);
        }

        if (check_3 != decoding_bits.get(3)) {
            errorfinding.add(1);
        }
        else if (check_3 == decoding_bits.get(3)) {
            errorfinding.add(0);
        }

        if (check_2 != decoding_bits.get(1)) {
            errorfinding.add(1);
        }
        else if (check_2 == decoding_bits.get(1)) {
            errorfinding.add(0);
        }

        if (check_1 != decoding_bits.get(0)) {
            errorfinding.add(1);
        }
        else if (check_1 == decoding_bits.get(0)) {
            errorfinding.add(0);
        }

        //combine the groups that we can get the error bit's position
        int corret_flag = 8 * errorfinding.get(0) + 4 * errorfinding.get(1) + 2 * errorfinding.get(2) + errorfinding.get(3);
        if(corret_flag == 0){     // no bits error
            return BinaryAsciiToString(decoding_bits);
        }
        else {            //we assume that there is only one bit, if not, we can't find it. If there is only one bit, we can correct
            if(corret_flag <= decoding_bits.size()) {
                if (decoding_bits.get(corret_flag - 1) == 0) {
                    decoding_bits.set(corret_flag - 1, 1);
                } else if (decoding_bits.get(corret_flag - 1) == 1) {
                    decoding_bits.set(corret_flag - 1, 0);
                }
            }
        }
        return BinaryAsciiToString(decoding_bits);
    }

    //whether the number of bit 1 is even or not
    private static int even_one(int num) {
        if (num%2 == 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    private String BinaryAsciiToString(ArrayList<Integer> bits) {          // translate binary ascii code to String
        int ascii = 128 * bits.get(2) + 64 * bits.get(4) + 32 * bits.get(5)
                + 16 * bits.get(6) + 8 * bits.get(8) + 4 * bits.get(9) + 2 * bits.get(10) + bits.get(11);
        return "" + (char) ascii;
    }

    private void init(){
        text_task4 = (TextView)findViewById(R.id.tv4);
        button4 = (Button)findViewById(R.id.button4);
        button42 = (Button)findViewById(R.id.button42);
        button43 = (Button) findViewById(R.id.button43);
        for(int i=0; i<64; i++){
            FREQUENCIES[i] = 400 + 100 * i;     //using 64 frequencies to transimit
        }
        FREQUENCIES[64] = 6800;
        FREQUENCIES[65] = 7000;
        FREQUENCIES[66] = 6900;
    }
}