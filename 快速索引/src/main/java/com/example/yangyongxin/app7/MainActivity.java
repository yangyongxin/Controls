package com.example.yangyongxin.app7;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yangyongxin.app7.adapter.HaoHanAdapter;
import com.example.yangyongxin.app7.bean.Person;
import com.example.yangyongxin.app7.view.QuickIndexBar;
import com.example.yangyongxin.app7.view.QuickIndexBar.OnLetterUpdateListener;
import com.example.yangyongxin.app7.util.Cheeses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    private ListView mMainList;
    private TextView tv_center;
    private List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainList = (ListView) findViewById(R.id.lv_main);
        tv_center = (TextView) findViewById(R.id.tv_center);
        QuickIndexBar bar = (QuickIndexBar) findViewById(R.id.bar);

        persons = new ArrayList<Person>();
        for (int i = 0; i < Cheeses.NAMES.length; i++) {
            String name = Cheeses.NAMES[i];
            persons.add(new Person(name));
        }
        // 进行排序
        Collections.sort(persons);
        HaoHanAdapter adapter = new HaoHanAdapter(this, persons);
        mMainList.setAdapter(adapter);

        // 设置监听
        bar.setListener(new OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
//				Utils.showToast(getApplicationContext(), letter);

                showLetter(letter);
                // 根据字母定位ListView, 找到集合中第一个以letter为拼音首字母的对象,得到索引
                for (int i = 0; i < persons.size(); i++) {
                    Person person = persons.get(i);
                    String l = person.getPinyin().charAt(0) + "";
                    if (TextUtils.equals(letter, l)) {
                        // 匹配成功
                        mMainList.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private Handler mHandler = new Handler();
    /**
     * 显示字母
     *
     * @param letter
     */
    protected void showLetter(String letter) {
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText(letter);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_center.setVisibility(View.GONE);
            }
        }, 2000);

    }

}
