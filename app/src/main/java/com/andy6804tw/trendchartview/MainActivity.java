package com.andy6804tw.trendchartview;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.andy6804tw.trendchartviewlib.HorizontalScrollChartParentView;
import com.andy6804tw.trendchartviewlib.ITrendData;
import com.andy6804tw.trendchartviewlib.TrendChartView;
import com.andy6804tw.trendchartviewlib.TrendYAxisView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private TrendYAxisView trendYAxis;
    private HorizontalScrollChartParentView svContainer;
    private TrendChartView trendChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        trendYAxis = (TrendYAxisView) findViewById(R.id.trend_y_axis);
        svContainer = (HorizontalScrollChartParentView) findViewById(R.id.sv_container);
        trendChartView = (TrendChartView) findViewById(R.id.trend_chart_view);
        trendYAxis.setChartView(trendChartView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) trendYAxis.getLayoutParams();
        params.width = trendChartView.getLeftMarginSize();
        params.height = trendChartView.getChartHeight();
        trendYAxis.setLayoutParams(params);

        svContainer.setOnScrollListener(new HorizontalScrollChartParentView.OnScrollListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                trendChartView.onTrendCharScrollChanged(x, y, oldX, oldY);
            }
        });

        trendChartView.post(new Runnable() {
            @Override
            public void run() {
                trendChartView.fillData(formatDataList(), mockDayList(), getForecastDaysCount());
            }
        });
        trendChartView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: "+trendChartView.getOffsetByPosition(1));
                svContainer.smoothScrollTo(trendChartView.getOffsetByPosition(1),0);
            }
        },1000);

    }

    private int getForecastDaysCount() {
        return 3;
    }

    private List<String> mockDayList() {
        List<String> list = new ArrayList<>(getForecastDaysCount());
        list.add("昨天");
        list.add("今天");
        list.add("明天");
        return list;
    }


    List<ITrendData> formatDataList() {
        List<ITrendData> dataList = new ArrayList<>();
        long todayTime = getDayBegin().getTime();
        long yesterday = todayTime - 24 * 60 * 60 * 1000;
        Random random = new Random();
        for (int i = 0; i < getForecastDaysCount() * 24; i++) {
            int baseValue = 300;
            if (i / 24 == 0) {
                baseValue = 40;
            }else if(i / 24 == 1){
                baseValue = 50;
            }else if(i / 24 == 2){
                baseValue = 55;
            }else if(i / 24 == 3){
                baseValue = 52;
            }else if(i / 24 == 4){
                baseValue = 65;
            }else if(i / 24 == 5){
                baseValue = 100;
            }else if(i / 24 == 6){
                baseValue = 80;
            }
            int aqiValue = baseValue+i*2;
            Log.d("=======", "baseValue "+baseValue+" aqiValue "+aqiValue);
            int aqiLevel = Utils.getAqiIndex(aqiValue);
            String aqiDesc = getString(Utils.getIndexDescription(aqiLevel));
            @ColorInt int color = Utils.getColor(this, Utils.getIndexColor(aqiLevel));
            dataList.add(new TrendHourBean(yesterday + (60 * 60 * 1000 * i), aqiValue, aqiLevel, color, aqiValue, aqiDesc));
        }
        return dataList;
    }

    public Timestamp getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

}
