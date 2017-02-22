package com.example.rmn.date;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rmn.date.data.DBcontract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rmn on 21-08-2016.
 */
public class ExtraSubAdapter extends RecyclerView.Adapter<ExtraSubAdapter.Holder> {

    Context mContext;
    Cursor dataCursor;
    String date,day;
    EditDialog.EditCallback mEditCallback;

   public ExtraSubAdapter(Context context,EditDialog.EditCallback editCallback,String  week_day,String date){
       this.mContext=context;
       this.date=date;
       this.day=week_day;
       this.mEditCallback=editCallback;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.extra_sub_row,null);
        Holder holder=new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if (position==0){
            holder.toolbar.setVisibility(View.GONE);
            if (dataCursor.getCount()>0) {
                holder.statusView.setText("Extra Classes");
                holder.statusView.setTextSize(15);
                holder.statusLay.setBackgroundColor(Color.WHITE);
            }
            else {
                holder.statusLay.setVisibility(View.GONE);
                holder.statusView.setVisibility(View.GONE);
            }
        }
        else {
            dataCursor.moveToPosition(position-1);
            Log.e("mid ", dataCursor.getString(dataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_ID)));

            long cloid = dataCursor.getLong(dataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_ID));
            Log.e("iddddddddddd ", cloid + "....>>");
            Cursor cursor = mContext.getContentResolver()
                    .query(DBcontract.monDB.CONTENT_URI, null, DBcontract.monDB.MASTER_ID + "=? AND "+ DBcontract.monDB.COLUMN_DATE+"=? ", new String[]
                            {Long.toString(cloid),date}, null);

            if (cursor.getCount() > 0) {


                while (cursor.moveToNext()) {

                    final String sub = dataCursor.getString(dataCursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB));

                    holder.subView.setText(sub);

                    Date date1 = new Date(Long.parseLong(dataCursor.getString(dataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_TIME))));
                    DateFormat formatter = new SimpleDateFormat("HH:mm");
                    String dateFormatted = formatter.format(date1);
                    holder.timeView.setText(dateFormatted);

                    holder.periodView.setText(dataCursor.getString(dataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_PERIOD)));

                    holder._colId = cursor.getLong(cursor.getColumnIndex(DBcontract.monDB.MASTER_ID));

                    int attend = cursor.getInt(cursor.getColumnIndex(DBcontract.monDB.COLUMN_ATTEND));
                    int bunk = cursor.getInt(cursor.getColumnIndex(DBcontract.monDB.COLUMN_BUNK));
                    if (attend == 1 && bunk == 0) {
                        holder.statusView.setText("ATTEND");
                    } else if (attend == 0 && bunk == 1) {
                        holder.statusView.setText("BUNK");
                    }

                    holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.edit) {
                                EditDialog dialog = EditDialog
                                        .NewInstance(mEditCallback, 1, position, holder._colId, date, day, sub);
                                FragmentManager manager = ((Activity) mContext).getFragmentManager();
                                dialog.show(manager, "rmnkmr");
                                return true;
                            }
                            return false;
                        }
                    });
                }
                cursor.close();
            }else {
                holder.toolbar.setVisibility(View.GONE);
                holder.statusLay.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (dataCursor==null) {

            Log.e("setCursor to",0 +" == count ");
            return 0;
        }
        else{
            Log.e("setCursor to",dataCursor.getCount()+" ==+== count ");
        return dataCursor.getCount()+1;}
    }

    public void setCursor(Cursor cursor){
        if (cursor!=null)
        Log.e("setCursor to",cursor.getCount()+"--  count ");
        this.dataCursor=cursor;
    }


    public void swapItems(Cursor newCursor) {
        dataCursor = newCursor;
        Log.e("swapItems ", "clle");
        notifyDataSetChanged();
    }


    public class Holder extends RecyclerView.ViewHolder{

        TextView subView,timeView,periodView,statusView;
        Toolbar toolbar;
         long _colId;
        RelativeLayout statusLay;

        public Holder(View itemView) {
            super(itemView);
            subView=(TextView)itemView.findViewById(R.id.header);
            timeView=(TextView)itemView.findViewById(R.id.time);
            periodView=(TextView)itemView.findViewById(R.id.period);
            statusView=(TextView)itemView.findViewById(R.id.msg);
            statusLay=(RelativeLayout) itemView.findViewById(R.id.status);

            toolbar = (Toolbar) itemView.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.context_menu);
        }

    }
}
