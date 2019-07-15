package com.Shiki.ble.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Shiki.ble.CharacterActivity;
import com.Shiki.ble.DAO.ServiceMode;
import com.Shiki.ble.R;

import java.util.List;

public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.ViewHolder> {
    private List<ServiceMode> mserviceList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView serviceNum;
        TextView serviceMac;
        Button enterButton;

        public ViewHolder(View view){
            super(view);
            serviceNum = (TextView) view.findViewById(R.id.serviceNum);
            serviceMac = (TextView) view.findViewById(R.id.serviceMac);
            enterButton = (Button) view.findViewById(R.id.enterService);
        }
    }

    public serviceAdapter(List<ServiceMode> serviceList){
        mserviceList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item,parent,false);
        ViewHolder holder = new serviceAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull serviceAdapter.ViewHolder holder, int position) {
        final ServiceMode s1 = mserviceList.get(position);
        holder.serviceNum.setText("服务"+position);
        holder.serviceMac.setText(s1.getServiceMac());
        holder.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CharacterActivity.class);
                intent.putExtra("mac",s1.getServiceMac());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mserviceList.size();
    }
}
