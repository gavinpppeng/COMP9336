package com.example.lab3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.Manifest.*;
import static android.Manifest.permission.*;


public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private Button button1;
    private Button button2;
    private Button button3;
    private WifiManager mWifiManager;
    private List<ScanResult> SRL;
    private Boolean ifexists = Boolean.FALSE;
    private String TAG= "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        //find the uniwide and connect it
        findWifi();
        //Wifiversion();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if support 5G
                if (mWifiManager.is5GHzBandSupported())
                    tv1.setText("Your device supports 5G!");
                else
                    tv1.setText("Unfortunately, your device doesn't support 5G!");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                // 2.4GHz: 2.4-2.5 GHz, 5GHz: 4.9-5.9 GHz
                tv2.setText("Frequecy: "+ String.valueOf(wifiInfo.getFrequency()/1000) +  "GHz\n" + "Bit rate: " + String.valueOf(wifiInfo.getLinkSpeed()) + "Mbps");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv3.setText("802.11b and 802.11g: 2.4GHz\n" + "802.11a: 5GHz, up to 54 Mbps\n" + "802.11ac: 5GHz, VHT-OFDM, up to 346.8Mbps\n");
            }
        });
    }

    private void init(){
        tv1 =(TextView) findViewById(R.id.tv1);
        tv2 =(TextView) findViewById(R.id.tv2);
        tv3 =(TextView) findViewById(R.id.tv3);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
    }

    private void Wifiversion(){


    }

    //find the uniwide and connect to it
    private void findWifi() {
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        mWifiManager.setWifiEnabled(true);

        if (ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        if (mWifiManager.startScan()) {         // initially scan wifi nearby
            SRL = mWifiManager.getScanResults();
            Log.i(TAG, "ScanResults:" + SRL.toString());
            for (ScanResult item: SRL)
                if ("uniwide".equals(item.SSID))
                    ifexists = Boolean.TRUE;

            if (ifexists) {                      // find "uniwide" AP around
                Toast.makeText(getApplicationContext(), "'uniwide' AP already be found and will be connected.", Toast.LENGTH_LONG).show();
            }
            else {                              // doesn't find around
                Toast.makeText(getApplicationContext(), "Unfortunately, don't find the 'uniwide'.", Toast.LENGTH_LONG).show();
            }
        }

        WifiConfiguration configuration = new WifiConfiguration();
        WifiEnterpriseConfig enterConfig = new WifiEnterpriseConfig();

        String username = getString(R.string.un);
        String password = getString(R.string.pwd);

        configuration.SSID = "\"" + "uniwide" + "\"";
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
        configuration.status = WifiConfiguration.Status.ENABLED;

        enterConfig.setIdentity(username);
        enterConfig.setPassword(password);
        enterConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
        configuration.enterpriseConfig = enterConfig;

        int netId = mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(netId, true);
        mWifiManager.saveConfiguration();
        mWifiManager.reconnect();
    }
}
