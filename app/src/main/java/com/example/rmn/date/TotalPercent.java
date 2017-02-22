package com.example.rmn.date;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;

/**
 * A simple {@link Fragment} subclass.
 */
public class TotalPercent extends Fragment implements CriteriaDialog.EditNameDialogListener {
    private static final String DEFAULT = "75";
    TextView percent, class_no, criteria, attend1, total, desc;
    int attend, bunk, i;
    double p_criteria = 75;
    int classno, req_attend;
    int result;
    String percnt;
    Toolbar toolbar;
    Animation anim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.last_update, container, false);

        percent = (TextView) view.findViewById(R.id.percent);
        class_no = (TextView) view.findViewById(R.id.no_day);
        criteria = (TextView) view.findViewById(R.id.criteria);
        attend1 = (TextView) view.findViewById(R.id.attend);
        total = (TextView) view.findViewById(R.id.total);
        desc = (TextView) view.findViewById(R.id.desc);
        String[] projection = {"SUM(" + DBcontract.monDB.COLUMN_ATTEND + ")", "SUM(" + DBcontract.monDB.COLUMN_BUNK + ")"};

        Cursor cursor = getActivity().getContentResolver().query(DBcontract.monDB.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            attend = cursor.getInt(0);
            bunk = cursor.getInt(1);
        }
        SharedPreferences sf1 = getActivity().getSharedPreferences("criteria1", getActivity().MODE_PRIVATE);

        p_criteria = Double.parseDouble(sf1.getString("percent", DEFAULT));

        if (attend != 0 || bunk != 0) {
            result = getreqNoClass(attend, bunk, p_criteria);
            anim= AnimationUtils.loadAnimation(getActivity(),R.anim.scale);
            percnt= String.format("%.2f",((attend+0.0)/(attend+bunk))*100);

            if (result==0){
                class_no.setText("congratulations!!");
                desc.setText("you have reached your goal");
            }
            else if(result>0){
                if (((int) result)!=1){
                    desc.setText("classes are required to attend for");
                }else desc.setText("class is required to attend for");
                class_no.setText(result+"");
            }

        }else {
            percnt= String.format("%.0f",100.0);
        }

        percent.setText(percnt+"%");
        percent.setAnimation(anim);

        criteria.setText(String.format("%.0f ", p_criteria) + "%");
        attend1.setText(attend + "");
        total.setText(attend + bunk + "");
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getreqNoClass(int attend, int bunk, Double p_criteria) {

        int i = 0, rclassint = 0;
        double sub_class;
        double iattend = attend, itotal = (attend + bunk + 0.0);
        String str_sub;
        double cpercent, rclass;
        double iattend_holder, itotal_holder;

        cpercent = ((iattend + 0.0) / itotal) * 100;
        rclass = p_criteria * itotal;
        sub_class = rclass - iattend;
        iattend_holder = iattend;
        itotal_holder = itotal;

        if (cpercent < p_criteria) {
            while (i <= 1) {
                iattend_holder += (sub_class * 4);
                itotal_holder += (sub_class * 4);
                rclass = (itotal_holder * p_criteria) / 100;
                 sub_class = rclass - iattend_holder;
                str_sub = String.format("%.3f", sub_class);

                if (sub_class <= 1.0) {
                    i = 1;
                }
                if (str_sub.equals("0.000")) {
                    i = 2;
                    rclassint = (int) rclass;
                    Log.e("rclassint ", rclassint + "");
                    double fraction = rclass - rclassint;
                     if (fraction > 0.0000000) {
                        rclassint++;
                    }
                }
            }
            return rclassint - attend;
        }
     else {

            return 0;
        }
    }

    @Override
    public void onFinishEditDialog(String inputText, int pos) {

    }
}