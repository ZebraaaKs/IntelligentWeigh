package com.example.chen.intelligentweigh.activity.kidActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;

import java.util.Set;


public class DeviceListActivity extends BaseActivity {
    // Debugging  
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;  
  
    // Return Intent extra  
    public static String EXTRA_DEVICE_ADDRESS = "device_addresess";
  
    // Member fields  
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setup the window  
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

    }  

  
}  