package com.Shiki.ble.Adapter;

import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Shiki.ble.ConnectedActivity;
import com.Shiki.ble.R;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.util.List;


public class deviceAdapter extends RecyclerView.Adapter<deviceAdapter.ViewHolder> {

    private List<BleDevice> mDevicelist;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView deviceName;
        TextView deviceMac;
        Button connectButton;
        ImageView imageView;

        public ViewHolder(View view){
            super(view);
            deviceName = (TextView)view.findViewById(R.id.deviceName);
            deviceMac =(TextView)view.findViewById(R.id.deviceMac);
            connectButton = (Button) view.findViewById(R.id.connect);
            imageView = (ImageView)view.findViewById(R.id.pic);
        }
    }

    public deviceAdapter(List<BleDevice> deviceList){
        mDevicelist = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BleDevice device = mDevicelist.get(position);
        holder.deviceName.setText("设备名称: "+device.getName());
        holder.deviceMac.setText("mac地址: "+device.getMac());
        holder.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                BleManager.getInstance().connect(device, new BleGattCallback() {
                    @Override
                    public void onStartConnect() {
                        Toast.makeText(view.getContext(),"开始连接",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectFail(BleDevice bleDevice, BleException exception) {
                        Toast.makeText(view.getContext(),"连接失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                        Toast.makeText(view.getContext(),"连接成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view.getContext(), ConnectedActivity.class);
                        view.getContext().startActivity(intent);
                    }

                    @Override
                    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mDevicelist != null){
            return mDevicelist.size();
        }
        else
            return 0;
    }
}
