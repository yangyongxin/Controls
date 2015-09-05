package com.example.yangyongxin.app4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yangyongxin.app4.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangyongxin on 15/8/29.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final int PULL_REFRESH = 0;
    private static final int REFRESHING = 1;
    private static final int RELEASE_REFRESH = 2;

    private View headerView;
    private View footerView;
    private ImageView iv_arrow;
    private ProgressBar pb_rotate;
    private TextView tv_state;
    private TextView tv_time;

    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;

    private int downY;
    private int headerViewHeight;
    private int footerViewHeight;
    private int currntState = PULL_REFRESH;
    private boolean isLoadingMore = false;

    public RefreshListView(Context context) {
        super(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOnScrollListener(this);
        initHeaderView();
        initRotateAnimation();
        initFooterView();
    }

    private void initRotateAnimation() {
        upAnimation = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(300);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(-180, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.layout_header, null);
        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb_rotate = (ProgressBar) headerView.findViewById(R.id.pb_rotate);
        tv_state = (TextView) headerView.findViewById(R.id.tv_state);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        addHeaderView(headerView);
    }

    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.layout_footer, null);

        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        addFooterView(footerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (currntState == REFRESHING) {
                    break;
                }
                int deltaY = (int) (ev.getY() - downY);
                int paddingTop = -headerViewHeight + deltaY;
                if (paddingTop > -headerViewHeight && getFirstVisiblePosition() == 0) {
                    if (paddingTop >= 0 && currntState == PULL_REFRESH) {
                        currntState = RELEASE_REFRESH;
                        refreshHeaderView();
                    } else if (paddingTop < 0 && currntState == RELEASE_REFRESH) {
                        currntState = PULL_REFRESH;
                        refreshHeaderView();

                    }

                    headerView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currntState == PULL_REFRESH) {
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                } else if (currntState == RELEASE_REFRESH) {
                    headerView.setPadding(0, 0, 0, 0);
                    currntState = REFRESHING;
                    refreshHeaderView();
                    if (listener != null) {
                        listener.onPullRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshHeaderView() {
        switch (currntState) {
            case PULL_REFRESH:
                tv_state.setText("下拉刷新");
                iv_arrow.startAnimation(downAnimation);
                break;
            case REFRESHING:
                tv_state.setText("正在刷新");
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(View.INVISIBLE);
                pb_rotate.setVisibility(View.VISIBLE);
                break;
            case RELEASE_REFRESH:
                tv_state.setText("松开刷新");
                iv_arrow.startAnimation(upAnimation);
                break;
        }

    }

    public void completeRefresh() {
        if (isLoadingMore) {
            footerView.setPadding(0, -footerViewHeight, 0, 0);
            isLoadingMore = false;
        } else {
            headerView.setPadding(0, -headerViewHeight, 0, 0);
            tv_state.setText("下拉刷新");
            tv_time.setText("最后刷新:" + getCurrentTime());
            pb_rotate.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            currntState = PULL_REFRESH;
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    private OnRefreshListener listener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }


    public interface OnRefreshListener {
        void onPullRefresh();

        void onLoadingMore();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && getLastVisiblePosition() == (getCount() - 1) && !isLoadingMore) {
            isLoadingMore = true;
            footerView.setPadding(0, 0, 0, 0);
            setSelection(getCount());
            if (listener != null) {
                listener.onLoadingMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
