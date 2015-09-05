package com.example.yangyongxin.qq;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yangyongxin.qq.util.Cheeses;
import com.example.yangyongxin.qq.view.DragLayout;
import com.example.yangyongxin.qq.view.MyLinearLayout;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.Random;

public class MainActivity extends Activity {

    private ListView mLeftList;
    private ListView mMainList;
    private ImageView mHeaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLeftList = (ListView) findViewById(R.id.lv_left);
        mMainList = (ListView) findViewById(R.id.lv_main);
        mHeaderImage = (ImageView) findViewById(R.id.iv_header);
        MyLinearLayout mLinearLayout = (MyLinearLayout) findViewById(R.id.mll);
        DragLayout mDragLayout = (DragLayout) findViewById(R.id.dl);

        mLeftList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextColor(Color.WHITE);
                return view;
            }
        });

        mMainList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));

        mDragLayout.setDragStatusListener(new DragLayout.OnDragStatusChangeListener() {

            @Override
            public void onOpen() {
                Random random = new Random();
                int nextInt = random.nextInt(50);
                mLeftList.smoothScrollToPosition(nextInt);
            }

            @Override
            public void onClose() {
                ObjectAnimator mAnimator = ObjectAnimator.ofFloat(mHeaderImage, "translationX", 15.0f);
                mAnimator.setInterpolator(new CycleInterpolator(4));
                mAnimator.setDuration(500);
                mAnimator.start();
            }


            @Override
            public void onDraging(float percent) {
                ViewHelper.setAlpha(mHeaderImage, 1 - percent);
            }
        });

        mLinearLayout.setDraglayout(mDragLayout);

    }

}
