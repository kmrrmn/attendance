package com.example.rmn.date.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.media.UnsupportedSchemeException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rmn.date.DBHandler;

import static com.example.rmn.date.data.DBcontract.*;

/**
 * Created by rmn on 28-04-2016.
 */
public class dataProvider extends ContentProvider {

    public SQLiteDatabase dbhandler;
    public DBHandler dbHandler;
    private static final int MASTER = 100;
    private static final int SUB = 101;
    private static final int PERIOD = 102;
    private static final int TIME = 103;
    private static final int MON = 104;
    private static final int SUB_MON=105;
    private static final int SUB_MASTER=106;
    private static final int SUB_MASTER_MON = 108;
    private static final int DAY_MASTER = 109;
    private static final int SUB_DETAIL = 110;


    private static SQLiteQueryBuilder sub_master_mon_table_builder;
    private static SQLiteQueryBuilder sub_mon_table_builder;
    private static SQLiteQueryBuilder sub_master_table_builder;
    public static UriMatcher uriMatcher = buildUriMatcher();


    static {

        sub_mon_table_builder = new SQLiteQueryBuilder();
        sub_mon_table_builder.setTables(SubTable.TABLE_NAME + " LEFT JOIN "
                + monDB.TABLE_NAME +
                " ON " +
                SubTable.TABLE_NAME+"."+SubTable.COLUMN_ID + " = " +
                monDB.TABLE_NAME+"."+ monDB.SUB_ID);

        sub_master_table_builder=new SQLiteQueryBuilder();
        sub_master_table_builder.setTables(SubTable.TABLE_NAME + " LEFT JOIN " +
                 DBmaster.TABLE_NAME +
                  " ON " +
                    SubTable.COLUMN_ID + " = "
                + DBmaster.SUB_ID );

        sub_master_mon_table_builder=new SQLiteQueryBuilder();
        sub_master_mon_table_builder.setTables(SubTable.TABLE_NAME + " LEFT JOIN " +
                DBmaster.TABLE_NAME +
                " ON " +
                SubTable.COLUMN_ID + " = "
                + DBmaster.SUB_ID  +
                 " LEFT JOIN " + monDB.TABLE_NAME
                 + " ON "
                +  DBmaster.COLUMN_ID + " = " +
                   monDB.MASTER_ID);
    }


    private Cursor getSubByDay(Uri uri, String[] projection, String selection, String[] args, String sortorder) {

        return sub_mon_table_builder.query(
                dbhandler,
                projection,
                selection,
                args, null, null, null

        );
    }


    public static UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_LOCATION, MASTER);
        matcher.addURI(authority, PATH_MONDB, MON);
        matcher.addURI(authority, PATH_SUB, SUB);

        matcher.addURI(authority, PATH_SUB_MASTER, SUB_MASTER);
        matcher.addURI(authority, PATH_SUB_MON, SUB_MON);
        matcher.addURI(authority, PATH_DETAIL, SUB_DETAIL);
        matcher.addURI(authority, PATH_MONDB + "/*", DAY_MASTER);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(
            Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        int match = uriMatcher.match(uri);
        dbhandler = new DBHandler(getContext()).getReadableDatabase();

        switch (match) {

            case SUB_DETAIL:
                Log.e("SUB_DETAIL ","called");
                Log.e("SUB_DETAIL ",sub_master_mon_table_builder.toString());

                cursor = sub_master_mon_table_builder.query(
                        dbhandler,
                        projection,
                        selection,
                        selectionArgs,
                        SubTable.COLUMN_ID,
                        null,
                        null);
                break;


            case SUB_MASTER:





                cursor = sub_master_table_builder.query(
                        dbhandler,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;


            case SUB_MON:
                Log.e("SUBMON","CALLED");
                cursor = sub_mon_table_builder.query(
                        dbhandler,
                        projection,
                        selection,
                        selectionArgs,
                        SubTable.COLUMN_ID,
                        null,
                        null);
                break;



            case MASTER:
                Log.e("master query provider" ,"   called");
                cursor = dbhandler.query(
                        DBmaster.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder
                );
                break;


            case MON:
                cursor = dbhandler.query(
                        DBcontract.monDB.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder
                );
                break;

            case SUB:
                Log.e("Sub query provider" ,"   called");
                cursor = dbhandler.query(
                        DBcontract.SubTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null, null,
                        sortOrder
                );

                break;

            default:
                throw new UnsupportedOperationException("query  UNKNOWN URI:------>" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);
        if (match == MASTER) {
            return DBmaster.CONTENT_ITEM_TYPE;
        } else
            return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        dbhandler = new DBHandler(getContext()).getReadableDatabase();
        Uri returnuri;
        long _id;
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MASTER:
                _id = dbhandler.insert(DBmaster.TABLE_NAME, null, values);
                break;

            case MON:
                _id = dbhandler.insert(monDB.TABLE_NAME, null, values);
                break;

            case SUB:
                _id = dbhandler.insert(SubTable.TABLE_NAME, null, values);
                break;

            case SUB_MASTER:
                _id = dbhandler.insert(SubTable.TABLE_NAME, null, values);
                break;

            default:
                throw new UnsupportedOperationException("insert unkown uri " + uri);
        }
        if (_id > 0) {
            returnuri = DBmaster.buildMasterUri(_id);
        } else throw new android.database.SQLException("failed to insert in " + uri);

        return returnuri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        int rowDeleted;
        dbhandler = new DBHandler(getContext()).getReadableDatabase();

        switch (match) {
            case MASTER:
                rowDeleted = dbhandler.delete(DBmaster.TABLE_NAME, selection, selectionArgs);
                break;

            case MON:
                rowDeleted = dbhandler.delete(DBcontract.monDB.TABLE_NAME, selection, selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("unkown uri " + uri);
        }
        if (null == selection || 0 != rowDeleted) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        dbhandler = new DBHandler(getContext()).getReadableDatabase();
        int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {

            case MASTER:
                rowsUpdated = dbhandler.update(DBmaster.TABLE_NAME, values, selection, selectionArgs);
                break;

            case MON:
                rowsUpdated = dbhandler.update(monDB.TABLE_NAME, values, selection, selectionArgs);

                Log.e("mon ","selecttion /"+selection);
                Log.e("mon ","setArgs0 /"+selectionArgs[0]);
                Log.e("mon ","setArgs1 /"+selectionArgs[1]);

                Log.e("mon ","cv  /"+values.toString());
                Log.e("mon ","rowupdate /"+rowsUpdated);
                break;

            case SUB:
                rowsUpdated=dbhandler.update(SubTable.TABLE_NAME,values,selection,selectionArgs);
                        break;
            default:
                throw new UnsupportedOperationException("unkown uri " + uri);
        }
        if (rowsUpdated > 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
