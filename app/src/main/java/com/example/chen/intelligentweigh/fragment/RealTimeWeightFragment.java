package com.example.chen.intelligentweigh.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.kidActivity.DeviceListActivity;
import com.example.chen.intelligentweigh.bean.CowWeight;
import com.example.chen.intelligentweigh.bean.WeighData;
import com.example.chen.intelligentweigh.fragment.kidFragment.DeviceListFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.NetWorkUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * author : chen
 * date   : 2018/11/27  17:17
 * desc   : 对肉牛的实时称重
 */
public class RealTimeWeightFragment extends BaseFragment  {

    private TextView tv_real_id;
    private TextView tv_real_weight;
    private Button bt_real_sure;
    private boolean isTwoPan;
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    // private TextView mTextView;
    private static final String TAG = "MainActivity";
    private BluetoothAdapter mBluetoothAdapter;
    private List<Integer> mBuffer;
    private static final int MSG_NEW_DATA = 3;
    private final int HEX = 0;
    private final int ASCII = 2;
    private int mCodeType = HEX;
    private ConnectThread mConnectThread;
    public ConnectedThread mConnectedThread;
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    //private TextView tv_show;
    // private HashMap<String,String> map = new HashMap<>();
    private  int len = 0;
    private TextView tv_real_date;
    private LoadingDailog dialog;
     boolean flag = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.real_time_weight_frag, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mBuffer = new ArrayList<Integer>();
        tv_real_id = (TextView) view.findViewById(R.id.tv_real_id);
        tv_real_weight = (TextView) view.findViewById(R.id.tv_real_weight);
        bt_real_sure = (Button) view.findViewById(R.id.bt_real_sure);
        tv_real_date = (TextView) view.findViewById(R.id.tv_real_date);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "请打开蓝牙",
                    Toast.LENGTH_LONG).show();
            getActivity().finish();
            return;
        }
        new TitleBuilder(view).setTitleText("实时称重").setRightText("选择蓝牙连接").setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeType = HEX;
                mHandler.sendEmptyMessage(MSG_NEW_DATA);
                if (mConnectThread != null) {
                    mConnectThread.cancel();
                    mConnectThread = null;
                }
                if(isTwoPan){
                    DeviceListFragment fragment = new DeviceListFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
                }else{
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }

            }
        }).setLeftText("上传本地数据").setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(getActivity())
                        .setMessage("上传中...")
                        .setCancelable(false)
                        .setCancelOutside(false);
                dialog=loadBuilder.create();
                dialog.show();


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<CowWeight> list = LitePal.findAll(CowWeight.class);
                        for(CowWeight  cowWeight:list){
                            OkHttpUtils.get()
                                    .addParams("cattleid",cowWeight.getWeid())
                                    .addParams("phone", cowWeight.getPhone())
                                    .addParams("weigh",cowWeight.getWeight())
                                    .addParams("datetime",cowWeight.getDate())
                                    .url(HttpUrlUtils.ADDONEWEIGHT)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                            Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            flag = true;

                                        }
                                    });

                        }
                        if(flag) {
                            int i = LitePal.deleteAll(CowWeight.class);
                            Log.e(TAG, "删除" + i);
                        }
                        dialog.dismiss();
                    }

                },10000);


            }
        }).build();

        bt_real_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity()!=null) {
                    SubMitData();
                }
            }
        });

    }

    private void SubMitData() {
        if(tv_real_id.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请称重",Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_real_weight.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"请称重",Toast.LENGTH_SHORT).show();
            return;
        }
        if(NetWorkUtils.isNetworkConnected(getActivity())){
            OkHttpUtils.get()
                    .addParams("cattleid",tv_real_id.getText().toString())
                    .addParams("phone", SharedUtils.getPhone(getActivity()))
                    .addParams("weigh",tv_real_weight.getText().toString())
                    .addParams("datetime",tv_real_date.getText().toString())
                    .url(HttpUrlUtils.ADDONEWEIGHT)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Toast.makeText(getActivity(),"长传失败",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response) {
                            if("ok".equals(response)){
                                Toast.makeText(getActivity(),"上传成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"长传失败",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else{
            CowWeight cowWeight = new CowWeight();
            cowWeight.setWeid(tv_real_id.getText().toString());
            cowWeight.setPhone(SharedUtils.getPhone(getActivity()));
            cowWeight.setDate(tv_real_date.getText().toString());
            cowWeight.setWeight(tv_real_weight.getText().toString());
            Log.e(TAG,cowWeight.toString());
            cowWeight.save();
            Toast.makeText(getActivity(),"保存本地成功",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.other_content_frag) != null) {
            isTwoPan = true;
        } else {
            isTwoPan = false;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case MSG_NEW_DATA:
                    StringBuffer buf = new StringBuffer();
                    String str = "";
                    synchronized (mBuffer) {
                        if (mCodeType == ASCII) {
                            for (int i : mBuffer) {
                                buf.append((char) i);
                            }

                        } else if (mCodeType == HEX) {
                            for (int i : mBuffer) {
                                if (i > 9) {
                                    buf.append(Integer.toHexString(i));
                                }else{
                                    buf.append(0);
                                    buf.append(i);
                                }
                            }

                        } else {
                            for (int i : mBuffer) {
                                buf.append(i);
                            }
                        }
                        Log.e(TAG,"元数据"+buf.toString());
                            String data = buf.toString();
                            int strlength = data.length()-1;
                            char c[] = data.toCharArray();
                            if(strlength>61) {
                                if (c[strlength] == 'e' && c[strlength - 1] == 'f' && c[strlength - 60] == 'f' && c[strlength - 61] == 'f') {
                                    String data1 = data.substring(strlength - 61, strlength + 1);
                                    Log.e(TAG, "新数据" + data1);
                                    String date = formatDate(data1.substring(10, 18));
                                    Log.e(TAG, "日期" + date);
                                    String id = formatId(data1.substring(18, 48));
                                    Log.e(TAG, "编号" + id);
                                    String weight = hex2decimal(data1.substring(48, 56), data1.substring(58, 60));
                                    Log.e(TAG, "重量" + weight);
                                    tv_real_id.setText(id);
                                    tv_real_weight.setText(weight);
                                    tv_real_date.setText(date);
                            }
                            break;
                        }
                        len = buf.toString().length();




                    }


                    break;

                default:
                    break;
            }
        }

    };

    public String formatDate(String data){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String m1 = data.substring(0, 2);
        int i = Integer.parseInt(m1);
        String m2 = data.substring(2, 4);
        int i1 = Integer.parseInt(m2);
        int month = i+i1;
        String d1 = data.substring(4, 6);
        int i2 = Integer.parseInt(d1);
        String d2 = data.substring(6, 8);
        int i3 = Integer.parseInt(d2);
        int day = i2+i3;
        return year+"-"+month+"-"+day;
    }

    public String formatId(String data){
        String id = "";
        for(int i=0;i<data.length();i++){
            if(i==0||i%2==0){
                String d = data.substring(i,i+2);
                int i1 = Integer.parseInt(d);
                Log.e(TAG,"id数据"+i1);
                id = id+i1;
            }
        }
        return id;
    }



    public  String hex2decimal(String hex,String xiaoshu) {
        int k = Integer.parseInt(xiaoshu);
        String j   = "";
        for(int i=0;i<hex.length();i++){
            if(i==0||i%2==0){
                String substring = hex.substring(i, i + 2);
                int i1 = Integer.parseInt(substring);
                j = j+i1;
            }
        }
        Log.e(TAG,"j====="+j);

        return String.valueOf(Integer.parseInt(j)+"."+k);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled Launch the DeviceListActivity to see
                    // devices and do scan
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), "BT not enabled", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                if (resultCode != Activity.RESULT_OK) {
                    return;
                } else {
                    String address = data.getExtras().getString(
                            DeviceListFragment.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // Attempt to connect to the device
                    connect(device);
                }
                break;
        }
    }

    public void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a
     * device. It runs straight through; the connection either succeeds or
     * fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID
                        .fromString(SPP_UUID));
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {

                Log.e(TAG, "unable to connect() socket", e);
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG,
                            "unable to close() socket during connection failure",
                            e2);
                }
                return;
            }

            mConnectThread = null;

            // Start the connected thread
            // Start the thread to manage the connection and perform
            // transmissions
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();

        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device. It handles all
     * incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[256];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    synchronized (mBuffer) {
                        for (int i = 0; i < bytes; i++) {
                            mBuffer.add(buffer[i] & 0xFF);
                        }
                    }
                    mHandler.sendEmptyMessage(MSG_NEW_DATA);
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }




}
