package com.nk.contentproviderhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDBHelpter extends SQLiteOpenHelper {

    public static String path = "/mnt/sdcard/userdbdemo.db";
    //创建stu表的语句
    public static final String CREATE_BOOK = "create table userdemo(" +
            "id integer primary key autoincrement," +
            "name text," +
            "sex text," +
            "age interger)";
    //构造方法
    private Context context;
    public MyDBHelpter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }
    //重写onCreate（）方法，执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        db.execSQL(CREATE_BOOK);
        Toast.makeText(context,"Create successful", Toast.LENGTH_SHORT).show();

    }
    //重写onUpgrade（）此方法在更新数据表用到，现在不用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}