package com.example.yangyongxin.qq.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by yangyongxin on 15/9/1.
 */
public class DragLayout extends FrameLayout {
    private ViewDragHelper mDragHelper;

    private ViewGroup mLeftContent;
    private ViewGroup mMainContent;
    private int mWidth;
    private int mHeight;
    private int mRange;
    private OnDragStatusChangeListener mListener;
    private Status mStatus = Status.Close;


    public static enum Status {
        Close, Open, Draging, Status;
    }

    public interface OnDragStatusChangeListener {
        void onOpen();

        void onClose();

        void onDraging(float percent);
    }

    public void setDragStatusListener(OnDragStatusChangeListener mListener) {
        this.mListener = mListener;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }


    public DragLayout(Context context) {
        super(context);
        init();
    }


    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {

            return mRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mMainContent) {
                left = fixLeft(left);
            }
            return left;
        }

        private int fixLeft(int left) {
            if (left < 0) {
                return 0;
            } else if (left > mRange) {
                return mRange;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            int newLeft = left;
            if (changedView == mLeftContent) {
                left = mMainContent.getLeft() + dx;
                newLeft = fixLeft(left);
                mLeftContent.layout(0, 0, mWidth, mHeight);
                mMainContent.layout(newLeft, 0, newLeft + mWidth, mHeight);
            }
            dispathDragEvent(newLeft);

            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel == 0 && mMainContent.getLeft() > mRange / 2) {
                open();
            } else if (xvel > 0) {
                open();
            } else {
                close();
            }
            super.onViewReleased(releasedChild, xvel, yvel);
        }


        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }
    };

    public void close() {
        if (mDragHelper.smoothSlideViewTo(mMainContent, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(DragLayout.this);
        }
        ;
        // mMainContent.layout(0, 0, mWidth,mHeight);
    }

    public void open() {
        if (mDragHelper.smoothSlideViewTo(mMainContent, mRange, 0)) {
            ViewCompat.postInvalidateOnAnimation(DragLayout.this);
        }
        ;
        //mMainContent.layout(mRange, 0, mHeight + mWidth,mHeight);
    }


    private void dispathDragEvent(int newLeft) {

        float percent = newLeft * 1.0f / mRange;

        if (mListener != null) {
            mListener.onDraging(percent);
        }
        Status preStatus = mStatus;
        mStatus = updateStatus(percent);
        if (mStatus != preStatus) {
            if (mStatus == Status.Close) {
                if (mListener != null) {
                    mListener.onClose();
                }
            } else if (mStatus == Status.Open) {
                if (mListener != null) {
                    mListener.onOpen();
                }
            }
        }
        animViews(percent);
    }

    private Status updateStatus(float percent) {
        if (percent == 0.0f) {
            return Status.Close;
        } else if (percent == 1.0f) {
            return Status.Open;
        } else {
            return Status.Draging;
        }
    }

    private void animViews(float percent) {
        // 缩放动画
        ViewHelper.setScaleX(mLeftContent, evaluate(percent, 0.5f, 1.0f));
        ViewHelper.setScaleY(mLeftContent, evaluate(percent, 0.5f, 1.0f));
        // 平移动画:
        ViewHelper.setTranslationX(mLeftContent, evaluate(percent, -mWidth / 2.0f, 0));
        // 透明度:
        ViewHelper.setAlpha(mLeftContent, evaluate(percent, 0.5f, 1.0f));

        ViewHelper.setScaleX(mMainContent, evaluate(percent, 1.0f, 0.8f));
        ViewHelper.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f));

        getBackground().setColorFilter(evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);

    }

    /**
     * 估值器
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 颜色变化过度
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public int evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int) ((startA + (int) (fraction * (endA - startA))) << 24) |
                (int) ((startR + (int) (fraction * (endR - startR))) << 16) |
                (int) ((startG + (int) (fraction * (endG - startG))) << 8) |
                (int) ((startB + (int) (fraction * (endB - startB))));
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) {
            throw new IllegalStateException("布局至少有两个孩子");
        }
        if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("View必须是ViewGroup的子类");
        }
        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRange = (int) (mWidth * 0.6);

    }
}
