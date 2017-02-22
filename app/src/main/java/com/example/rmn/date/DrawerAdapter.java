package com.example.rmn.date;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rmn on 14-05-2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.viewHolder>
{
    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;

    private String TITLES[];

    DrawerAdapter(String titles[]){
        TITLES=titles;
    }

    @Override
    public  viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.drawer_row,parent,false);

            viewHolder holder=new viewHolder(view );
            return holder;

    }

    @Override
    public void onBindViewHolder(DrawerAdapter.viewHolder holder, int position) {

            holder.row_item.setText(TITLES[position]);
            Log.e("TITLES ",position+" : "+TITLES[position]);
            Log.e("TITLES len"," : "+TITLES.length);
    }

    @Override
    public int getItemCount() {
        return TITLES.length;
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
       TextView row_item;
       int holder_id;
        public viewHolder(View itemView) {
            super(itemView);
                row_item = (TextView) itemView.findViewById(R.id.row_item);

        }
    }
}
