package com.example.rmn.date;


import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.rmn.date.utiles.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment implements CriteriaDialog.updateCriteriaCallback {

    CheckBox mPeriCheckBox;
    String percent = "75";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    SettingAdapter adapter;
    CriteriaDialog.updateCriteriaCallback callbackContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyler);
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        adapter = new SettingAdapter(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        mPeriCheckBox = (CheckBox) view.findViewById(R.id.notify_check);
        mPeriCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isAllow;
                SharedPreferences preferences = getActivity().getSharedPreferences(Constants.PREF_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                if (mPeriCheckBox.isChecked()) {
                    isAllow = Constants.TRUE;
                } else {
                    isAllow = Constants.FALSE;
                }

                editor.putString(Constants.IS_ALLOW_NOTIFY, isAllow);
            }
        });

        callbackContext = this;
        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());

                if (child != null && gestureDetector.onTouchEvent(e)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    if (position == 0) {
                        CriteriaDialog alert = CriteriaDialog.NewInstance(callbackContext);
                        FragmentManager manager = getActivity().getFragmentManager();
                        alert.show(manager, "rmnkmr");
                    }
                }
                return false;

            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void updateCriteria() {
        adapter = new SettingAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        Log.e("updateCriteria", "called");
    }
}