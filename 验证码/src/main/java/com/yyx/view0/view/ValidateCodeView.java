package com.yyx.view0.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.yyx.view0.R;

import java.util.Random;

/**
 * Created by yangyongxin on 15/8/28.
 */
public class ValidateCodeView extends View {

    private Paint paint;
    private String titleText;
    private int bgColor;
    private int titleSize;
    private int titleColor;
    private Rect rect;

    public ValidateCodeView(Context context) {
        this(context, null);
    }

    public ValidateCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = getRandomString();
                postInvalidate();
            }
        });
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ValidateCodeView);
        titleText = getRandomString();
        titleSize = array.getDimensionPixelSize(R.styleable.ValidateCodeView_titleSize, 10);
        titleColor = array.getColor(R.styleable.ValidateCodeView_titleColor, 0x0);
        bgColor = array.getColor(R.styleable.ValidateCodeView_bgColor, 0x0);
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(titleSize);
            rect = new Rect();
            paint.getTextBounds(titleText, 0, titleText.length(), rect);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = rect.height() + titleSize;
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = rect.width() + titleSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(bgColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
        paint.setColor(titleColor);
        canvas.drawText(titleText, getWidth() / 2 - rect.width() / 2, getHeight() / 2 + rect.height() / 2, paint);
    }

    public String getRandomString() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
