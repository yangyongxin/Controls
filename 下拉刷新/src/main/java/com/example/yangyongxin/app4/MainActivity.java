package com.example.yangyongxin.app4;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yangyongxin.app4.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RefreshListView refreshListView;

    private MyAdapter adapter;
    private List<String> list = new ArrayList<String>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
            refreshListView.completeRefresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        for (int i = 1; i <= 20; i++) {
            list.add("listview原来的数据-----" + i);
        }
        adapter = new MyAdapter();
        refreshListView.setAdapter(adapter);
        refreshListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                requestDataFromServer(false);
            }

            @Override
            public void onLoadingMore() {
                requestDataFromServer(true);
            }
        });
    }

    private void requestDataFromServer(final boolean isLoadingMore) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                if (isLoadingMore) {
                    for (int i = 1; i <= 5; i++) {
                        list.add("加载更多的数据-----" + i);
                    }
                } else {
                    list.add(0, "下拉刷新的数据");
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
        ;
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        refreshListView = (RefreshListView) findViewById(R.id.refreshListView);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(MainActivity.this);
            textView.setPadding(20, 20, 20, 20);
            textView.setTextSize(18);
            textView.setText(list.get(position));
            return textView;
        }
    }

}
