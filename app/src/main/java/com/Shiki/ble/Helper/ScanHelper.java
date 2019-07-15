package com.Shiki.ble.Helper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.Shiki.ble.Adapter.deviceAdapter;
import com.Shiki.ble.MainActivity;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScanHelper {

    public List<BleDevice> bleDeviceList = new ArrayList<>();
    boolean Ifstart = false;
    public deviceAdapter adapter = new deviceAdapter(bleDeviceList);

    public void ScanHelper(UUID[] uuid, String mac, String deviceName){
        if(uuid != null && mac != null && deviceName != null){
            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                    .setServiceUuids(uuid)      // 只扫描指定的服务的设备，可选
                    .setDeviceName(true, deviceName)         // 只扫描指定广播名的设备，可选
                    .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                    .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                    .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                    .build();
            BleManager.getInstance().initScanRule(scanRuleConfig);
        }
    }

    public void ScanStart(){
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onLeScan(BleDevice bleDevice) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                bleDeviceList.add(bleDevice);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {

            }
        });
    }

    public List<BleDevice> Device(){
        return bleDeviceList;
    }
}
