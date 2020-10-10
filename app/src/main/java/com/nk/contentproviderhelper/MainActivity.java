package com.nk.contentproviderhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = MainActivity.class.getSimpleName();
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    EditText editText;
    Button insert, delete, update, query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        init();
    }

    private void init(){
        editText = findViewById(R.id.edit);
        insert = findViewById(R.id.insert);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        query = findViewById(R.id.query);
        insert.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.insert:
                String name = editText.getText().toString();
                //创建期待匹配的uri
                Uri uri1 = Uri.parse("content://com.nk.contentproviderhelper.databasetest.provider/userdemo");
                ContentValues values = new ContentValues();
                values.put("name",name);
                //获得ContentResolver对象，调用方法
                getContentResolver().insert(uri1,values);
                break;
            case R.id.delete:
                String name1 = editText.getText().toString();
                Uri uri2 = Uri.parse("content://com.nk.contentproviderhelper.databasetest.provider/userdemo");
                getContentResolver().delete(uri2,"name=?",new String[]{name1});
                break;
            case R.id.update:
                String updateStr = editText.getText().toString();
                Uri uri3 =Uri.parse("content://com.nk.contentproviderhelper.databasetest.provider/userdemo");
                ContentValues values1 = new ContentValues();
                values1.put("name",updateStr);
                getContentResolver().update(uri3,values1,"name=?",new String[]{""});
                break;
            case R.id.query:
                Uri uri = Uri.parse("content://com.nk.contentproviderhelper.databasetest.provider/userdemo");
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                cursor.moveToFirst();
                do{
                    String name2 = cursor.getString(cursor.getColumnIndex("name"));
                    Log.e(TAG, "onClick: "+name2 );
                }while(cursor.moveToNext());
                cursor.close();
                break;
            default:
                break;
        }
    }
}
