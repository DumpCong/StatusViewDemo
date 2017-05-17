package com.xu.statusview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xu.statusview.utils.DensityUtil;


/**
 * 订单状态view
 * Created by xu on 2017/5/11.
 */
public class StatusView extends View {
    private Context mContext;
    private String[] contentArray = new String[]{"进行中", "安装汇报", "客户支付", "订单完成", "客户评价"};
    private Paint mBlodLinePaint;//粗线
    private Paint mThinLinePaint;//细线
    private Paint mTextPaint;//文字
    private int curStatus;//当前的状态(0-进行中 1-安装汇报 2-客户支付 3-订单完成 4-客户评价)
    private static int BLUE = Color.BLUE;
    private static int BLACK = Color.DKGRAY;
    private int viewWidth = 0;//StatusView宽度
    private int viewHeight = 0;//StatusView高度
    private int defaultWidth = 0;//宽度
    private int defaultHeight = 0;//高度
    private int textSize = 10;//单位SP

    //统一调用三个参数的构造方法
    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        curStatus = typedArray.getInteger(R.styleable.StatusView_curstatus, 0);
        typedArray.recycle();
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    //设置当前状态(0-进行中 1-安装汇报 2-客户支付 3-订单完成 4-客户评价)
    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
        invalidate();
    }

    //初始化画笔
    public void initPaint() {
        mBlodLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlodLinePaint.setStrokeWidth(DensityUtil.dip2px(mContext, 5));//粗线线宽
        mBlodLinePaint.setColor(BLUE);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(BLUE);
        mTextPaint.setTextSize(DensityUtil.sp2px(mContext, textSize));
        mThinLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mThinLinePaint.setStrokeWidth(DensityUtil.dip2px(mContext, 3));//细线线宽
        mThinLinePaint.setColor(BLACK);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = MeasureSpec.getMode(widthMeasureSpec);
        if (i == MeasureSpec.AT_MOST) {
            viewWidth = Math.min(defaultWidth, MeasureSpec.getSize(widthMeasureSpec));
        } else if (i == MeasureSpec.EXACTLY) {
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        int j = MeasureSpec.getMode(heightMeasureSpec);
        if (j == MeasureSpec.AT_MOST) {
            viewHeight = Math.min(defaultHeight, MeasureSpec.getSize(heightMeasureSpec));
        } else if (j == MeasureSpec.EXACTLY) {
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(viewWidth, viewHeight);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("curStatus", curStatus + "");
        Bitmap bigCircle = BitmapFactory.decodeResource(getResources(), R.mipmap.order_status_cricle);
        Bitmap smallCircle = BitmapFactory.decodeResource(getResources(), R.mipmap.order_status_small);
        int topMargin = (viewHeight - DensityUtil.sp2px(mContext, textSize) - DensityUtil.dip2px(mContext, 10) - smallCircle.getHeight()) / 2;
//      int topMargin = DensityUtil.dip2px(mContext, 15);
        int leftMargin = DensityUtil.dip2px(mContext, 20);
        int lineWidth = (viewWidth - 5 * smallCircle.getWidth() - leftMargin * 2) / 4;//线的长度
        int lineTopMargin = topMargin + DensityUtil.dip2px(mContext, 10)
                + DensityUtil.sp2px(mContext, textSize) + smallCircle.getHeight() / 2;
        int bitmapTopMargin = lineTopMargin - smallCircle.getHeight() / 2;
        for (int i = 0; i < 5; i++) {
            if (i < curStatus) {
                mTextPaint.setColor(BLUE);
                mThinLinePaint.setColor(BLUE);
                canvas.drawBitmap(smallCircle, leftMargin + i * smallCircle.getWidth() + i * lineWidth, bitmapTopMargin, mTextPaint);
                canvas.drawLine(leftMargin + i * lineWidth + (i + 1) * smallCircle.getWidth(), lineTopMargin,
                        leftMargin + i * lineWidth + lineWidth + (i + 1) * smallCircle.getWidth(), lineTopMargin, mThinLinePaint);
            }
            if (i == curStatus && curStatus != 4) {
                mTextPaint.setColor(BLUE);
                mThinLinePaint.setColor(BLACK);
                canvas.drawBitmap(bigCircle, leftMargin + i * smallCircle.getWidth() + i * lineWidth, bitmapTopMargin - 2, mTextPaint);
                canvas.drawLine(leftMargin + i * smallCircle.getWidth() + i * lineWidth + bigCircle.getWidth(), lineTopMargin,
                        leftMargin + i * smallCircle.getWidth() + i * lineWidth + lineWidth / 2 + +bigCircle.getWidth(), lineTopMargin, mBlodLinePaint);
                canvas.drawLine(leftMargin + i * smallCircle.getWidth() + i * lineWidth + lineWidth / 2 + bigCircle.getWidth(), lineTopMargin,
                        leftMargin + i * smallCircle.getWidth() + i * lineWidth + lineWidth + bigCircle.getWidth(), lineTopMargin, mThinLinePaint);
            }
            if (curStatus == 4 && curStatus == i) {
                mTextPaint.setColor(BLUE);
                canvas.drawBitmap(smallCircle, leftMargin + i * smallCircle.getWidth() + i * lineWidth, bitmapTopMargin, mTextPaint);
            }
            if (i > curStatus) {
                mTextPaint.setColor(BLACK);
                mThinLinePaint.setColor(BLACK);
                canvas.drawBitmap(smallCircle, leftMargin + (i - 1) * smallCircle.getWidth() + bigCircle.getWidth() + i * lineWidth, bitmapTopMargin, mTextPaint);
                if (i != 4) {
                    canvas.drawLine(leftMargin + i * lineWidth + (i) * smallCircle.getWidth() + bigCircle.getWidth(), lineTopMargin,
                            leftMargin + i * lineWidth + lineWidth + (i) * smallCircle.getWidth() + bigCircle.getWidth(), lineTopMargin, mThinLinePaint);
                }
            }
            canvas.drawText(contentArray[i], leftMargin + i * smallCircle.getWidth() + smallCircle.getWidth() / 2 + i * lineWidth - mTextPaint.measureText(contentArray[i]) / 2, topMargin, mTextPaint);
        }
    }
}
