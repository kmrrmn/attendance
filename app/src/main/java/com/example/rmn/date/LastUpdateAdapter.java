package com.example.rmn.date;

import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by rmn on 02-06-2016.
 */
public class LastUpdateAdapter extends RecyclerView.Adapter<LastUpdateAdapter.viewHolder> {

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.last_update_row,parent,false);
       viewHolder viewholder=new viewHolder(view,viewType);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        holder.subView.setText("sub");
        holder.at_bu.setText("2/3");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView subView,at_bu;
       public viewHolder(View itemView, int viewType){
           super(itemView);
           subView=(TextView)itemView.findViewById(R.id.sub);
           at_bu=(TextView)itemView.findViewById(R.id.at_bu);
       }
    }
}
