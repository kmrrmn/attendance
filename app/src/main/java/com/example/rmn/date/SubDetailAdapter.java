package com.example.rmn.date;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
 import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rmn.date.data.DBcontract;

import java.util.List;

/**
 * Created by rmn on 15-05-2016.
 */

public class SubDetailAdapter extends RecyclerView.Adapter<SubDetailAdapter.DetailHolder> {
    List<String> list;
    Context context;
    static Cursor dataCursor;
    SubDetailAdapter (Context context){
        this.context=context;
     }

    public class DetailHolder extends RecyclerView.ViewHolder implements subNameChangeDialog.OnSubNameChangeListener{
        long subid;
        TextView sub,attend,bunk,percent,su,mo,tu,we,th,fr,sa;
        Toolbar toolbar;
        subNameChangeDialog.OnSubNameChangeListener instanceListener =this;
        public DetailHolder(View itemView) {
            super(itemView);

            su=(TextView)itemView.findViewById(R.id.sun1);
            mo=(TextView)itemView.findViewById(R.id.mon1);
            tu=(TextView)itemView.findViewById(R.id.tue1);
            we=(TextView)itemView.findViewById(R.id.wed1);
            th=(TextView)itemView.findViewById(R.id.thu1);
            fr=(TextView)itemView.findViewById(R.id.fri1);
            sa=(TextView)itemView.findViewById(R.id.sat1);
            sub=(TextView)itemView.findViewById(R.id.sub);
            attend=(TextView)itemView.findViewById(R.id.attend);
            bunk=(TextView)itemView.findViewById(R.id.bunk);
            percent=(TextView)itemView.findViewById(R.id.percent);
            toolbar=(Toolbar)itemView.findViewById(R.id.tool);
            toolbar.inflateMenu(R.menu.edit_detail);
        }

        @Override
        public void OnSubNameChange(String newSubName) {
            Log.e("OnSubNameChange ","called");
            sub.setText(newSubName);
        }

    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(context).inflate(R.layout.sub_detail_row,parent,false);
        DetailHolder holder=new DetailHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DetailHolder holder, final int position) {

        dataCursor.moveToPosition(position);

        holder.subid=dataCursor.getLong(dataCursor.getColumnIndex(DBcontract.SubTable.COLUMN_ID));
        String sub=dataCursor.getString(dataCursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB));
        holder.sub.setText(sub);
        holder.attend.setText( dataCursor.getInt(5)+"");
        holder.bunk.setText(Integer.toString(dataCursor.getInt(6)));

    setPercent(holder,dataCursor.getInt(5),dataCursor.getInt(6));

        Cursor cursor=context
                .getContentResolver()
                .query(DBcontract.DBmaster.CONTENT_URI,
                        new String[] {DBcontract.DBmaster.COLUMN_DAY},
                        DBcontract.DBmaster.SUB_ID+"=?",new String[]{Long.toString(holder.subid)},null);

        String sucolor="#ffffff";
        String mocolor="#ffffff";
        String tucolor="#ffffff";
        String wecolor="#ffffff";
        String thcolor="#ffffff";
        String frcolor="#ffffff";
        String sacolor="#ffffff";

        if (cursor!=null){
            if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    switch (cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_DAY))){
                        case "Mon":
                            mocolor="#00dddd";
                             break;
                        case "Tue":
                            tucolor="#00dddd";
                            break;
                        case "Wed":
                            wecolor="#00dddd";
                            break;
                        case "Thu":
                            thcolor="#00dddd";
                            break;
                        case "Fri":
                            frcolor="#00dddd";
                            break;
                        case "Sat":
                            sacolor="#00dddd";
                            break;
                        case "Sun":
                            sucolor="#00dddd";
                            break;
                    }
                }
            }
        }


        holder.su.setTextColor(Color.parseColor(sucolor));
        holder.mo.setTextColor(Color.parseColor(mocolor));
        holder.tu.setTextColor(Color.parseColor(tucolor));
        holder.we.setTextColor(Color.parseColor(wecolor));
        holder.th.setTextColor(Color.parseColor(thcolor));
        holder.sa.setTextColor(Color.parseColor(sacolor));
        holder.fr.setTextColor(Color.parseColor(frcolor));

        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.edit){
                          subNameChangeDialog dialog=subNameChangeDialog.NewInstance(holder.instanceListener,holder.sub.getText().toString(),holder.subid);
                    android.app.FragmentManager manager1 = ((Activity) context).getFragmentManager();
                    dialog.show(manager1,"ds");
                }
                return true;
            }
        });
    }

    public void swapCursor(Cursor cursor){
        this.dataCursor=cursor;

        if(dataCursor!=null){
            try {
                Log.e("swpcrsr_dtcrsr_>count  ", String.valueOf(dataCursor.getCount()));
            }catch (Exception e){
                Log.e("swp cath",e.getMessage());
            }
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null==dataCursor)return 0;
        return dataCursor.getCount();
    }

    public void setPercent(DetailHolder holder,int attend,int bunk){
        double prcnt;
        int prct;
        String percent;

        if ((attend+bunk)==0){
            percent="100";
        }else {
                prcnt = ((attend + 0.0) / (attend + bunk)) * 100;
            Log.e("prcnt ",prcnt+"");
                prct = ((int) prcnt);
            Log.e("attend ",attend+"");
            Log.e("bunk ",bunk+"");

                if (prcnt / prct == 1.0) {
                    percent = String.format("%.0f", prcnt);
                    Log.e("prcnt 1.0 ",prcnt+"");
                } else {
                     percent = String.format("%.2f", prcnt);
                    Log.e("prcnt",prcnt+"");
                }
        }
        holder.percent.setText(percent+"%");
    }


}
