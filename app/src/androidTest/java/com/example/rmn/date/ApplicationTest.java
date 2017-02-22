package com.example.rmn.date;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;

import com.example.rmn.date.data.DBcontract;
import com.example.rmn.date.data.DBcontract.DBmaster;

import java.sql.Date;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testCreateDB() throws Throwable{

        mContext.deleteDatabase(DBHandler.DB_NAME);

        SQLiteDatabase db=new DBHandler(this.mContext).getReadableDatabase();

        assertEquals(true,db.isOpen());
        db.close();
    }

    public void testInsertDB() throws Throwable{

        SQLiteDatabase db=new DBHandler(this.mContext).getReadableDatabase();


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

         rowId=db.insert(DBcontract.sunDB.TABLE_NAME,null,contentValues);
        rowId1=db.insert(DBcontract.tueDB.TABLE_NAME,null,contentValues);
        rowId2=db.insert(DBcontract.monDB.TABLE_NAME,null,contentValues);

        assertTrue(0<rowId);
        assertTrue(0<rowId1);
        assertTrue(0<rowId2);

        Cursor cursor =   db.query(DBcontract.sunDB.TABLE_NAME,null,null,null,null,null,null);
 if (cursor.moveToFirst()){

 }else {
     fail("NO DATA ");
 }
     }

    public void testUpdate() throws Throwable{

        int i=0,rowUpdated;
        SQLiteDatabase db=new DBHandler(this.mContext).getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBcontract.sunDB.COLUMN_SUB,"qwerty");
        contentValues.put(DBcontract.sunDB.COLUMN_ATTEND,3);
        contentValues.put(DBcontract.sunDB.COLUMN_BUNK,2);
        contentValues.put(DBcontract.sunDB.COLUMN_DATE,0);

        String selection=DBcontract.sunDB.COLUMN_SUB +"= ? AND " +
                DBcontract.sunDB.COLUMN_DATE + "=?";

        String[] selectionArgs={"math",Integer.toString(i)};

         rowUpdated=db.update(DBcontract.sunDB.TABLE_NAME,contentValues,selection,selectionArgs);
    assertTrue(rowUpdated>0);
    }

    public void testDelete() throws Throwable{
        SQLiteDatabase db=new DBHandler(this.mContext).getReadableDatabase();

          db.delete(DBcontract.sunDB.TABLE_NAME,null,null);
    }
}