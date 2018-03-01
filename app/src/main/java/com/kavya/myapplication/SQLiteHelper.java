package com.kavya.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "userdatabase.db";
    public static final int DBVERSION = 1;

    public static final String DBTABLENAME = "userinfo";

    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_AGE = "AGE";
    public static final String KEY_ADDRESS = "ADDRESS";
    public static final String KEY_SALARY = "SALRY";

    SQLiteDatabase bdDatabase;



    public SQLiteHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String creatQuery = "CREATE TABLE "+DBTABLENAME+
                "("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                KEY_NAME+" TEXT,"+KEY_AGE+" INT,"+KEY_ADDRESS+" CHAR(20),"+
                KEY_SALARY+" REAL);";
        sqLiteDatabase.execSQL(creatQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVsersion, int newVersion) {
            String dropQuery = "DROP TABLE IF EXISTS "+DBTABLENAME;
        sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL(dropQuery);
            onCreate(sqLiteDatabase);
    }

    public void addUserInfo(UserInfo info){
        bdDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(KEY_NAME,info.getName());
        values.put(KEY_AGE,info.getAge());
        values.put(KEY_ADDRESS,info.getAddress());
        values.put(KEY_SALARY,info.getSalary());
        bdDatabase.insert(DBTABLENAME,null,values);
        bdDatabase.close();
    }

    public UserInfo getOneUserInfo(int id){
        bdDatabase = this.getReadableDatabase();
        Cursor cursor = bdDatabase.query(DBTABLENAME,new String[]{KEY_ID,KEY_NAME,KEY_AGE,KEY_ADDRESS,KEY_SALARY},KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        UserInfo info = new UserInfo(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Integer.parseInt(cursor.getString(2)),
                cursor.getString(3),Float.parseFloat(cursor.getString(4)));
        return info;
    }

    public List<UserInfo> getAllUserInfo(){
        List<UserInfo> list = new ArrayList<UserInfo>();
        bdDatabase = this.getWritableDatabase();
        String selectQuer =  "SELECT * FROM "+DBTABLENAME;
        Cursor cursor = bdDatabase.rawQuery(selectQuer,null);
        if(cursor.moveToFirst()){
            do {
                UserInfo userInfo =  new UserInfo();
                userInfo.setId(Integer.parseInt(cursor.getString(0)));
                userInfo.setName(cursor.getString(1));
                userInfo.setAge(Integer.parseInt(cursor.getString(2)));
                userInfo.setAddress(cursor.getString(3));
                userInfo.setSalary(Float.parseFloat(cursor.getString(4)));
                list.add(userInfo);
            }while (cursor.moveToNext());
        }

        return list;
    }

    public int updateUserInfo(UserInfo info){
        bdDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(KEY_NAME,info.getName());
        values.put(KEY_AGE,info.getAge());
        values.put(KEY_ADDRESS,info.getAddress());
        values.put(KEY_SALARY,info.getSalary());

        return bdDatabase.update(DBTABLENAME,values,KEY_ID+"=?",new String[]{String.valueOf(info.getId())});
    }

    public void deleteOneUserInfo(UserInfo info){
        bdDatabase = this.getWritableDatabase();
        bdDatabase.delete(DBTABLENAME,KEY_ID+"=?",new String[]{String.valueOf(info.getId())});
        bdDatabase.close();
    }

    public void deleteAllDataUserInfo(){
        bdDatabase = this.getWritableDatabase();
        bdDatabase.delete(DBTABLENAME,null,null);
        bdDatabase.close();
    }
}
