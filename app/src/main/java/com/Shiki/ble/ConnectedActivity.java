package com.Shiki.ble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.Shiki.ble.Adapter.serviceAdapter;
import com.Shiki.ble.DAO.ServiceMode;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

public class ConnectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        TextView name = (TextView) findViewById(R.id.name);
        TextView mac = (TextView) findViewById(R.id.mac);
        Button disconnect = (Button) findViewById(R.id.cancelConnect);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.serviceView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        final BleDevice device = bleDeviceList.get(0);

        name.setText("设备名称: "+device.getName());
        mac.setText("Mac地址："+device.getMac());
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleManager.getInstance().disconnect(device);
                Toast.makeText(ConnectedActivity.this,"断开连接",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConnectedActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        List<ServiceMode> list = new ArrayList<>();
        serviceAdapter adapter = new serviceAdapter(list);

        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(device);
        List<BluetoothGattService> serviceList = gatt.getServices();



        for(BluetoothGattService service:serviceList){
            Log.d("GATT",device.getName()+service.getUuid().toString());
            List<BluetoothGattCharacteristic> characteristics = BleManager.getInstance().getBluetoothGattCharacteristics(service);
            ServiceMode s1 = new ServiceMode();
            s1.setServiceNum("1");
            s1.setServiceMac(service.getUuid().toString());
            list.add(s1);
            adapter.notifyDataSetChanged();
        }

        recyclerView.setAdapter(adapter);

    }
}
