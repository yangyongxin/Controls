package com.yyx.view5.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by yangyongxin on 15/8/31.
 */
public class SlideMenu extends FrameLayout {

    private View menuView, mainView;
    private int downX;
    private int menuWidth = 0;
    private Scroller scroller;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
        menuWidth = menuView.getLayoutParams().width;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        menuView.layout(-menuWidth, 0, 0, bottom);
        mainView.layout(0, 0, right, bottom);
    }

  /*  @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) ( event.getX()- downX);
                if(Math.abs(deltaX)>8){
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int deltaX = (int) (moveX - downX);
                int newScrollX = getScrollX() - deltaX;
                if (newScrollX < -menuWidth) newScrollX = -menuWidth;
                if (newScrollX > 0) newScrollX = 0;
                scrollTo(newScrollX, 0);
                downX = moveX;
                break;
            case MotionEvent.ACTION_UP:
/*                ScrollAnimation scrollAnimation;
                if (getScrollX()>-menuWidth/2){
                    scrollAnimation = new ScrollAnimation(this, 0);
                }else {
                    scrollAnimation = new ScrollAnimation(this, -menuWidth);
                }
                startAnimation(scrollAnimation);
                break;*/
                if (getScrollX() > -menuWidth / 2) {
                    openMenu();
                } else {
                    closeMenu();
                }
                break;
        }
        return true;
    }

    private void openMenu() {
        scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 400);
        invalidate();
    }

    private void closeMenu() {
        scroller.startScroll(getScrollX(), 0, -menuWidth - getScrollX(), 0, 400);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    public void switchMenu() {
        if (getScrollX() == 0) {
            closeMenu();
        } else {
            openMenu();
        }
    }


}
