package com.example.lab4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button ButtonCheck;
    private Button ButtonDiscovery;
    private TextView tv;
    private ListView lv;
    WifiP2pManager WD_Manager;
    WifiP2pManager.Channel channel;
    List<String> peersInfo = new ArrayList<>();
    List<String> name = new ArrayList<>();
    List<String> address = new ArrayList<>();
    private IntentFilter intentFilter;
    private AlertDialog alertDialog;
    private String TAG ="Debug/info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        WD_Manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = WD_Manager.initialize(this, getMainLooper(), null);

        //Task 1: check the state of Wifi-Direct
        ButtonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (WD_Manager.WIFI_P2P_STATE_ENABLED!=2) {tv.setText("Unfortunately, Wifi-Direct is unavailable!");}
                else {
                    tv.setText("Wifi-Direct is available");
                    //make the button enable
                    ButtonDiscovery.setEnabled(true);
                }
            }
        });

        //Task 2: Scan peers and show the list
        ButtonDiscovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Scanning...", Toast.LENGTH_SHORT).show();
                ScanPeers();
            }
        });

    }

    private void init(){
        ButtonCheck = (Button) findViewById(R.id.CheckButton);
        ButtonDiscovery = (Button) findViewById(R.id.DiscoveryButton);
        tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.lv);

        intentFilter = new IntentFilter();  //register broadcast
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);  // indicate that the state of Wi-Fi p2p connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);    // indicate whether Wi-Fi p2p is enabled or disabled.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);     //indicate that the available peer list has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);  //indicate that this device details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);   // indicate that peer discovery has either started or stopped.
    }

    private void ScanPeers(){
        // scanning peers
        // The API is asynchronous and responses to requests from an application are on listener callbacks provided by the application.
        // The application needs to do an initialization with initialize(Context, Looper, WifiP2pManager.ChannelListener) (above) before doing any p2p operation.
        //Most application calls need a ActionListener instance for receiving callbacks ActionListener#onSuccess or ActionListener#onFailure.
        // Action callbacks indicate whether the initiation of the action was a success or a failure. Upon failure, the reason of failure can be one of ERROR, P2P_UNSUPPORTED or BUSY.
        WifiP2pManager.ActionListener actionListener = new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int i) {
                if (i==0) {
                    Toast.makeText(MainActivity.this, "Discovery failed due to an internal error.", Toast.LENGTH_SHORT).show();
                }
                else if(i==1) {
                    Toast.makeText(MainActivity.this, "Discovery failed due to p2p is unsupported on the device", Toast.LENGTH_SHORT).show();
                }
                else if(i==2){
                    Toast.makeText(MainActivity.this, "Discovery failed due to the framework is busy and unable to service the request", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //using broadcast to receive the information to show the peers list states
        BroadcastReceiver mReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                //setting the receiving information
                String action = intent.getAction();
                Log.i(TAG, "broadcast.action:"+action);

                //indicate whether Wi-Fi p2p is enabled or disabled
                if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
                }
                //indicate that the available peer list has changed.
                // This can be sent as a result of peers being found, lost or updated.
                else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
                    Toast.makeText(getApplicationContext(), "Wifi-direct peers changed action", Toast.LENGTH_SHORT).show();
                    //get the WiFi p2p device list from intent
                    WifiP2pDeviceList mPeers = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST);

                    peersInfo.clear();
                    name.clear();
                    address.clear();
                    for(WifiP2pDevice peers: mPeers.getDeviceList()){
                        Log.d(TAG, "Device info: "+ peers);
                        String temp = "DeviceAddress: "+ peers.deviceAddress + "\n" + peers.toString();
                        peersInfo.add(temp);
                        name.add(peers.deviceName);
                        address.add(peers.deviceAddress);
                    }

                    lv.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return peersInfo.size();
                        }

                        @Override
                        public Object getItem(int i) {
                            return peersInfo.get(i);
                        }

                        @Override
                        public long getItemId(int i) {
                            return i;
                        }

                        @Override
                        public View getView(int i, View view, ViewGroup viewGroup) {
                            LayoutInflater mlayoutInflater = LayoutInflater.from(context);
                            View mView = mlayoutInflater.inflate(R.layout.list_items,null);
                            TextView tv1 = (TextView) mView.findViewById(R.id.tx);
                            Log.i(TAG, "peersinfo:" + peersInfo);
                            tv1.setText(peersInfo.get(i));

                            return mView;
                        }
                    });


                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            mdialog(view, name.get(i), i);
                        }
                    });
                }
                else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){}
                else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
                    Toast.makeText(getApplicationContext(), "Wifi-direct connection state changed", Toast.LENGTH_SHORT).show();
                }
            }

            private void mdialog(View view, final String deviceName, final int Postion){
                LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.dialog, (ViewGroup)findViewById(R.id.view_dialog));
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WifiDirectConnect(address.get(Postion));
                    }
                });
                final TextView dialog_tv = (TextView) dialog.findViewById(R.id.tv_dialog);
                dialog_tv.setText("Connect with " + deviceName);
                alertDialog = builder.create();
                alertDialog.setView(dialog);
                alertDialog.show();

            }
        };
        registerReceiver(mReciver, intentFilter);
        WD_Manager.discoverPeers(channel, actionListener);
    }

    private void WifiDirectConnect(String deviceAddress){
        WifiP2pConfig config = new WifiP2pConfig();

        config.deviceAddress = deviceAddress;

        WD_Manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Connection request.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(getApplicationContext(), "Connection failed.", Toast.LENGTH_SHORT).show();
            }
        });
    };
}

