package com.dailytask.manager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperComTask extends SQLiteOpenHelper {
    private static final String DB_NAME = "completedtask_database";
    public static final String DB_TABLE = "completedTask_table";

    //columns
    private static final String ID = "ID";
    private static final String TASKNAME = "ITEM1";
    private static final String TASKTIME = "ITEM2";

    //public static final

    public DatabaseHelperComTask(Context context) {
        super (context,DB_NAME,null,15);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASKNAME +" TEXT,"
                +TASKTIME  +" TEXT )";

        db.execSQL (CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS "+ DB_TABLE);

        onCreate (db);//creates table again

    }
    //create method to insert completed task into the db
    public boolean insertData(CompletedTaskClass completedTaskClass){
        SQLiteDatabase db=this.getWritableDatabase ();
        ContentValues contentValues=new ContentValues ();
        contentValues.put (TASKNAME,completedTaskClass.getTitle ());
        contentValues.put (TASKTIME,completedTaskClass.getTime ());
      //  contentValues.put (ID,completedTaskClass.getId ());

        long result=db.insert (DB_TABLE,null,contentValues);
        return result != -1;
    }
    //create method to display data
    public Cursor ViewData(){
        SQLiteDatabase db=this.getReadableDatabase ();
        String query="SELECT *FROM "+DB_TABLE;
        Cursor cursor=db.rawQuery (query,null);

        return cursor;
    }
    //method that deletes specific data
    public void deleteData(String  name){
        SQLiteDatabase db=this.getWritableDatabase ();
       db.execSQL ("DELETE FROM "+DB_TABLE +" WHERE "+TASKNAME +"='"+name+"'");
        db.close ();
    }
        //method that deletes all data
    public void deleteAllData(){
        SQLiteDatabase db=this.getWritableDatabase ();
        db.execSQL ("DELETE FROM " +DB_TABLE);
        db.close ();
    }

}
