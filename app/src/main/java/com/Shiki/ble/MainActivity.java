package com.Shiki.ble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.Shiki.ble.Adapter.deviceAdapter;
import com.Shiki.ble.Helper.ScanHelper;
import com.Shiki.ble.Perssion.showContacts;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ScanHelper scanHelper = new ScanHelper();
    List<BleDevice> bleDeviceList;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final showContacts contacts = new showContacts();
        if(Build.VERSION.SDK_INT >= 23){
            contacts.showContacts(this);
        }

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance().enableLog(true).setReConnectCount(1,5000).setOperateTimeout(5000);

        final RecyclerView deviceView = (RecyclerView) findViewById(R.id.deviceList);
        final FloatingActionButton ScanButton = (FloatingActionButton) findViewById(R.id.startScan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        deviceView.setLayoutManager(layoutManager);

        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean IfSupport = BleManager.getInstance().isSupportBle();
                if (IfSupport){
                    boolean IfEnable = BleManager.getInstance().isBlueEnable();
                    if(!IfEnable){
                        BleManager.getInstance().enableBluetooth();
                    }
                    else {
                        if(flag == 0){
                            scanHelper.ScanStart();
                            deviceAdapter adapter = scanHelper.adapter;
                            deviceView.setAdapter(adapter);
                            flag = 1;
                        }else if(flag == 1){
                            BleManager.getInstance().cancelScan();
                            bleDeviceList = scanHelper.Device();
                            if(bleDeviceList.size() == 0){
                                Toast.makeText(MainActivity.this,"未发现可用设备",Toast.LENGTH_SHORT).show();
                            }
                            flag = 0;
                        }

                    }
                }else{
                    Toast.makeText(MainActivity.this,"手机不支持BLE",Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
