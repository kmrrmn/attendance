package com.example.rmn.date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.rmn.date.data.DBcontract;

import java.util.ArrayList;

import static com.example.rmn.date.data.DBcontract.*;

public class DBHandler extends SQLiteOpenHelper {
    public int row_nos;
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "attendanceManager";
    SQLiteDatabase db;
    public final String table_main = "master";
    public final String sub = "sub";
    public final String day = "day";
    Context context;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String Create_sub_table = "CREATE TABLE " + SubTable.TABLE_NAME + "(" +

                SubTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SubTable.COLUMN_SUB + " TEXT NOT NULL) ;";

        String Create_table_main = "CREATE TABLE " + DBmaster.TABLE_NAME + "(" +

                DBmaster.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBmaster.SUB_ID + " INTEGER NOT NULL, " +
                DBmaster.COLUMN_DAY + " TEXT," +
                DBmaster.COLUMN_PERIOD + " INTEGER," +
                DBmaster.COLUMN_TIME + " TEXT," +
                DBmaster.AM_PM+ " TEXT," +
                DBmaster.COLUMN_IS_EXTRA + " TEXT," +

                "FOREIGN KEY (" + DBmaster.SUB_ID + ") REFERENCES " +
                SubTable.TABLE_NAME + "(" + SubTable.COLUMN_ID + ")" +

                "UNIQUE (" + DBmaster.COLUMN_DAY + "," + DBmaster.COLUMN_PERIOD +
                "," + DBmaster.COLUMN_TIME +") ON CONFLICT REPLACE" +
                ");";

        String Create_table_MON = "CREATE TABLE " + monDB.TABLE_NAME + "(" +
                monDB.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                monDB.SUB_ID + " TEXT NOT NULL," +
                monDB.MASTER_ID + " TEXT NOT NULL," +
                monDB.COLUMN_ATTEND + " INTEGER," +
                monDB.COLUMN_BUNK + " INTEGER," +
                monDB.COLUMN_DATE + " DATE," +
                monDB.COLUMN_IS_EXTRA + " TEXT," +

                "FOREIGN KEY (" + monDB.SUB_ID + ") REFERENCES " +
                SubTable.TABLE_NAME + "(" + SubTable.COLUMN_ID + ")" +

                "FOREIGN KEY (" + monDB.MASTER_ID + ") REFERENCES " +
                SubTable.TABLE_NAME + "(" + DBmaster.COLUMN_ID + ")" +
                ");";


        db.execSQL(Create_table_main);
        db.execSQL(Create_table_MON);
        db.execSQL(Create_sub_table);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBmaster.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + monDB.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SubTable.TABLE_NAME);
        }
        onCreate(db);
    }


}
