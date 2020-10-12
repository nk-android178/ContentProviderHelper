package com.nk.contentproviderhelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class DatabaseProvider extends ContentProvider {

    private MyDBHelpter myDBHelpter;
    private static final String TAG = "DatabaseProvider";

    //添加整形常亮
    public static final int USER_DIR = 0;
    public static final int USER_ITEM = 1;

    //创建authority
    public static final String AUTHORITY = "com.nk.contentproviderhelper.databasetest.provider";
    //创建UriMatcher对象
    private static UriMatcher uriMatcher;

    //创建静态代码块
    static {
        //实例化UriMatcher对象
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //可以实现匹配URI的功能
        //参数1：authority 参数2：路径 参数3：自定义代码
        uriMatcher.addURI(AUTHORITY, "userdemo", USER_DIR);
        uriMatcher.addURI(AUTHORITY, "userdemo/#", USER_ITEM);
    }

    public DatabaseProvider() {
        Log.e(TAG, "DatabaseProvider: ");
    }

    //onCreate()方法
    @Override
    public boolean onCreate() {
        //实现创建MyDBHelpter对象"userdbdemo.db" MyDBHelpter.path
        //运行程序的时候会自动创建数据库，创建表
        String DBname = Environment.getExternalStorageDirectory().getPath()+"/userdbdemo.db"; //数据库路径及名称
        Log.d(TAG,"DBname="+DBname);
        myDBHelpter = new MyDBHelpter(getContext(),DBname , null, 1);
        return true;
    }

    //删除数据表数据
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //创建SQLiteDatabase对象
        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        int deleteInt = 0;
        //匹配uri
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                //参数1：表名   参数2：约束删除列的名字   参数3：具体行的值
                deleteInt = db.delete("userdemo", selection, selectionArgs);
                break;
            case USER_ITEM:
                String deleteId = uri.getPathSegments().get(1);
                deleteInt = db.delete("userdemo", "id=?", new String[]{deleteId});
                break;
            default:
        }

        return deleteInt;
    }

    //插入数据
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
            case USER_ITEM:
                //参数1：表名  参数2：没有赋值的设为空   参数3：插入值
                long newUserId = db.insert("userdemo", null, values);
                break;
            default:
                break;
        }
        return null;
    }

    //查询数据
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                //参数1：表名  其他参数可借鉴上面的介绍
                cursor = db.query("userdemo", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_ITEM:
                String queryId = uri.getPathSegments().get(1);
                cursor = db.query("userdemo", projection, "id=?", new String[]{queryId}, null, null, sortOrder);
                break;
            default:
        }
        return cursor;

    }

    //更新数据
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db = myDBHelpter.getWritableDatabase();
        int updateRow = 0;
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                updateRow = db.update("userdemo",values,selection,selectionArgs);
                break;
            case USER_ITEM:
                String updateId = uri.getPathSegments().get(1);
                updateRow = db.update("userdemo",values,"id=?",new String[]{updateId});

                break;
            default:
        }
        return updateRow;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}