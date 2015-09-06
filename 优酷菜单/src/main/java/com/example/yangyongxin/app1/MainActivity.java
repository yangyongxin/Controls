package com.example.yangyongxin.app1;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView iv_home, iv_menu;
    private RelativeLayout level1, level2, level3;

    private boolean isShowLevel2 = true;
    private boolean isShowLevel3 = true;
    private boolean isShowMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (AnimUtil.animCount != 0) {
                return true;
            }
            if (isShowMenu) {
                int startOffset = 0;
                if (isShowLevel3) {
                    AnimUtil.closeMeun(level3, startOffset);
                    startOffset += 200;
                    isShowLevel3 = false;
                }
                if (isShowLevel2) {
                    AnimUtil.closeMeun(level2, startOffset);
                    startOffset += 200;
                    isShowLevel2 = false;
                }
                AnimUtil.closeMeun(level1, startOffset);
            } else {
                AnimUtil.showMeun(level1, 0);
                AnimUtil.showMeun(level2, 200);
                isShowLevel2 = true;
                AnimUtil.showMeun(level3, 400);
                isShowLevel3 = true;

            }
            isShowMenu = !isShowMenu;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListener() {
        iv_home.setOnClickListener(this);
        iv_menu.setOnClickListener(this);

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        level1 = (RelativeLayout) findViewById(R.id.level1);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level3 = (RelativeLayout) findViewById(R.id.level3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home:
                if (AnimUtil.animCount != 0) {
                    return;
                }
                if (isShowLevel2) {
                    int startOffset = 0;
                    if (isShowLevel3) {
                        AnimUtil.closeMeun(level3, startOffset);
                        startOffset += 200;
                        isShowLevel3 = false;
                    }
                    AnimUtil.closeMeun(level2, startOffset);
                } else {
                    AnimUtil.showMeun(level2, 0);
                }
                isShowLevel2 = !isShowLevel2;
                break;
            case R.id.iv_menu:
                if (AnimUtil.animCount != 0) {
                    return;
                }
                if (isShowLevel3) {
                    AnimUtil.closeMeun(level3, 0);
                } else {
                    AnimUtil.showMeun(level3, 0);
                }
                isShowLevel3 = !isShowLevel3;
                break;
        }
    }
}
