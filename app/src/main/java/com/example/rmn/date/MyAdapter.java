package com.example.rmn.date;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rmn.date.data.DBcontract;
import com.example.rmn.date.data.DBcontract.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rmn on 10-02-2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> implements Alertdialog.DeleteCallback {

    Animation anim;
    int counter = 0;
    Uri uri;
    private String day, date;
    Button button;
    public static int anInt, buInt;
    private ArrayList<String> dataset;
    private Context context;
    private int position, holderid = 0;
    public static Cursor dataCursor;
    private View mEmptyView;
    private Alertdialog alert;
    List<Long> _idList;
    Alertdialog.DeleteCallback mDeleteCallback;
    EditDialog.EditCallback mEditCallback;

    String[] projection = {
            "SUM(" + monDB.COLUMN_ATTEND + ")",
            "SUM(" + monDB.COLUMN_BUNK + ")"};

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    @Override
    public void InvalidateOnDelete(Cursor newCursor) {
        Log.e("adapter ", "InvalidateOnDelete");
        swapItems(newCursor);
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        int tAttend, tBunk;
        List<Integer> list_type;
        TextView attendView, bunkView, header, percent, peiodView, msgView, timeView ;
        Button bunk;
        Button attend;
        Toolbar toolbar;
        long _subId, _colId, _updateId;
        int status = -1;
        int at, bu;
        RelativeLayout statusView;
        GridLayout grid;


        public viewHolder(View itemview) {
            super(itemview);

            status = -1;
            at = 0;
            bu = 0;
            _updateId = 0;
            statusView = (RelativeLayout) itemview.findViewById(R.id.status);
            grid = (GridLayout) itemview.findViewById(R.id.grid);
            list_type = new ArrayList<>();
            peiodView = (TextView) itemview.findViewById(R.id.period);
            timeView = (TextView) itemview.findViewById(R.id.time);
            percent = (TextView) itemview.findViewById(R.id.percent);
            header = (TextView) itemview.findViewById(R.id.header);
             bunk = (Button) itemview.findViewById(R.id.bunk);
            attend = (Button) itemview.findViewById(R.id.attend);
            attendView = (TextView) itemview.findViewById(R.id.tatal_attend);
            msgView = (TextView) itemview.findViewById(R.id.msg);
            bunkView = (TextView) itemview.findViewById(R.id.total_bunk);
            toolbar = (Toolbar) itemview.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.context_menu);
        }
    }

    public MyAdapter(Context context, Alertdialog.DeleteCallback deleteCallback, EditDialog.EditCallback editCallback, String day, String date) {
        this.context = context;
        this.day = day;
        Log.e("MWADAPTER DATE", date + "  DATE");
        this.date = date;
        _idList = new ArrayList<>();
        this.mDeleteCallback = deleteCallback;
        this.mEditCallback = editCallback;
    }

    public MyAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int view_type) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail, parent, false);
        context = parent.getContext();
        viewHolder vh = new viewHolder(view);
        holderid = 0;
        return vh;
    }

    public void onBindViewHolder(final viewHolder holder, final int pos) {

        dataCursor.moveToPosition(pos);

        holder._subId = dataCursor.getLong(dataCursor.getColumnIndex(SubTable.COLUMN_ID));
        holder._colId = dataCursor.getLong(dataCursor.getColumnIndex(DBmaster.COLUMN_ID));
        final String sub = dataCursor.getString(dataCursor.getColumnIndex(SubTable.COLUMN_SUB));
        holder.header.setText(sub);

         holder.peiodView.setText(dataCursor.getString(dataCursor.getColumnIndex(DBmaster.COLUMN_PERIOD)));

        Date date1 = new Date(Long.parseLong(dataCursor.getString(dataCursor.getColumnIndex(DBcontract.DBmaster.COLUMN_TIME))));
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        String dateFormatted = formatter.format(date1);
        holder.timeView.setText(dateFormatted);

        setAttendBunk(holder, 0);

        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        EditDialog dialog = EditDialog.NewInstance(mEditCallback, 0, pos, holder._colId, date, day, sub);
                        FragmentManager manager = ((Activity) context).getFragmentManager();
                        dialog.show(manager, "rmnkmr");
                        return true;

                    case R.id.delete:
                        Alertdialog alertdialog = Alertdialog.NewInstance(mDeleteCallback, holder._colId, day);
                        FragmentManager manager1 = ((Activity) context).getFragmentManager();
                        alertdialog.show(manager1, "rmnkmr");
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });


        holder.bunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Entry(holder, 0, 1);
                if (uri != null) {
                    holder._updateId = ContentUris.parseId(uri);
//                        _idList.add(ContentUris.parseId(uri));
//                        String text = "Bunk:" + ++holder.tBunk;
                    setAttendBunk(holder, 2);
//                        holder.bunk.setClickable(false);
//                        holder.attend.setClickable(true);
//                        holder.at = 0;
//                        holder.bu = 1;
                }
