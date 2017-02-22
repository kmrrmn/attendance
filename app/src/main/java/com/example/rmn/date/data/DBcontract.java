package com.example.rmn.date.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rmn on 27-04-2016.
 */
public class DBcontract {

    public static final String CONTENT_AUTHORITY = "com.example.rmn.date";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SUB = "sub";
    public static final String PATH_LOCATION = "master";
    public static final String PATH_MONDB = "monTable";
    public static final String PATH_SUB_MASTER = "subMaster";
    public static final String PATH_SUB_MON = "subMon";
    public static final String PATH_DETAIL = "detail";

    public static final class SubTable implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUB).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"
                + CONTENT_AUTHORITY + "/" + PATH_SUB;

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"
                + CONTENT_AUTHORITY + "/" + PATH_SUB;

        public static final String TABLE_NAME = "subTable";
        public static final String COLUMN_ID = "sub_id";
        public static final String COLUMN_SUB = "sub";


        public static Uri buildMasterUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }


        public static final class DBmaster implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"
                + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"
                + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String COLUMN_ID = "m_id";
        public static final String TABLE_NAME = "master";

        public static final String SUB_ID = "mas_sub_id";
        public static final String COLUMN_DAY="day";
        public static final String COLUMN_PERIOD="period";
            public static final String COLUMN_TIME="time";
            public static final String AM_PM="AM_PM";
            public static final String COLUMN_IS_EXTRA="isextraMaster";

        public static Uri buildMasterUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }
    }

    public static final class monDB implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MONDB).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"
                + CONTENT_AUTHORITY + "/" + PATH_MONDB;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"
                + CONTENT_AUTHORITY + "/" + PATH_MONDB;

        public static final String COLUMN_ID = "mon_id";
        public static final String SUB_ID = "mon_sub_id";
        public static final String MASTER_ID = "master_id";
        public static final String TABLE_NAME = "monTable";
        public static final String COLUMN_ATTEND = "attend";
        public static final String COLUMN_BUNK = "Bunk";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IS_EXTRA="isextraMon";


        public static Uri buildMasterUri(long _id) {
            return ContentUris.withAppendedId(CONTENT_URI, _id);
        }

        public static Uri buildMasterMonUri(String sub) {
            return CONTENT_URI.buildUpon().appendPath(sub).build();
        }

        public static Uri buildMasterDayUri(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id).build();
        }

        public static String getDayFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class AllDetail {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DETAIL).build();
    }


    public static final class SubMasterTable {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUB_MASTER).build();
    }

    public static final class SubMonTable {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUB_MON).build();

    }

}
