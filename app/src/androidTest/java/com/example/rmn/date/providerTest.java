package com.example.rmn.date;

import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ApplicationTestCase;
import com.example.rmn.date.data.DBcontract;

import static com.example.rmn.date.data.DBcontract.*;

/**
 * Created by rmn on 28-04-2016.
 */
public class providerTest extends ApplicationTestCase<Application> {
    public providerTest() {
        super(Application.class);
    }

//    public void testCreateDB() throws Throwable{
//
//        mContext.deleteDatabase(DBHandler.DB_NAME);
//
//        SQLiteDatabase db=new DBHandler(this.mContext).getReadableDatabase();
//
//        assertEquals(true,db.isOpen());
//        db.close();
//    }

    public void testInsertDB() throws Throwable{

        ContentValues contentValues=new ContentValues();
        contentValues.put(DBcontract.sunDB.COLUMN_SUB,"math");
        contentValues.put(DBcontract.sunDB.COLUMN_ATTEND,5);
        contentValues.put(DBcontract.sunDB.COLUMN_BUNK,5);
        contentValues.put(DBcontract.sunDB.COLUMN_DATE,0);

        ContentValues contentValues1=new ContentValues();
        contentValues.put(DBcontract.tueDB.COLUMN_SUB,"math");
        contentValues.put(DBcontract.tueDB.COLUMN_ATTEND,5);
        contentValues.put(DBcontract.tueDB.COLUMN_BUNK,5);
        contentValues.put(DBcontract.tueDB.COLUMN_DATE,0);

        ContentValues contentValues2=new ContentValues();
        contentValues.put(DBcontract.monDB.COLUMN_SUB,"math");
        contentValues.put(DBcontract.monDB.COLUMN_ATTEND,5);
        contentValues.put(DBcontract.monDB.COLUMN_BUNK,5);
        contentValues.put(DBcontract.monDB.COLUMN_DATE,0);

        long rowId,rowId1,rowId2;
        Uri rowUri,rowUri1,rowUri2;

        rowUri=mContext.getContentResolver().insert(DBcontract.sunDB.CONTENT_URI,contentValues);
        rowId= ContentUris.parseId(rowUri);
        assertTrue(0<rowId);

        Cursor cursor=mContext.getContentResolver().query(DBcontract.sunDB.CONTENT_URI,
         null,null,null,null);
         assertTrue(cursor.getCount()>0);
        if (cursor.moveToFirst()){
cursor.close();
        }else {
            fail("NO DATA ");
        }
    }

//    public void testDeleteRecord() throws Throwable{
//
//        mContext.getContentResolver().delete(DBcontract.DBmaster.CONTENT_URI,
//                null,null);
//        Cursor cursor=mContext.getContentResolver().query(DBcontract.DBmaster.CONTENT_URI,
//                null,null,null,null);
//        assertEquals(cursor.getCount(),0);
// cursor.close();
//    }

    public void testUpdateRow() throws Throwable{

        int i=0,rowUpdated;

         ContentValues contentValues=new ContentValues();
        contentValues.put(DBcontract.sunDB.COLUMN_SUB,"qwerty");
        contentValues.put(DBcontract.sunDB.COLUMN_ATTEND,3);
        contentValues.put(DBcontract.sunDB.COLUMN_BUNK,2);
        contentValues.put(DBcontract.sunDB.COLUMN_DATE,0);

        String selection=DBcontract.sunDB.COLUMN_SUB +"= ? ";

        String[] selectionArgs={"math"};

        rowUpdated=mContext.getContentResolver().update(DBcontract.sunDB.CONTENT_URI,contentValues,selection,selectionArgs);
        assertTrue(rowUpdated>0);

        Cursor cursor=mContext.getContentResolver().query(DBmaster.CONTENT_URI,
                null,null,null,null);
    }

    public void testDelete() throws Throwable {
        mContext.getContentResolver();
    }

    }