//                }
            }
        });

        holder.attend.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 anInt++;

//                if (holder._updateId != 0 && holder.bu != 0) {
//                    context.getContentResolver().delete(monDB.CONTENT_URI, monDB.COLUMN_ID + "=?", new String[]{Long.toString(holder._updateId)});
//
//                    holder._updateId = 0;
//                    Uri uri = Entry(holder, 1, 0);
//                    if (uri != null) {
//                        holder._updateId = ContentUris.parseId(uri);
//                        _idList.add(ContentUris.parseId(uri));
//                        String text = "Bunk:" + ++holder.tBunk;
//                        setAttendBunk(holder, 0);
//                        holder.bunk.setClickable(true);
//                        holder.attend.setClickable(false);
//                        holder.at = 1;
//                        holder.bu = 0;
//                    }
//                } else {
                                                 Uri uri = Entry(holder, 1, 0);
                                                 if (uri != null) {
                                                     holder._updateId = ContentUris.parseId(uri);
                                                     // _idList.add(ContentUris.parseId(uri));
                                                     String text = "Bunk:" + ++holder.tBunk;
                                                     setAttendBunk(holder, 1);
//                    holder.bunk.setClickable(true);
//                    holder.attend.setClickable(false);
//                    holder.at = 1;
//                    holder.bu = 0;
                                                 }
//                }
                                             }

                                         }
        );
    }

    public int getItemCount() {
        if (null == dataCursor)
            return 0;
        Log.e("getItemCount ", dataCursor.getCount() + "");
        return dataCursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        this.dataCursor = cursor;
        this.notifyDataSetChanged();
        if (dataCursor != null) {
            Log.e("swapCursor sizee data", dataCursor.getCount() + ")");
            Log.e("swapCursor sizee cur", cursor.getCount() + ")");
        }

    }

    public Cursor getCursor() {
        return dataCursor;
    }


    public Uri Entry(viewHolder holder, int at, int bu) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(monDB.MASTER_ID, holder._colId);
        contentValues.put(monDB.SUB_ID, holder._subId);
        contentValues.put(monDB.COLUMN_ATTEND, at);
        contentValues.put(monDB.COLUMN_BUNK, bu);
        contentValues.put(monDB.COLUMN_DATE, date);

        Log.e("onCreat entry masterid ", holder._colId + "");

        Log.e("onCreat entry date ", date + "date");
        uri = context.getContentResolver().insert(monDB.CONTENT_URI, contentValues);


