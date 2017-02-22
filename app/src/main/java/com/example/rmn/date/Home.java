package com.example.rmn.date;


import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.method.CharacterPickerDialog;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmn.date.Main2Activity;
import com.example.rmn.date.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.rmn.date.data.DBcontract;
import com.example.rmn.date.utiles.Constants;
import com.example.rmn.date.utiles.Receiver;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;


public class Home extends Fragment implements View.OnClickListener, CalenderDialog.CallbackForAttendance {

    public static final String extra_Year = "year", extra_date = "date", extra_Month = "month", extra_Day = "day", control = "con";
    RadarChart mChart;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    FloatingActionButton fab,
      subAdd, getAttend;
    FrameLayout selectView;
    TextView totalPercentView;
    List<String> subs;
    FrameLayout homeView;
    List<Double> percents;
    int visibilityToken = 0;
    int totalAttend = 0, totalBunk = 0;
    double totalPercent;
    RelativeLayout noSubView;
    String[] projection =
            {DBcontract.SubTable.COLUMN_ID,
                    "SUM(" + DBcontract.monDB.COLUMN_ATTEND + ")",
                    "SUM(" + DBcontract.monDB.COLUMN_BUNK + ")",
                    DBcontract.SubTable.COLUMN_SUB};

    Animation animation;
    int i = 0, attend = 0, bunk = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        View view = inflater.inflate(R.layout.home, container, false);
        homeView = (FrameLayout) view.findViewById(R.id.home);
        noSubView=(RelativeLayout)view.findViewById(R.id.nosub);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        subAdd = (FloatingActionButton) view.findViewById(R.id.add_sub);
        getAttend = (FloatingActionButton) view.findViewById(R.id.attend);
        selectView = (FrameLayout) view.findViewById(R.id.action);
        totalPercentView = (TextView) view.findViewById(R.id.percent);
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("onTouch",")))");
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (visibilityToken == 0) {
                        visibilityToken = 1;
                        Log.e("onTouch","))--->)");
                        selectView.setVisibility(View.VISIBLE);

                        animateRevealColorFromCoordinates(selectView, R.color.bunkColor, (int) event.getRawX(), (int) event.getRawY());

                    } else {
                        Log.e("onTouch",")-------->))");
                        visibilityToken = 0;
                        animateRevealColorFromCoordinates(homeView, R.color.attendColor, (int) event.getRawX(), (int) event.getRawY());

                    }
                }
                return false;
            }
        });
        subAdd.setOnClickListener(this);
        getAttend.setOnClickListener(this);

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        mChart = (RadarChart) view.findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDescription("");
        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.BLACK);
        mChart.setWebAlpha(150);

        MarkerView view1 = new RadarMarkerView(getActivity(), R.layout.marker);
        mChart.setMarkerView(view1);

        mChart.animateXY(1000, 1000,
                Easing.EasingOption.EaseOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);

        xAxis.setValueFormatter(new XAxisValueFormatter() {

            @Override
            public String getXValue(String s, int i, ViewPortHandler viewPortHandler) {
                return subs.get((int) i % subs.size());
            }
        });

        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinValue(0f);
        yAxis.setAxisMaxValue(100f);
        yAxis.setDrawLabels(false);

        return view;
    }


    public void setData() {

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<Entry> entries2 = new ArrayList<Entry>();

        for (int i = 0; i < subs.size(); i++) {

            float f = Float.parseFloat(String.format("%.2f", percents.get(i)));
            Entry entry = new Entry(f, i);
            entries1.add(entry);

        }

        RadarDataSet set1 = new RadarDataSet(entries1, "");
        set1.setColor(Color.rgb(0, 0, 0));
        set1.setFillColor(Color.rgb(0, 0, 0));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);


        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(subs, sets);
        data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_sub:
                visibilityToken=0;
                Intent intent = new Intent(getActivity(), Add.class);
                selectView.setVisibility(View.GONE);
                startActivity(intent);
                break;
            case R.id.attend:
                visibilityToken=0;
                CalenderDialog dialog = CalenderDialog.newInstance(getActivity(), this);
                dialog.show(getActivity().getFragmentManager(), "calenderView");
                selectView.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        i = 0;
        setUpWindowAnimation();
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in);
        totalPercentView.setAnimation(animation);

        subs = new ArrayList<>();
        percents = new ArrayList<>();
        subs.clear();
        percents.clear();
        Cursor cursor = getActivity()
                .getContentResolver()
                .query(DBcontract.SubMonTable.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {

                    homeView.setVisibility(View.VISIBLE);
                    noSubView.setVisibility(View.GONE);
                    attend = cursor.getInt(1);
                    bunk = cursor.getInt(2);
                    totalAttend += attend;
                    totalBunk += bunk;
                    if ((attend + bunk) != 0) {
                        double percent = ((attend + 0.0) / (attend + bunk)) * 100;
                        percents.add(percent);
                        String sub = cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB));
                        String[] subSplit = sub.split(" ");
                        Log.e("SPLIT SUB ", subSplit.length + "");
                        subs.add(subSplit[0]);
                        i++;
                    } else {
                        percents.add(100.0);
                        subs.add(cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB)));
                    }
                }

                totalPercent = (((totalAttend + 0.0) / (totalBunk + totalAttend)) * 100);
                String percentString = String.format("%.2f", totalPercent);
                totalPercentView.setText(percentString + "%");
            } else {
                homeView.setVisibility(View.GONE);
                noSubView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "empty", Toast.LENGTH_LONG).show();}
        }
        setData();
    }

    @Override
    public void startActivity(String date, int year, int month, String weekDay) {
        Intent intent = new Intent(getActivity(), Main2Activity.class);
        intent.putExtra(control, 1);
        intent.putExtra(Constants.EXTRA_YEAR, year);
        intent.putExtra(Constants.EXTRA_DATE, date);
        intent.putExtra(Constants.EXTRA_MONTH, month);
        intent.putExtra(Constants.EXTRA_DAY, weekDay);
        Log.e("home weekday  ", "--->" + weekDay);
        Log.e("home date  ", "--->" + date);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public class MyValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
            return null;
        }
    }


    private void setUpWindowAnimation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.RIGHT);
            slide.setDuration(500);
            slide.setStartDelay(200);
            getActivity().getWindow().setReenterTransition(slide);
            getActivity().getWindow().setExitTransition(slide);
        }
    }

    public Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, @ColorRes int color, int x, int y) {
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
            viewRoot.setBackgroundColor(ContextCompat.getColor(getActivity(), color));
            anim.setDuration(500);

            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();

            if (viewRoot.getId()==R.id.home){
                selectView.setVisibility(View.GONE);
            }

            return anim;
        }

        return null;
    }
}
