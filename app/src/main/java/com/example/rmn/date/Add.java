package com.example.rmn.date;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;

import java.util.Vector;


public class Add extends AppCompatActivity implements View.OnClickListener, AddDayDialog.SubDetailCallback {
    EditText sub;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Context context = this;
    SubDayDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sub_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbar);
        setTitle(null);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sub = (EditText) findViewById(R.id.editText);
        sub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSubDetails();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubDayDetailAdapter(this, null);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
    }

    public void addDay(View view) {
        AddDayDialog dialog = AddDayDialog.NewInstance(sub.getText().toString(), this);
        dialog.show(getFragmentManager(), "mgr");
    }

    @Override
    public void getSubDetails() {

        String selection = DBcontract.SubTable.COLUMN_SUB + "=?";
        String[] selectionArgs = {sub.getText().toString()};
        String orderBy = DBcontract.DBmaster.COLUMN_DAY;

        Cursor cursor = getContentResolver().query(DBcontract.SubMasterTable.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.e("SUB DETaIL ", cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_ID))
                        + "--" + cursor.getString(cursor.getColumnIndex(DBcontract.SubTable.COLUMN_SUB)) + "--" +
                        cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.SUB_ID)) + "--" +
                        cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_DAY)) + "--" +
                        cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_PERIOD)) + "--" +
                        cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_TIME)) + "--" +
                        cursor.getString(cursor.getColumnIndex(DBcontract.DBmaster.COLUMN_ID)) + "--"
                );
            }
        }

        adapter = new SubDayDetailAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
        Log.e("getSubDetails  ", "size " + cursor.getCount());
        Log.e("getSubDetails  ", "col count " + cursor.getColumnCount());
        adapter.SwapCursor(cursor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
