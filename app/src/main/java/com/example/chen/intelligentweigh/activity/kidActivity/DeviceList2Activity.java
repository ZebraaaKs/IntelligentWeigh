package com.example.chen.intelligentweigh.activity.kidActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.example.chen.intelligentweigh.BaseActivity;
import com.example.chen.intelligentweigh.R;


public class DeviceList2Activity extends BaseActivity {
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