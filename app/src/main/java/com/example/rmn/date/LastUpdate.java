package com.example.rmn.date;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.rmn.date.data.DBcontract;
/**
 * A simple {@link Fragment} subclass.
 */
public class LastUpdate extends Fragment implements LoaderManager.LoaderCallbacks{
    GridLayout gridLayout;
    NestedScrollView scroll;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    LastUpdateAdapter adapter;
    private static final int LOADER_ID=1;
    private String date="2/04/2016",day;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.last_update, container, false);
        scroll=(NestedScrollView)view.findViewById(R.id.scroll);
        gridLayout=(GridLayout)view.findViewById(R.id.grid);
        gridLayout.setVisibility(View.GONE);
        scroll.setVisibility(View.VISIBLE);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyler);
        manager=new LinearLayoutManager(getActivity());
        adapter=new LastUpdateAdapter();
         recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        date=getArguments().getString("date");
        day=getArguments().getString("day");
        Log.e("LAST UPDATE DAY ","DATTE "+date+" "+day);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }
    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        return null;
    }
    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }
    @Override
    public void onLoaderReset(Loader loader) {

    }
    //@Override
//    public void onDestroy() {
//        super.onDestroy();
//        Intent intent=new Intent(getActivity(),MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        getActivity().startActivity(intent);
//    }
}
