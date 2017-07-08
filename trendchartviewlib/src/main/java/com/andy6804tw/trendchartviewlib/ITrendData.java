package com.andy6804tw.trendchartviewlib;


import android.support.annotation.ColorInt;

public interface ITrendData {
    /**
     * time for data (unit millisecond )
     * @return
     */
    long timestamp();

    /**
     * value for data
     * @return
     */
    int value();

    /**
     * aqi 精确数值对应的大概数值 非精确
     * @return
     */
    int warpValue();

    /**
     * text info in pop view used indicator current status
     * @return
     */
    String popTextInfo();

    /**
     * 等级级别值
     * @return
     */
    int colourLevel();


    @ColorInt
    int levelColor();
}