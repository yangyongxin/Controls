package com.example.yangyongxin.app3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private EditText editText;

    private List<String> list = new ArrayList<String>();
    private ListView listView;
    private PopupWindow popupWindow;
    private ImageView iv_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initLitView();
        initListener();
    }

    private void initLitView() {
        listView = new ListView(this);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(new MyAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
                editText.setText(tv_number.getText().toString());
                popupWindow.dismiss();
            }
        });
    }

    private void initListener() {

        iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(listView, editText.getWidth(),
                        editText.getHeight() * (list.size() < 5 ? list.size() : 5));
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(editText, 0, 0);
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            list.add(13800000000l + i + "");
        }
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        iv_select = (ImageView) findViewById(R.id.iv_select);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final View view = View.inflate(MainActivity.this, R.layout.adapter_list, null);
            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            tv_number.setText(list.get(position));
            view.measure(0, 0);
            int l = view.getMeasuredHeight();
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();

                    popupWindow.update(editText.getWidth(), view.getMeasuredHeight() * (list.size() < 5 ? list.size() : 5));
                    if (list.size() == 0) {
                        popupWindow.dismiss();
                        iv_select.setVisibility(View.GONE);
                    }
                }
            });
            return view;
        }
    }
}
