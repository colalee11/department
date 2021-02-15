package com.example.campusdepartment.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.campusdepartment.R;
import com.example.campusdepartment.SQLite.ContentSQLiteHelper;
import com.example.campusdepartment.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchMainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    Drawable bitmap;
    EditText mEditSearch;
    TextView mTvSearch;
    TextView mTvTip, tt;
    ImageView back;
    ListViewForScrollView mListView;
    ListViewForScrollView mListView1;
    TextView mTvClear;
    SimpleCursorAdapter adapter;
    ContentSQLiteHelper searchSqliteHelper, recordsSqliteHelper;
    //RecordsSqliteHelpe recordsSqliteHelper;
    SQLiteDatabase db_search;
    SQLiteDatabase db_records;
    Cursor cursor, cursor1;
    SimpleAdapter adapter1;
    ScrollView scrollView, scrollView1;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    String searchString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        initview();
        initListener();
        initdata();
    }

    private void initview() {
        mEditSearch = findViewById(R.id.edittext);
        mTvTip = findViewById(R.id.tv_tip);
        mTvSearch = findViewById(R.id.tv_search);
        tt = findViewById(R.id.tt);
        mListView = findViewById(R.id.listView);
        mListView1 = findViewById(R.id.listView1);
        mTvClear = findViewById(R.id.tv_clear);
        scrollView = findViewById(R.id.scrollView);
        scrollView1 = findViewById(R.id.scrollView1);
        back = findViewById(R.id.picture);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initdata() {
        recordsSqliteHelper = new ContentSQLiteHelper(this);
        cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
        adapter = new SimpleCursorAdapter(this, R.layout.search_list, cursor
                , new String[]{"username"}, new int[]{R.id.text1}
                , CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(adapter);
        // 尝试从保存信息的数据库中获取数据并显示
        adapter1 = new SimpleAdapter(this, mData, R.layout.search_list_activity,
                new String[]{"content", "picture", "number", "price"},
                new int[]{R.id.content, R.id.picture, R.id.number, R.id.price});

        adapter1.setViewBinder(new SimpleAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else {
                    return false;
                }
            }
        });
        mListView1.setAdapter(adapter1);
        mListView1.setOnItemClickListener(this);
    }


    //初始化事件监听
    private void initListener() {
        //清除历史纪录
        mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecords();
            }
        });

        //搜索按钮保存搜索纪录，隐藏软键盘
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //保存搜索记录
                insertRecords(mEditSearch.getText().toString().trim());
                if (mEditSearch.getText().toString().equals("")) {
                    tt.setVisibility(View.GONE);
                    scrollView1.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
                    refreshListView();
                } else {
                    scrollView.setVisibility(View.GONE);
                    scrollView1.setVisibility(View.VISIBLE);
                    searchString = mEditSearch.getText().toString();
                    GetCommonUsers(searchString);

                }
            }
        });

        /**
         * EditText对键盘搜索按钮的监听，保存搜索纪录，隐藏软件盘
         */
        // 搜索及保存搜索纪录
        mEditSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //保存搜索记录
                    insertRecords(mEditSearch.getText().toString().trim());
                }
                return false;
            }
        });
        /**
         * EditText搜索框对输入值变化的监听，实时搜索
         */
        // 使用TextWatcher实现对实时搜索
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEditSearch.getText().toString().equals("")) {
                    tt.setVisibility(View.GONE);
                    scrollView1.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
                    refreshListView();
                }

            }
        });

    }

    //删除历史纪录
    private void deleteRecords() {
        db_records = recordsSqliteHelper.getWritableDatabase();
        db_records.execSQL("delete from table_records");

        cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
        if (mEditSearch.getText().toString().equals("")) {
            refreshListView();
        }
    }

    private void refreshListView() {
        adapter.notifyDataSetChanged();
        adapter.swapCursor(cursor);
    }

    //保存搜索纪录
    private void insertRecords(String username) {
        if (!hasDataRecords(username)) {
            db_records = recordsSqliteHelper.getWritableDatabase();
            db_records.execSQL("insert into table_records values(null,?,?,?,?,?)", new String[]{username, ""});
            db_records.close();
        }
    }

    //检查是否已经存在此搜索纪录
    private boolean hasDataRecords(String records) {

        cursor = recordsSqliteHelper.getReadableDatabase()
                .rawQuery("select _id,username from table_records where username = ?"
                        , new String[]{records});

        return cursor.moveToNext();
    }

    protected void GetCommonUsers(String searchData) {
        searchSqliteHelper = new ContentSQLiteHelper(this);
        cursor1 = searchSqliteHelper.getWritableDatabase().rawQuery(
                "select * from commodities where content like '%" + searchData + "%'", null);
        mData.clear();
        adapter1.notifyDataSetChanged();
        int num = cursor1.getCount();
        if (num > 0) {
            byte[] imgArr = null;
            Bitmap photo = null;
            // 必须使用moveToFirst方法将记录指针移动到第1条记录的位置
            cursor1.moveToFirst();
            do {
                String eName = cursor1.getString(cursor1.getColumnIndex("content"));
                String eprice = cursor1.getString(cursor1.getColumnIndex("price"));
                String enumber = cursor1.getString(cursor1.getColumnIndex("number"));
                imgArr = cursor1.getBlob(cursor1.getColumnIndex("picture"));
                Map<String, Object> item = new HashMap<String, Object>();
                if (null != imgArr && imgArr.length > 0) {
                    photo = BitmapFactory.decodeByteArray(imgArr, 0, imgArr.length);
                    item.put("picture", photo);
                } else { //如果用户没有上传图片，就用默认图标代替
                    item.put("image", R.mipmap.ic_launcher);
                }
                item.put("content", eName);
                item.put("price", eprice);
                item.put("number", enumber);
                mData.add(item);
            } while (cursor1.moveToNext());

        }
        if (mData.size() == 0) {
            tt.setVisibility(View.VISIBLE);
            scrollView1.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
        } else {
            tt.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bitmap bitmap = (Bitmap) mData.get(position).get("picture");
        Bundle bundle = new Bundle();
        bundle.putString("content", (String) mData.get(position).get("content"));
        bundle.putParcelable("picture", bitmap);
        bundle.putString("number", (String) mData.get(position).get("number"));
        bundle.putString("price", (String) mData.get(position).get("price"));
        Intent intent = new Intent(SearchMainActivity.this, ProductDetailsMainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
