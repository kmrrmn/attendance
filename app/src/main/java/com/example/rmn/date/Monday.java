package com.example.rmn.date;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.rmn.date.data.DBcontract;


public class Monday extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Alertdialog.DeleteCallback, EditDialog.EditCallback {
    FloatingActionButton fab;
    RecyclerView recyclerView,extraRecyclerView;
    MyAdapter adapter;
    ExtraSubAdapter extraSubAdapter;
    RecyclerView.LayoutManager layoutManager;
    final static String Extra_sub = "sub", Extra_detail = "detail";
    private FragmentManager supportFragmentManager;
    private String arg, week_day, date;
    TextView tv;
    private static final int LOADER_ID = 0,LOADER_ID_EXTRA = 1;
    public static String[] COLUMN = new String[2];
    int pos = 0;

    public static Monday newInstance(String date, String extraDay) {
        Monday fragment = new Monday();
        Log.e("Monday newInstance ", "date " + date + " EXTRADAY " + extraDay);
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putString("weekDay", extraDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        week_day=getArguments().getString("weekDay");
        date=getArguments().getString("date");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            pos = getArguments().getInt("pos");
        } catch (Exception e) {

        }

        View view = inflater.inflate(R.layout.monday, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        extraRecyclerView = (RecyclerView) view.findViewById(R.id.recyler_view_extra);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyler_view_mon);
        recyclerView.setHasFixedSize(true);
        extraRecyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        extraRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        registerForContextMenu(recyclerView);
        recyclerView.scrollToPosition(pos);

        Log.e("mon day date ", date + "date");
        Log.e("mon day weekday ", week_day);

        tv = (TextView) view.findViewById(R.id.day);
        adapter = new MyAdapter(getActivity(), this, this, week_day, date);
        extraSubAdapter=new ExtraSubAdapter(getActivity(),this,week_day,date);

        recyclerView.setAdapter(adapter);
        extraRecyclerView.setAdapter(extraSubAdapter);
        return view;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = -1;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:

                Log.d("HHIIIIII RMNMNMNMN", "EDDIIIIIIIIII clikcd");
//                initiatePopupWindow();
                DialogFragment dialog = new dialog();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                dialog.show(fm, "NoticeDialogFragment");
                break;

            case R.id.delete:
                ContentValues cv = new ContentValues();

                cv.put(COLUMN[1], 0);
                //        String selection = DBcontract.DBmaster.COLUMN_SUB;
                //      selection = "?";

//                getContext().getContentResolver().update(DBcontract.DBmaster.CONTENT_URI,cv,selection,new String[] {})
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        getLoaderManager().initLoader(LOADER_ID_EXTRA, null, this);
        Log.e("onActivityCreated ", "called");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id==0){
        String[] projection =
                {DBcontract.DBmaster.COLUMN_ID, DBcontract.DBmaster.COLUMN_IS_EXTRA,DBcontract.SubTable.COLUMN_SUB, DBcontract.DBmaster.COLUMN_PERIOD, DBcontract.DBmaster.COLUMN_TIME,DBcontract.DBmaster.COLUMN_PERIOD, DBcontract.SubTable.COLUMN_ID};

        String selection = DBcontract.DBmaster.COLUMN_DAY + "= ? AND "+
                DBcontract.DBmaster.COLUMN_IS_EXTRA+"=?";
        String[] arg = {week_day,"no"};
        Log.e("weekday  ", week_day);

        Log.e("onCreateLoader  ", " created");
        return new CursorLoader(getActivity(),
                DBcontract.SubMasterTable.CONTENT_URI,
                projection,
                selection,
                arg,
                null
        );}

        else if(id==1){

            String[] projection =
                    {DBcontract.DBmaster.COLUMN_ID, DBcontract.DBmaster.COLUMN_IS_EXTRA,DBcontract.SubTable.COLUMN_SUB, DBcontract.DBmaster.COLUMN_PERIOD, DBcontract.DBmaster.COLUMN_TIME,DBcontract.DBmaster.COLUMN_PERIOD, DBcontract.SubTable.COLUMN_ID};

            String selection = DBcontract.DBmaster.COLUMN_DAY + "= ? AND "+
                    DBcontract.DBmaster.TABLE_NAME+"."+DBcontract.DBmaster.COLUMN_IS_EXTRA+" !=?   " ;
            String[] arg = {week_day,"no"};
            Log.e("weekday  ", week_day);

            Log.e("onCreateLoader  ", " created");

            return new CursorLoader(getActivity(),
                    DBcontract.SubMasterTable.CONTENT_URI,
                    null,
                    selection,
                    arg,
                    null
            );

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId()==0){
        if (data.getCount() == 0) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
            adapter.swapCursor(data);
        }
        Log.e("onLoadFinished size ", data.getCount() + "");
        }
        else if (loader.getId()==1){
       extraSubAdapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e("onLoaderReset ", "RESET");
        if (loader.getId()==0)
            adapter.swapCursor(null);
        else if (loader.getId()==1)
            extraSubAdapter.setCursor(null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void InvalidateOnDelete(Cursor cursor) {
        Log.e("mon ", "InvalidateOnDelete");
        MyAdapter adapter = (MyAdapter) recyclerView.getAdapter();
        adapter.swapItems(cursor);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.getAdapter().notifyItemChanged(0);

    }

    @Override
    public void editDetail(int token,int pos, Cursor cursor) {
        if (token==0) {
            recyclerView.setVerticalScrollbarPosition(pos);
            MyAdapter adapter = (MyAdapter) recyclerView.getAdapter();
            if (cursor != null)
                adapter.swapItems(cursor);

            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.getAdapter().notifyItemChanged(0);
        }
        else if (token==1){

            extraRecyclerView.setVerticalScrollbarPosition(pos);
            ExtraSubAdapter adapter = (ExtraSubAdapter) extraRecyclerView.getAdapter();

            if (cursor != null)
                adapter.swapItems(cursor);

            extraRecyclerView.getAdapter().notifyDataSetChanged();
            extraRecyclerView.getAdapter().notifyItemChanged(0);
        }

    }
}