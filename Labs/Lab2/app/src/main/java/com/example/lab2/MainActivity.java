package com.example.lab2;

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
import android.os.Looper;
import android.renderscript.ScriptC;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView tv;
    private TextView iptv;
    private ListView lv;
    private WifiManager mWifiManager;
    private List<ScanResult> ScanWifi;
    private Set WifiName = new HashSet();
    private String[] WifiAttributes;
    private List<String> ESSID = new ArrayList<String>();
    private String TAG = "Debug/info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Setting the listview visible
                lv.setVisibility(View.VISIBLE);
                findWifi();
            }
        });
        }

        // initializing some parameters
        private void init(){
        tv =(TextView) findViewById(R.id.tv);
        button = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.lv);
        iptv = (TextView) findViewById(R.id.ip_text);
        }

        // scaning some wifi and then input the username and password and then connecting
        private void findWifi(){
            mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            //getting Wi-Fi states and enable Wi-Fi
            if(!mWifiManager.isWifiEnabled() && mWifiManager.getWifiState() != mWifiManager.WIFI_STATE_ENABLING){
                mWifiManager.setWifiEnabled(true);
            }

            // enable the permission of access coarse location
            // if don't do that, can't get the result
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }

            //starting wifi scan
            if(mWifiManager.startScan()){
                ScanWifi = mWifiManager.getScanResults();
                if(ScanWifi.size()!=0){
                    // getting all the wifi result and sorting them by their level(signal strength)
                    for (int i = 0; i < ScanWifi.size(); i ++) {
                        for (int j = 0; j < ScanWifi.size() - 1; j ++) {
                            if (ScanWifi.get(j).level < ScanWifi.get(j + 1).level){
                                ScanResult temp = ScanWifi.get(j);
                                ScanWifi.set(j, ScanWifi.get(j + 1));
                                ScanWifi.set(j+ 1, temp);
                            }
                }
            }
                    Log.d(TAG, ScanWifi.toString());
                    WifiName.clear();
                    ESSID.clear();
                    int count = 0;
                    WifiAttributes = new String[ScanWifi.size()];
                    for (int i=0; i<ScanWifi.size();i++) {
                        //  Task 2 only show 4 different signal strength
                        /* if(!WifiName.contains(ScanWifi.get(i).SSID)){
                            WifiName.add(ScanWifi.get(i).SSID);
                            ESSID.add(ScanWifi.get(i).SSID);
                        WifiAttributes[count] = String.valueOf(count + 1) + "." + ScanWifi.get(i).SSID + " BSSID: " + ScanWifi.get(i).BSSID +
                                " Capabilites: " + ScanWifi.get(i).capabilities + " Signal Strength: " + ScanWifi.get(i).level;
                        count ++;
                         */
                            //   show all the Wi-Fi
                        WifiAttributes[i] = String.valueOf(i + 1) + "." + ScanWifi.get(i).SSID + " BSSID: " + ScanWifi.get(i).BSSID +
                                    " Capabilites: " + ScanWifi.get(i).capabilities + " Signal Strength: " + ScanWifi.get(i).level;

                        //Log.d(TAG, ScanWifi.get(i).SSID);

                          /*  //Task 2 only show 4 different signal strength
                            if (count==4){
                                break;
                            }
                        }   //  */
                    }
                    lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, WifiAttributes));
                    }
                }
            //listview.setVisibility(View.VISIBLE);

            // click list view to connect wifi
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int Position = i;
                    final WifiConfiguration configuration =new WifiConfiguration();
                    final WifiEnterpriseConfig enterConfig =new WifiEnterpriseConfig();

                    Log.i(TAG, String.valueOf(i));

                    //getting the alert dialog
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialog = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.dialog));
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    //setting the alert dialog
                    builder.setTitle("Connect to " + ESSID.get(i));

                    // just quit
                    builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    // connect the Wi-Fi
                    builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            EditText username = (EditText) dialog.findViewById(R.id.username);
                            EditText password = (EditText) dialog.findViewById(R.id.password);

                            //getting the username and password from EditText
                            String un = String.valueOf(username.getText());
                            String pwd = String.valueOf(password.getText());

                            configuration.SSID = "\"" + ESSID.get(Position) + "\"";
                            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                            configuration.status = WifiConfiguration.Status.ENABLED;

                            enterConfig.setIdentity(un);
                            enterConfig.setPassword(pwd);
                            enterConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);

                            configuration.enterpriseConfig = enterConfig;

                            int netId = mWifiManager.addNetwork(configuration);
                            mWifiManager.enableNetwork(netId, true);
                            mWifiManager.saveConfiguration();
                            mWifiManager.reconnect();

                            //getting Wi-Fi information
                            Thread thread = new Thread(new Runnable() {
                                @SuppressLint("WrongConstant")
                                @Override
                                public void run() {
                                    Log.i(TAG, "enter thread...");
                                    WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
                                    while (wifiInfo.getIpAddress() == 0) {
                                        wifiInfo = mWifiManager.getConnectionInfo();
                                        Log.i(TAG, String.valueOf(wifiInfo.getIpAddress()));
                                    }
                                    int IpAddress = wifiInfo.getIpAddress();
                                    String ESSID = wifiInfo.getSSID();

                                    Log.i(TAG, String.valueOf(IpAddress));
                                    @SuppressLint("DefaultLocale") String ipv4 = String.format("%d.%d.%d.%d",
                                            (IpAddress & 0xff),
                                            (IpAddress >> 8 & 0xff),
                                            (IpAddress >> 16 & 0xff),
                                            (IpAddress >> 24 & 0xff));
                                    tv.setText("ESSID: " + ESSID + "\nIP address: " + ipv4);

                                    Looper.prepare();

                                    //getting the alert dialog
                                    LayoutInflater ip_inflater = getLayoutInflater();
                                    final View ip_info = ip_inflater.inflate(R.layout.ip_information, (ViewGroup) findViewById(R.id.ip));
                                    TextView a = (TextView) ip_info.findViewById(R.id.ip_text);
                                    a.setText("ESSID: " + ESSID + "\nIP address: " + ipv4);
                                    AlertDialog.Builder ip_builder = new AlertDialog.Builder(MainActivity.this);

                                    //setting the alert dialog
                                    ip_builder.setTitle("Wi-Fi connected!...");

                                    // just quit
                                    ip_builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                                    ip_builder.setView(ip_info);
                                    ip_builder.show();
                                   // Toast.makeText(getApplicationContext(), "ESID:" + ESSID + "\nIP address:" + ipv4, 0).show();
                                    Looper.loop();
                                }
                            });
                            thread.start();
                        }
                    }).setView(dialog);

                    // if it is not "WPA" mode, it will not react
                    if(ScanWifi.get(i).capabilities.contains("WPA")){
                        builder.show();
                    }
                }
            });

            }

    }

