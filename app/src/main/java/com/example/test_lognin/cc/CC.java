package com.example.test_lognin.cc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.test_lognin.R;
import com.example.test_lognin.group.GroupItemDecoration;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

public class CC extends AppCompatActivity implements CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener {

    TextView TextMonthDay,TextYear,TextCurrentDay;
    CalendarView calendarView;
    RelativeLayout relativeLayout;
    private int year;
    CalendarLayout calendarLayout;
    RecyclerView recyclerView;

    protected void initWindow() { }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_cc);
        TextMonthDay = findViewById(R.id.tv_month_day);
        TextYear = findViewById(R.id.tv_year);
        relativeLayout = findViewById(R.id.rl_tool);
        calendarView = findViewById(R.id.calendarView);
        TextCurrentDay = findViewById(R.id.tv_current_day);
        TextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!calendarLayout.isExpand()) {
                    calendarLayout.expand();  //自動展開填充布局
                    return;
                }
                calendarView.showYearSelectLayout(year);
                TextYear.setVisibility(View.GONE);
                TextMonthDay.setText(String.valueOf(year));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.scrollToCurrent();
            }
        });

        calendarLayout = findViewById(R.id.calendarLayout);
        calendarView.setOnYearChangeListener(this);
        calendarView.setOnCalendarSelectListener(this);
        TextYear.setText(String.valueOf(calendarView.getCurYear()));
        year = calendarView.getCurYear();
        TextMonthDay.setText(calendarView.getCurMonth() + "月" + calendarView.getCurDay() + "日");
        TextCurrentDay.setText(String.valueOf(calendarView.getCurDay()));
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        recyclerView.setAdapter(new ArticleAdapter(this));

        initData();
    }

    @Override
    public void initData() {
        int year = calendarView.getCurYear();
        int month = calendarView.getCurMonth();
        //Map<String, Calendar> map = new HashMap<>(); //寫標記日期
        //map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
        //        getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        //calendarView.setSchemeDate(map);
    }

    //標記的設定
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color); //標記顏色
        calendar.setScheme(text);
        return calendar;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        TextYear.setVisibility(View.VISIBLE);
        TextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        TextYear.setText(String.valueOf(calendar.getYear()));
        year = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
        TextMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }
}