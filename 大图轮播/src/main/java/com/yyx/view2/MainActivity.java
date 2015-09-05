package com.yyx.view2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyx.view2.bean.Ad;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ViewPager viewPager;
    private List<Ad> list = new ArrayList<Ad>();
    private TextView textView;
    private LinearLayout dot_layout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        textView = (TextView) findViewById(R.id.textView);
        dot_layout = (LinearLayout) findViewById(R.id.dot_layout);
    }

    private void initData() {
        list.add(new Ad(R.drawable.a, "巩俐不低俗，我就不能低俗"));
        list.add(new Ad(R.drawable.b, "朴树又回来了，再唱经典老歌引百万人同唱啊"));
        list.add(new Ad(R.drawable.c, "揭秘北京电影如何升级"));
        list.add(new Ad(R.drawable.d, "乐视网TV版大放送"));
        list.add(new Ad(R.drawable.e, "热血屌丝的反杀"));
        initDots();
        viewPager.setAdapter(new MyPagerAdapter());
        int centerValue = Integer.MAX_VALUE / 2;
        int value = centerValue % list.size();
        viewPager.setCurrentItem(centerValue - value);
        updateIntroAndDot();
        handler.sendEmptyMessageDelayed(0, 4000);
    }

    private void initDots() {
        for (int i = 0; i < list.size(); i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            params.leftMargin = 5;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selector_dot);
            dot_layout.addView(view);
        }
    }

    ;

    private void initListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateIntroAndDot();

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateIntroAndDot() {
        int currrentPage = viewPager.getCurrentItem() % list.size();
        textView.setText(list.get(currrentPage).getIntro());

        for (int i = 0; i < dot_layout.getChildCount(); i++) {
            dot_layout.getChildAt(i).setEnabled(i == currrentPage);
        }

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(MainActivity.this, R.layout.adapter_ad, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Ad ad = list.get(position % list.size());
            imageView.setImageResource(ad.getIconResId());
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


}
