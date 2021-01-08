package com.jnu.relationshipnotes.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class HistogramView extends View {
    private Paint linePaint = new Paint();
    private Paint graphPaint= new Paint();
    private Paint textPaint = new Paint();
    private List<Data> mList= new ArrayList<>();
    private int horizonLength = 0;//水平方向长度
    private int verticalHeight= 0;//垂直方向高度
    private float hScaleSize = 1;
    private float wScaleSize = 1;
    public HistogramView(Context context) { super(context); }
    public HistogramView(Context context, AttributeSet attrs) { super(context, attrs); }
    private void init() {
        linePaint.setColor(Color.GRAY);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(3);
        graphPaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int width=0;
        float sum=0;
        for(Data histogram:mList){
            horizonLength+=histogram.spaceWidth+histogram.valueWidth;
            width=histogram.spaceWidth;
            if(sum<histogram.value)sum=histogram.value;
        }
        horizonLength +=width;
        verticalHeight+=sum+sum/4;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth() -50;
        int height= canvas.getHeight()-200;
        if (width < horizonLength) wScaleSize= (float) horizonLength / (float) width;
        if (height< verticalHeight)hScaleSize= (float) verticalHeight/ (float) height;
        int startX = (int) (width / 20 / wScaleSize);
        int startY = (int) (height/ 20 / hScaleSize);
        //垂直线
        canvas.drawLine(startX / wScaleSize, startY / hScaleSize,
                startX / wScaleSize,
                (startY + verticalHeight) / hScaleSize, linePaint);
        //水平线
        canvas.drawLine(startX/wScaleSize,(startY+verticalHeight)/hScaleSize,
                (startX + horizonLength) / wScaleSize,
                (startY + verticalHeight)/ hScaleSize, linePaint);
        int pWidth = startX;
        for (Data histogram : mList) {
            int red  =(int) (Math.random()*255);
            int green=(int) (Math.random()*255);
            int blue =(int) (Math.random()*255);
            int color= Color.rgb(red, green, blue);
            graphPaint.setColor(color);
            textPaint.setColor(color);
            graphPaint.setStrokeWidth(histogram.valueWidth / wScaleSize);
            if (pWidth == startX) pWidth += histogram.spaceWidth+histogram.valueWidth/2;
            else pWidth+= histogram.spaceWidth + histogram.valueWidth;
            //柱状图
            canvas.drawLine(pWidth/wScaleSize,(startY+verticalHeight)/hScaleSize,
                    pWidth / wScaleSize,
                    (startY+verticalHeight-histogram.value)/hScaleSize, graphPaint);
            float size = textPaint.getTextSize();
            textPaint.setTextSize(40);
            //绘制底部文字
            canvas.drawText(histogram.valueName, pWidth / wScaleSize,
                    (startY+verticalHeight)/hScaleSize+2*(size/wScaleSize),textPaint);
            //绘制顶部文字
            canvas.drawText(String.valueOf(histogram.value),pWidth/wScaleSize,
                    (startY+verticalHeight-histogram.value)/hScaleSize-(size/wScaleSize),textPaint);
        }
    }
    public void setData(List<Data> list) {
        mList.clear();
        mList.addAll(list);
        init();
    }
    public static class Data {
        //柱状图Y高度
        private int value = 0;
        //柱状图X名称
        private String valueName = "X";
        //柱状图宽度
        private int valueWidth = 100;
        //柱状图间距
        private int spaceWidth = 50;
        public Data(int value, String valueName) {
            this.value = value;
            this.valueName = valueName;
        }
    }
}
