package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private ListView listview;
    private Button buttonSensors;
    private Button buttonchange;
    private TextView text1;
    private TextView text3big;
    private TextView text3small;
    private ArrayList<String> showlist;
    public float[] gravity = new float[3];
    public float[] linear_acceleration = new float[3];
    final float alpha = (float) 0.8;
    private long last_time = 0;
    String flag = "Q1";
    private String TAG = "Debug!!!!!!!!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        buttonSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text1.setVisibility(View.VISIBLE);
                listview.setVisibility(View.VISIBLE);
                task1();
            }
        });

        buttonchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonchange.getText().toString().contains("Q1")){
                    flag = "Q2";
                    buttonchange.setText(flag);
                    buttonSensors.setVisibility(View.INVISIBLE);
                    listview.setVisibility(View.INVISIBLE);
                }
                else if(buttonchange.getText().toString().contains("Q2")){
                    flag="Q3";
                    buttonchange.setText(flag);
                    text1.setVisibility(View.INVISIBLE);
                    text3small.setVisibility(View.VISIBLE);
                    text3big.setVisibility(View.VISIBLE);
                }
                else if(buttonchange.getText().toString().contains("Q3")){
                    flag = "Q1";
                    buttonchange.setText(flag);
                    text3small.setVisibility(View.INVISIBLE);
                    text3big.setVisibility(View.INVISIBLE);
                    buttonSensors.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.VISIBLE);
                    text1.setText("Available sensors on this phone");
                }

            }
        });
    }

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void init(){
        //initialize the sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        text1 = (TextView) findViewById(R.id.text1);
        text3small = (TextView) findViewById(R.id.text3small);
        text3big = (TextView) findViewById(R.id.text3big);
        listview = (ListView) findViewById(android.R.id.list);
        buttonSensors = (Button) findViewById(R.id.button1);
        buttonchange = (Button) findViewById(R.id.button2);

    }

    //show all the sensors on the phone
    private void task1(){
        // getting all the sensor on the phone
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        showlist = new ArrayList<String>();
        for(int i=0; i<sensorList.size(); i++){
            // getting the type of the sensor
            mSensor = mSensorManager.getDefaultSensor(sensorList.get(i).getType());
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
            //getting all the information from the sensor
            showlist.add((i+1)+") "+ sensorList.get(i).toString());
        }
        listview.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.list_item, R.id.show_list, showlist));
    }

    // Called when there is a new sensor event. Note that "on changed" is somewhat of a misnomer,
    // as this will also be called if we have a new reading from a sensor with the exact same sensor values
    // (but a newer timestamp).
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate

            float result = last_time/event.timestamp;
            Log.i("Last time is ~~~~~~~~~~~~", String.valueOf(last_time));
            last_time = event.timestamp;
            Log.i("Time stamp is ~~~~~~~~~~~~~~~~~", String.valueOf(event.timestamp));

            Log.i(TAG,String.valueOf(result));

            //All values are in SI units (m/s^2)
            //values[0]: Acceleration minus Gx on the x-axis
            //values[1]: Acceleration minus Gy on the y-axis
            //values[2]: Acceleration minus Gz on the z-axis
            // This low-pass filter use this formula:
            // Y(n)=αX(n)+(1-α)Y(n-1)
            //A low-pass filter can be used to isolate the force of gravity.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            //It should be apparent that in order to measure the real acceleration of the device,
            // the contribution of the force of gravity must be eliminated.
            // This can be achieved by applying a high-pass filter
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
            if(flag == "Q2"){
                task2(gravity[0], gravity[1], gravity[2], linear_acceleration[0], linear_acceleration[1], linear_acceleration[2]);
            }
            if(flag == "Q3"){
                task3(gravity[0], gravity[1], gravity[2]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        return;
    }

    private void task2(double x, double y, double z, double withoutG_x, double withoutG_y, double withoutG_z){
        List< Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        Log.i(TAG, sensorList.toString());
        Log.i("hhhhhh", String.valueOf(sensorList.size()));
        String text1th = "Acceleration force including gravity\nX: "+x+"\nY: "+y+"\nZ: "+z;
        String text2nd = "\nAcceleration force without gravity\nX: "+withoutG_x+"\nY: "+withoutG_y+"\nZ: "+withoutG_z;
        text1.setText(text1th+text2nd);
    }

    private void task3(double x, double y, double z){
        float threshold = (float) 8.5;
        //The X axis is horizontal and points to the right,
        // the Y axis is vertical and points up and the Z axis points towards the outside of the front face of the screen.
        // In this system, coordinates behind the screen have negative Z values.
        // In fact, putting the phone in a stable state will make the corresponding value(x, y or z axis) stay a fixed value (about 9.8)
        // but using 8.5 can improve the sensitivity

        if(x > threshold){
            text3big.setText("Left");
        }
        else if (x < threshold*-1){
            text3big.setText("Right");
        }
        else if (y > threshold){
            text3big.setText("Default");
        }
        else if (y < threshold*-1){
            text3big.setText("Upside Down");
        }
        else if (z > threshold){
            text3big.setText("On the table");
        }
        else
            text3big.setText(" not stable\n");
    }

}
