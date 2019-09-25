package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private ListView lv;
    private BroadcastReceiver mReceiver;
    Intent bluemanager;
    private Button buttonstatus;
    private Button buttondiscover;
    BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        buttonstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btAdapter.isEnabled()){
                    tv.setText("Bluetooth is enabled.");
                }
                else {
                    tv.setText("Bluetooth is disabled.");
                }
            }
        });

        buttondiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearView();
                resultList.clear();
                deviceList.clear();
                discover();
            }
        });

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null){
            tv.setText("Bluetooth not available!");
        }
    }

    private void init(){
        tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.lv);
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mReceiver = new BlueReceiver();
        bluemanager = this.registerReceiver(mReceiver, filter);
        buttonstatus = (Button) findViewById(R.id.CheckButton);
        buttondiscover = (Button) findViewById(R.id.DiscoveryButton);
    }

     lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    }
}
