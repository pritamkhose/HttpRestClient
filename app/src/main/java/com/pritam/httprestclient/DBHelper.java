package com.pritam.httprestclient;

/**
 * Created by Pritam on 10/14/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    //private static final String LOG = DBHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;      // change version when added new method

    // Database Name
    private static final String DATABASE_NAME = "HttpRestDB";


    public DBHelper(final Context context) {
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public DBHelper(Context context) {
//
//        super(context, Environment.getExternalStorageDirectory() + File.separator + "NotesKeeper" +
//                File.separator + DATABASE_NAME, null, DATABASE_VERSION);
//
//
//        if (HomeActivity.ExternalCard)
//            SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator + "NotesKeeper" +
//                    File.separator + DATABASE_NAME, null);
//        else
//            SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
//    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables

        db.execSQL("create table HistoryTable(id text primary key, request_name text, create_time text, request_url text, " +
                " request_method text, headersList text, req_body text, req_body_type text, connectTimeout integer, writeTimeout integer, readTimeout integer)");

        //db.execSQL("create table CollectionTable(id text primary key, request_name text, create_time text, modify_time text, request_url text)"); // serialno integer

        db.execSQL("create table CollectionTable(id text primary key, request_name text, create_time text, modify_time text, request_url text, " +
                " request_method text, headersList text, req_body text, req_body_type text, connectTimeout integer, writeTimeout integer, readTimeout integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS HistoryTable");

        // create new tables
        onCreate(db);
    }

    // closing database
    @Override
    public synchronized void close() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            db.close();
            super.close();
        }
    }

    /**
     * get datetime
     */
    Date date = new Date();
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
        return dateFormat.format(date);
    }


    private String getID() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyMMddHHmmss", Locale.getDefault());
        return dateFormat.format(date);
    }



    /*********************************************************************/

    public void deleteHistory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (name != null && name.length() > 0) {
            db.delete("HistoryTable", " id = ?", new String[]{name});
        } else//delete all
        {
            db.execSQL(" delete from HistoryTable ");
        }
    }

    public long createHistory(HashMap<String,Object> dataal ,Boolean importdata) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ccnt = 0;
        try {
            String sql;
                if(importdata) {
                    sql = "Insert or Replace into HistoryTable (id , request_name , create_time , request_url, request_method , headersList , req_body , req_body_type , connectTimeout , writeTimeout , readTimeout ) values( '" +
                            dataal.get("id").toString() + "' , '" +
                            dataal.get("request_name").toString() + "' , '" +
                            dataal.get("create_time").toString() + "' , '" +
                            dataal.get("request_url").toString() + "' , '" +
                            dataal.get("request_method").toString() + "' , '" +
                            dataal.get("headersList").toString() + "' , '" +
                            dataal.get("req_body").toString() + "' , '" +
                            dataal.get("req_body_type").toString() + "' , " +
                            dataal.get("connectTimeout") + " , " +
                            dataal.get("writeTimeout") + " , " +
                            dataal.get("readTimeout") +
                            " )";

                } else {
                    sql = "Insert or Replace into HistoryTable (id , request_name , create_time , request_url, request_method , headersList , req_body , req_body_type , connectTimeout , writeTimeout , readTimeout ) values( '" +
                            getID() + "' , '" +
                            dataal.get("request_name").toString() + "' , '" +
                            getDateTime() + "' , '" +
                            dataal.get("request_url").toString() + "' , '" +
                            dataal.get("request_method").toString() + "' , '" +
                            dataal.get("headersList").toString() + "' , '" +
                            dataal.get("req_body").toString() + "' , '" +
                            dataal.get("req_body_type").toString() + "' , " +
                            dataal.get("connectTimeout") + " , " +
                            dataal.get("writeTimeout") + " , " +
                            dataal.get("readTimeout") +
                            " )";
                }

            System.out.println("---> "+ sql);
            db.execSQL(sql);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // db.endTransaction();
        }
        db.close();
        return ccnt;

    }

    public ArrayList<HashMap<String,Object>> getHistory(String query) {

        ArrayList<HashMap<String,Object>>  data = new ArrayList();
        String selectQuery = "SELECT  * FROM HistoryTable ";
        if (query != null && query.length() > 2)
            selectQuery +=  " where request_url like '%" + query + "%' ";

        selectQuery += "ORDER BY id DESC";

        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    HashMap<String, Object> HistoryTable = new HashMap<>();
                    HistoryTable.put("id", c.getString(c.getColumnIndex("id")));
                    HistoryTable.put("request_name", c.getString(c.getColumnIndex("request_name")));
                    HistoryTable.put("create_time", c.getString(c.getColumnIndex("create_time")));
                    HistoryTable.put("request_url", c.getString(c.getColumnIndex("request_url")));
                    HistoryTable.put("request_method", c.getString(c.getColumnIndex("request_method")));
                    HistoryTable.put("headersList", c.getString(c.getColumnIndex("headersList")));
                    HistoryTable.put("req_body", c.getString(c.getColumnIndex("req_body")));
                    HistoryTable.put("req_body_type", c.getString(c.getColumnIndex("req_body_type")));
                    HistoryTable.put("connectTimeout", c.getInt(c.getColumnIndex("connectTimeout")));
                    HistoryTable.put("writeTimeout", c.getInt(c.getColumnIndex("writeTimeout")));
                    HistoryTable.put("readTimeout", c.getInt(c.getColumnIndex("readTimeout")));

                    data.add(HistoryTable);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

   /* public long updateHistory(HashMap<String,Object> data) {
        SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            //id , request_name ,create_time,  modify_time , request_url, request_method , headersList , req_body , req_body_type , connectTimeout , writeTimeout , readTimeout
            values.put("request_name", data.get("request_name").toString());
            values.put("create_time", data.get("create_time").toString());
            values.put("request_url", data.get("request_url").toString());
            // update row
            db.update("HistoryTable", values, "id" + " = ?", new String[]{data.get("id").toString()});

            db.close();

        return 0;
    }

    public long updateAllHistory(ArrayList<HashMap<String,Object>> data) {
        SQLiteDatabase db = this.getWritableDatabase();

        long ccnt = 0;
            for (int i = 0; i < data.size(); i++) {
                ContentValues values = new ContentValues();
                //id , request_name ,create_time,  modify_time , request_url
                values.put("request_name", data.get(i).get("request_name").toString());
                values.put("request_name", data.get(i).get("request_name").toString());
                values.put("request_url", data.get(i).get("request_url").toString());
                // update row
                db.update("HistoryTable", values, "id" + " = ?", new String[]{data.get(i).get("id").toString()});
                ccnt++;
            }
            db.close();

        return ccnt;
    }*/

    /*********************************************************************/

    public void deleteCollection(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (name != null && name.length() > 0) {
            db.delete("CollectionTable", " id = ?", new String[]{name});
        } else//delete all
        {
            db.execSQL(" delete from CollectionTable ");
        }
    }

    public long createCollection(HashMap<String,Object> dataal ,Boolean importdata) { //ArrayList<HashMap<String,Object>> dataal
        SQLiteDatabase db = this.getWritableDatabase();
        long ccnt = 0;
        try {
            String sql;
            if(importdata) {
                sql = "Insert or Replace into CollectionTable (id , request_name , create_time , modify_time, request_url, request_method , headersList , req_body , req_body_type , connectTimeout , writeTimeout , readTimeout ) values( '" +
                        dataal.get("id").toString() + "' , '" +
                        dataal.get("request_name").toString() + "' , '" +
                        dataal.get("create_time").toString() + "' , '" +
                        getDateTime() + "' , '" +
                        dataal.get("request_url").toString() + "' , '" +
                        dataal.get("request_method").toString() + "' , '" +
                        dataal.get("headersList").toString() + "' , '" +
                        dataal.get("req_body").toString() + "' , '" +
                        dataal.get("req_body_type").toString() + "' , " +
                        dataal.get("connectTimeout") + " , " +
                        dataal.get("writeTimeout") + " , " +
                        dataal.get("readTimeout") +
                        " )";

            } else {
                sql = "Insert or Replace into CollectionTable (id , request_name , create_time , modify_time ,request_url, request_method , headersList , req_body , req_body_type , connectTimeout , writeTimeout , readTimeout ) values( '" +
                        getID() + "' , '" +
                        dataal.get("request_name").toString() + "' , '" +
                        getDateTime() + "' , '" +
                        getDateTime() + "' , '" +
                        dataal.get("request_url").toString() + "' , '" +
                        dataal.get("request_method").toString() + "' , '" +
                        dataal.get("headersList").toString() + "' , '" +
                        dataal.get("req_body").toString() + "' , '" +
                        dataal.get("req_body_type").toString() + "' , " +
                        dataal.get("connectTimeout") + " , " +
                        dataal.get("writeTimeout") + " , " +
                        dataal.get("readTimeout") +
                        " )";
            }

            System.out.println("---> "+ sql);
            db.execSQL(sql);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // db.endTransaction();
        }
        db.close();
        return ccnt;

    }

    public ArrayList<HashMap<String,Object>> getCollection(String query) {

        ArrayList<HashMap<String,Object>>  data = new ArrayList();
        String selectQuery = "SELECT  * FROM CollectionTable ";
        if (query != null && query.length() > 2)
            selectQuery = selectQuery + " where request_url like '" + query + "%' ";

        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {
                    HashMap<String, Object> CollectionTable = new HashMap<>();
                    CollectionTable.put("id", c.getString(c.getColumnIndex("id")));
                    CollectionTable.put("request_name", c.getString(c.getColumnIndex("request_name")));
                    CollectionTable.put("create_time", c.getString(c.getColumnIndex("create_time")));
                    CollectionTable.put("modify_time", c.getInt(c.getColumnIndex("modify_time")));
                    CollectionTable.put("request_url", c.getString(c.getColumnIndex("request_url")));
                    CollectionTable.put("request_method", c.getString(c.getColumnIndex("request_method")));
                    CollectionTable.put("headersList", c.getString(c.getColumnIndex("headersList")));
                    CollectionTable.put("req_body", c.getString(c.getColumnIndex("req_body")));
                    CollectionTable.put("req_body_type", c.getString(c.getColumnIndex("req_body_type")));
                    CollectionTable.put("connectTimeout", c.getInt(c.getColumnIndex("connectTimeout")));
                    CollectionTable.put("writeTimeout", c.getInt(c.getColumnIndex("writeTimeout")));
                    CollectionTable.put("readTimeout", c.getInt(c.getColumnIndex("readTimeout")));

                    data.add(CollectionTable);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            //System.out.println("ERROR:::"+e);
        }

        return data;
    }

    public long updateCollection(HashMap<String,Object> data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues CollectionTable = new ContentValues();
        //id , request_name ,create_time,  modify_time , request_url
        CollectionTable.put("request_name", data.get("request_name").toString());
        CollectionTable.put("create_time", data.get("create_time").toString());
        CollectionTable.put("modify_time", getDateTime());
        CollectionTable.put("request_url", data.get("request_url").toString());
        CollectionTable.put("request_method", data.get("request_method").toString());
        CollectionTable.put("headersList", data.get("headersList").toString());
        CollectionTable.put("req_body", data.get("req_body").toString());
        CollectionTable.put("req_body_type", data.get("req_body_type").toString());
        CollectionTable.put("connectTimeout", data.get("connectTimeout").toString());
        CollectionTable.put("writeTimeout", data.get("writeTimeout").toString());
        CollectionTable.put("readTimeout", data.get("readTimeout").toString());

        // update row
        db.update("CollectionTable", CollectionTable, "id" + " = ?", new String[]{data.get("id").toString()});

        db.close();

        return 0;
    }

    public long updateAllCollection(ArrayList<HashMap<String,Object>> data) {
        SQLiteDatabase db = this.getWritableDatabase();

        long ccnt = 0;
        for (int i = 0; i < data.size(); i++) {
            ContentValues CollectionTable = new ContentValues();
            //id , request_name ,create_time,  modify_time , request_url
            CollectionTable.put("request_name", data.get(i).get("request_name").toString());
            CollectionTable.put("create_time", data.get(i).get("create_time").toString());
            CollectionTable.put("modify_time", getDateTime());
            CollectionTable.put("request_url", data.get(i).get("request_url").toString());
            CollectionTable.put("request_method", data.get(i).get("request_method").toString());
            CollectionTable.put("headersList", data.get(i).get("headersList").toString());
            CollectionTable.put("req_body", data.get(i).get("req_body").toString());
            CollectionTable.put("req_body_type", data.get(i).get("req_body_type").toString());
            CollectionTable.put("connectTimeout", data.get(i).get("connectTimeout").toString());
            CollectionTable.put("writeTimeout", data.get(i).get("writeTimeout").toString());
            CollectionTable.put("readTimeout", data.get(i).get("readTimeout").toString());

            // update row
            db.update("CollectionTable", CollectionTable, "id" + " = ?", new String[]{data.get(i).get("id").toString()});
            ccnt++;
        }
        db.close();

        return ccnt;
    }

    /*********************************************************************/
}
