package com.Shiki.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.Shiki.ble.File.WriteData;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NotifyActivity extends AppCompatActivity {

    Button notifyButton;
    Button stopButton;
    LineChart mLineChar;
    List<Integer> dataTen = new ArrayList<>();
    WriteData writeData = new WriteData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        notifyButton = (Button) findViewById(R.id.notify);
        stopButton = (Button) findViewById(R.id.stopnotify);
        mLineChar = (LineChart) findViewById(R.id.mLineChar);

        mLineChar.setDrawGridBackground(false);
        mLineChar.getDescription().setEnabled(true);
        mLineChar.setTouchEnabled(true);
        mLineChar.setDragEnabled(true);
        mLineChar.setScaleEnabled(true);
        mLineChar.setPinchZoom(true);
        final XAxis xAxis = mLineChar.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        Intent intent = getIntent();
        String uuid = intent.getStringExtra("UUID");
        String serviceUuid = intent.getStringExtra("service");
        List<BleDevice> bleDeviceList = BleManager.getInstance().getAllConnectedDevice();
        final BleDevice device = bleDeviceList.get(0);

        BluetoothGatt gatt = BleManager.getInstance().getBluetoothGatt(device);
        BluetoothGattService service = gatt.getService(UUID.fromString(serviceUuid));
        final BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(uuid));


        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                BleManager.getInstance().notify(
                        device,
                        characteristic.getService().getUuid().toString(),
                        characteristic.getUuid().toString(),
                        new BleNotifyCallback() {
                            @Override
                            public void onNotifySuccess() {
                                Toast.makeText(NotifyActivity.this,"打开Notify成功",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNotifyFailure(BleException exception) {
                                Log.d("Failure",exception.toString());
                                Toast.makeText(NotifyActivity.this,"打开Notify失败",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCharacteristicChanged(byte[] data) {
                                int i = 0;
                                for(i=0;i<data.length;i=i+2){
                                    int s = 0;
                                    s = (s ^ data[i]);  //将b1赋给s的低8位
                                    s = (s << 8);  //s的低8位移动到高8位
                                    s = (s ^ data[i+1]); //在b2赋给s的低8位

                                    int value = 0;
                                    value = s;
                                    /*if(s < 0){
                                        value = 65535+1+s;
                                    }else{
                                        value = new BigDecimal(s).intValue();
                                    }*/

                                    dataTen.add(value);
                                    List<Entry> entries = new ArrayList<>();
                                    int b = 1;
                                    for(Integer d:dataTen){
                                        entries.add(new Entry(b,d));
                                        b++;
                                    }
                                    LineDataSet dataSet = new LineDataSet(entries,"震颤指数");
                                    LineData lineData = new LineData(dataSet);
                                    mLineChar.setData(lineData);
                                    mLineChar.setVisibleXRangeMaximum(20);
                                    mLineChar.moveViewToX(xAxis.getAxisMaximum()-20);
                                    mLineChar.notifyDataSetChanged();
                                }
                                /*for(byte b:data){
                                    int value = (b & 0X0FF);
                                    Log.d("BYTE",String.valueOf(b));
                                    dataTen.add(value);
                                    List<Entry> entries = new ArrayList<>();
                                    int i = 1;
                                    for(Integer d:dataTen){
                                        entries.add(new Entry(i,d));
                                        i++;
                                    }
                                    LineDataSet dataSet = new LineDataSet(entries,"震颤指数");
                                    LineData lineData = new LineData(dataSet);
                                    mLineChar.setData(lineData);
                                    mLineChar.setVisibleXRangeMaximum(20);
                                    mLineChar.moveViewToX(xAxis.getAxisMaximum()-20);
                                    mLineChar.notifyDataSetChanged();
                                }*/
                            }
                        }
                    );
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopButton.setVisibility(View.GONE);
                notifyButton.setVisibility(View.VISIBLE);
                StringBuffer stringBuffer = new StringBuffer();
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  hh:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String str = format.format(date);
                stringBuffer.append("\n"+str+":\n");
                for(Integer value :dataTen){
                    if(value != null){
                        stringBuffer.append(value.toString()+",");
                    }
                }
                writeData.writeToFile(stringBuffer.toString());
                BleManager.getInstance().stopNotify(
                        device,
                        characteristic.getService().getUuid().toString(),
                        characteristic.getUuid().toString());
            }
        });
    }
}