//        String[] projection =
//                {DBcontract.DBmaster.COLUMN_ID, DBcontract.SubTable.COLUMN_SUB, DBcontract.DBmaster.COLUMN_PERIOD, DBcontract.SubTable.COLUMN_ID};
//
//        String selection = DBcontract.DBmaster.COLUMN_DAY + "= ?";
//        String[] arg = {day};
//        Log.e("weekday rmn ", day);
//
//        Log.e("onCreateLoader  ", " created");
//
//
//        Cursor cursor2= context.getContentResolver().query(monDB.CONTENT_URI,null,monDB.MASTER_ID+"=? AND "+monDB.COLUMN_DATE+"=?",new String[] {Long.toString(holder._colId),date},null);
//        if (cursor2!=null && cursor2.getCount()!=0){
//            while (cursor2.moveToNext()){
//                Log.e("CURSOR2 ON ENTRY ",  " "
//                        +" SIDm="+cursor2.getLong(cursor2.getColumnIndex(monDB.SUB_ID))+ " "
//                        +" SID="+cursor2.getLong(cursor2.getColumnIndex(monDB.MASTER_ID))
//                );
//            }
//cursor2.close();
//        }

        return uri;
    }


    public void setAttendBunk(viewHolder holder, int type) {

        Cursor cursor = getMasterDayCursor(holder._subId);
        cursor.moveToNext();

        anim = AnimationUtils.loadAnimation(context, R.anim.alpha);

        Log.e("at bu colid ", "colid " + holder._colId);


        Cursor cursor1 = context
                .getContentResolver()
                .query(monDB.CONTENT_URI, new String[]{monDB.MASTER_ID, monDB.COLUMN_ATTEND, monDB.COLUMN_BUNK}, monDB.MASTER_ID + "=? AND " + monDB.COLUMN_DATE + "=?",
                        new String[]{Long.toString(holder._colId), date}, null);

        if (cursor1 != null)
            if (cursor1.getCount() != 0) {
                if (cursor1.moveToNext()) {
                    Log.e("cursor1 ", cursor1.getCount() + "count");
                    Log.e("cursor1 ", cursor1.getLong(cursor1.getColumnIndex(monDB.MASTER_ID)) + "mid");
                    holder.statusView.setVisibility(View.VISIBLE);
                    holder.grid.setVisibility(View.GONE);

                    if (cursor1.getInt(cursor1.getColumnIndex(monDB.COLUMN_ATTEND)) == 1) {

                        Log.e("cursor1 attend 1", cursor1.getLong(cursor1.getColumnIndex(monDB.MASTER_ID)) + "mid");
                        if (holder.statusView.getVisibility() == View.VISIBLE)
//               animateRevealColorFromCoordinates(holder.statusView,R.color.attendColor,
//                                holder.statusView.getWidth()/2,0);

                            holder.msgView.setText("ATTEND");
                    } else {
                        if (holder.statusView.getVisibility() == View.VISIBLE) {
//                        Animator anim=animateRevealColorFromCoordinates(holder.statusView,R.color.bunkColor,
//                                holder.statusView.getWidth()/2,0);
                            Log.e("cursor1 bunk 1 ", cursor1.getLong(cursor1.getColumnIndex(monDB.MASTER_ID)) + "mid");
                            holder.msgView.setText("BUNK");
                        }
                    }
                }
            } else {
                holder.statusView.setVisibility(View.GONE);
                holder.grid.setVisibility(View.VISIBLE);

                if (cursor.getCount() > 0) {

                    setPercent(holder, cursor.getInt(0), cursor.getInt(1));
                    if (type == 1) {
                        holder.attendView.setText(cursor.getInt(0) + "");
                    } else if (type == 2) {
                        holder.bunkView.setText(cursor.getInt(1) + "");
                    } else if (type == 0) {
                        holder.attendView.setText(cursor.getInt(0) + "");
                        holder.bunkView.setText(cursor.getInt(1) + "");
                    }
                }
                cursor.close();
            }
        cursor1.close();


    }


    public Cursor getMasterDayCursor(long id) {

        Cursor main_cursor = context.getContentResolver()
                .query(monDB.CONTENT_URI, projection,
                        monDB.SUB_ID + " = ? ",
                        new String[]{Long.toString(id)}, null);
        return main_cursor;
    }


    public void setPercent(viewHolder holder, int attend, int bunk) {
        double prcnt = 0;
        int prct;
        String percent;

        if ((attend + bunk) == 0) {
            percent = "100";
        } else {
            prcnt = ((attend + 0.0) / (attend + bunk)) * 100;
            prct = ((int) prcnt);

            if (prcnt / prct == 1.0) {
                percent = String.format("%.0f", prcnt);
            } else {
                percent = String.format("%.2f", prcnt);
            }

        }
        holder.percent.setAnimation(anim);
        holder.percent.setText(percent + "%");
    }


    public void swapItems(Cursor newCursor) {
        dataCursor = newCursor;
        Log.e("swapItems ", "clle");
        notifyDataSetChanged();
    }




    public void leftrightTransition() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideLeft = new Slide();
            slideLeft.setSlideEdge(Gravity.LEFT);

        }
    }

}

