package com.stufeed.android.customui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.stufeed.android.R;

public class TriangleShapeView extends View {

    private int color;
    private Path path;
    private Paint paint;

    public TriangleShapeView(Context context) {
        super(context);
        initView();
    }

    public TriangleShapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initValue(context, attrs);
        initView();
    }

    public TriangleShapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValue(context, attrs);
        initView();
    }

    private void initValue(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.TriangleShapeView);

        color = ta.getColor(R.styleable.TextViewExpandableAnimation_tvea_textStateColor, ContextCompat.getColor(context, R.color.colorPrimary));

        ta.recycle();
    }

    private void initView() {
        int w = getWidth() / 2;

        path = new Path();
        path.moveTo(w, 0);
        path.lineTo(2 * w, 0);
        path.lineTo(2 * w, w);
        path.lineTo(w, 0);
        path.close();

        paint = new Paint();
        paint.setColor(color);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
        requestLayout();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth() / 2;

        path = new Path();
        path.moveTo(w, 0);
        path.lineTo(2 * w, 0);
        path.lineTo(2 * w, w);
        path.lineTo(w, 0);
        path.close();

        paint = new Paint();
        paint.setColor(color);
        canvas.drawPath(path, paint);
    }
}