package com.example.rmn.date;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rmn on 02-06-2016.
 */
public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.viewHolder> {
    private static final String DEFAULT="75";
Context context;

    public SettingAdapter(Context context){
        this.context=context;
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.settings_row,parent,false);
        viewHolder holder=new viewHolder(view,viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
             holder.header.setText("Criteria");
        SharedPreferences sf=context.getSharedPreferences("criteria1",context.MODE_PRIVATE);
        holder.percent.setText(sf.getString("percent",DEFAULT)+"%");
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView header,percent;

        public viewHolder(View itemview,int itemtype){
            super(itemview);
            header=(TextView)itemview.findViewById(R.id.criteria);
            percent=(TextView)itemview.findViewById(R.id.percent);
        }
    }
}
