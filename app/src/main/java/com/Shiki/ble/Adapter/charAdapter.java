package com.Shiki.ble.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Shiki.ble.DAO.characterMode;
import com.Shiki.ble.NotifyActivity;
import com.Shiki.ble.R;

import java.util.List;

public class charAdapter extends  RecyclerView.Adapter<charAdapter.ViewHolder> {
    private List<characterMode> mcharList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView charnum;
        TextView charuuid;
        TextView charContent;
        Button enterButton;

        public ViewHolder(View view){
            super(view);
            charnum = (TextView) view.findViewById(R.id.charnum);
            charuuid = (TextView) view.findViewById(R.id.charuuid);
            charContent = (TextView) view.findViewById(R.id.charContent);
            enterButton = (Button) view.findViewById(R.id.enterchar);
        }
    }

    public charAdapter(List<characterMode> charList){
        mcharList = charList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.char_item,parent,false);
        ViewHolder holder = new charAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final characterMode s = mcharList.get(position);
        holder.charnum.setText("服务： "+ position);
        holder.charuuid.setText(s.getUuid());
        holder.charContent.setText("特性("+s.getCharactername()+")");
        holder.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s.getCharactername().contains("Notify")){
                    Intent intent = new Intent(view.getContext(), NotifyActivity.class);
                    intent.putExtra("UUID",s.getUuid());
                    intent.putExtra("service",s.getServiceUuid());
                    view.getContext().startActivity(intent);
                }else{
                    Toast.makeText(view.getContext(),"只能选择notify进行操作",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mcharList.size();
    }
}
