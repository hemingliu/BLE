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

import com.Shiki.ble.Adapter.charAdapter;
import com.Shiki.ble.DAO.characterMode;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CharacterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.characterView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String mac = intent.getStringExtra("mac");
        Log.d("UUID",mac);
        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        final BleDevice device = bleDeviceList.get(0);
        List<characterMode> characteristicList = new ArrayList<>();
        charAdapter adaper = new charAdapter(characteristicList);
        recyclerView.setAdapter(adaper);


        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(device);
        BluetoothGattService service = gatt.getService(UUID.fromString(mac));
        Log.d("UUID0",service.getUuid().toString());
        List<BluetoothGattCharacteristic> characteristics = BleManager.getInstance().getBluetoothGattCharacteristics(service);
        for(BluetoothGattCharacteristic characteristic:characteristics){
            int charaProp = characteristic.getProperties();
            Log.d("UUID1",String.valueOf(charaProp));
            StringBuffer property = new StringBuffer();
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                property.append("Read");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                property.append("Write");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                property.append("Write No Response");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                property.append("Notify");
                property.append(" , ");
            }
            if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                property.append("Indicate");
                property.append(" , ");
            }
            if (property.length() > 1) {
                property.delete(property.length() - 2, property.length() - 1);
            }
            characterMode mode = new characterMode();
            mode.setServiceUuid(mac);
            mode.setUuid(characteristic.getUuid().toString());
            mode.setCharactername(property.toString());
            Log.d("UUID2",property.toString());
            characteristicList.add(mode);
            adaper.notifyDataSetChanged();
        }
    }
}
