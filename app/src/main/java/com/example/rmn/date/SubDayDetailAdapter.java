package com.example.rmn.date;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rmn.date.data.DBcontract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rmn on 03-08-2016.
 */
public class SubDayDetailAdapter extends RecyclerView.Adapter<SubDayDetailAdapter.Holder> {

    Context context;
    Cursor mDataCursor;

    public SubDayDetailAdapter(Context context, Cursor cursor) {
        this.context = context;

        mDataCursor = cursor;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_day_detail_row, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Log.e("onBindViewHolder cont", mDataCursor.getCount() + "");
        mDataCursor.moveToPosition(position);
        holder.sub.setText(mDataCursor.getString(mDataCursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB)));
        holder.day.setText(mDataCursor.getString(mDataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_DAY)));
        holder.period.setText(mDataCursor.getString(mDataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_PERIOD)));

        Date date = new Date(Long.parseLong(mDataCursor.getString(mDataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_TIME))));
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateFormatted = formatter.format(date);
        holder.time.setText(dateFormatted);
    }

    @Override
    public int getItemCount() {
        if (mDataCursor != null) {
            Log.e("itemcount ", mDataCursor.getCount() + "");
            return mDataCursor.getCount();
        } else return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView sub, day, period, time;

        public Holder(View itemView) {
            super(itemView);
            sub = (TextView) itemView.findViewById(R.id.sub);
            day = (TextView) itemView.findViewById(R.id.day);
            period = (TextView) itemView.findViewById(R.id.period);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

    void SwapCursor(Cursor cursor) {
        mDataCursor = cursor;
    }
}
