package com.example.rmn.date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rmn.date.data.DBcontract;


public class Subject_Detail extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    int LOADER_ID;
    SubDetailAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int position=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        position = getArguments().getInt("pos");

        adapter=new SubDetailAdapter(getActivity());
        View view=inflater.inflate(R.layout.all_sub,container,false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyler);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setPressed(true);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.scrollToPosition(position);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

             String[] projection={
                     DBcontract.SubTable.COLUMN_ID,
                     DBcontract.SubTable.COLUMN_SUB,
                     DBcontract.DBmaster.COLUMN_DAY,
                     DBcontract.DBmaster.COLUMN_PERIOD,
                     DBcontract.DBmaster.COLUMN_TIME,
                     "SUM("+DBcontract.monDB.COLUMN_ATTEND+")",
                     "SUM("+DBcontract.monDB.COLUMN_BUNK+")",DBcontract.DBmaster.COLUMN_ID
             };

         return new CursorLoader(getActivity(),
                DBcontract.AllDetail.CONTENT_URI,
               projection,null,null, DBcontract.SubTable.COLUMN_SUB+ " ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.e("onLoadFid  dta.gtcunt", " "+data.getCount());
        Log.e("onLoadFi   ", " "+data.getColumnName(0)+" "+data.getColumnName(1)+" "+data.getColumnName(2)
                +" "+data.getColumnName(3)+" "+data.getColumnName(4)+" "+data.getColumnName(5)
                +" "+data.getColumnName(6));


        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
           adapter.swapCursor(null);
    }
}
