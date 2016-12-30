package com.huayinghuang.sqlitetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String DB_FILE = "people.db";

    private SQLiteDatabase mPeopleDb;
    private EditText mName, mNumber;
    private TextView mList;
    private String mTableName = MyDBOpenHelper.TABLE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDBOpenHelper myDBOpenHelper = new MyDBOpenHelper(this, DB_FILE, null, 1);
        mPeopleDb = myDBOpenHelper.getWritableDatabase();

        initView();
    }

    private void initView() {
        mName = (EditText)findViewById(R.id.etName);
        mNumber = (EditText)findViewById(R.id.etNumber);
        mList = (TextView)findViewById(R.id.tvList);

        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        findViewById(R.id.btnList).setOnClickListener(this);
        findViewById(R.id.btnDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Cursor cursor = null;
        String name = mName.getText().toString();
        String number = mNumber.getText().toString();

        switch (v.getId()) {
            case R.id.btnAdd:
                if (name.length() == 0 || number.length() == 0) {
                    showToast(getString(R.string.please_fill));
                } else {
                    addData(name, number);
                    initColumn();
                    showToast(getString(R.string.add_success));
                }

                break;
            case R.id.btnSearch:
                if (!name.equals("")) {
                    cursor = searchData("name", name);
                } else if (!number.equals("")) {
                    cursor = searchData("number", number);
                } else {
                    showToast(getString(R.string.please_fill));
                }

                if (cursor == null)
                    return;

                if (cursor.getCount() == 0) {
                    mList.setText("");
                    showToast(getString(R.string.cant_find));
                } else {
                    cursor.moveToFirst();
                    mList.setText(cursor.getString(0) +"\t"+ cursor.getString(1));

                    while (cursor.moveToNext()) {
                        mList.append("\n" + cursor.getString(0) +"\t"+ cursor.getString(1));
                    }
                }

                cursor.close();
                break;
            case R.id.btnList:
                cursor = listAllData();
                int row = cursor.getCount();
                int column = cursor.getColumnCount();

                if (cursor == null)
                    return;

                if (cursor.getCount() == 0) {
                    mList.setText("");
                    showToast(getString(R.string.no_data));
                } else {
                    cursor.moveToFirst();
                    mList.setText("");

                    for (int i=0; i<row; i++) {
                        for (int j=0; j<column; j++) {
                            mList.append(cursor.getString(j)+"\t");
                        }

                        mList.append("\n");
                        cursor.moveToNext();
                    }

                }

                cursor.close();
                break;
            case R.id.btnDelete:
                if (!name.equals("")) {
                    cursor = searchData("name", name);
                } else if (!number.equals("")) {
                    cursor = searchData("number", number);
                } else {
                    showToast(getString(R.string.please_fill));
                }

                if (cursor == null)
                    return;

                if (cursor.getCount() == 0) {
                    mList.setText("");
                    showToast(getString(R.string.cant_find));
                } else {
                    deleteData(name, number);
                    showToast(getString(R.string.delete_success));
                }

                cursor.close();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPeopleDb.close();
    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private void initColumn() {
        mName.setText(null);
        mNumber.setText(null);
    }

    private void addData(String name, String number) {
        ContentValues newRow = new ContentValues();
        newRow.put("name", name);
        newRow.put("number", number);
        mPeopleDb.insert(mTableName, null, newRow);
    }

    private void deleteData(String name, String number) {
        if (name.length() != 0) {
            mPeopleDb.delete(mTableName, "name=" + "\"" + name + "\"", null);
        } else if (number.length() != 0) {
            mPeopleDb.delete(mTableName, "number=" + "\"" + number + "\"", null);
        }
    }

    private Cursor searchData(String columnName, String searchItem) {
        return mPeopleDb.query(true, mTableName, new String[]{"name","number"},
                columnName +"=" + "\"" + searchItem + "\"", null, null, null, null,null);
    }

    private Cursor listAllData() {
        return mPeopleDb.query(true, mTableName, new String[]{"name","number"},
                null, null, null, null, null, null);
    }

}
