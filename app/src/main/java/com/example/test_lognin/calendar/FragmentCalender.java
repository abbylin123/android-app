package com.example.test_lognin.calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test_lognin.event.Event;
import com.example.test_lognin.event.EventAdapter;
import com.example.test_lognin.R;

import java.time.LocalDate;
import java.util.ArrayList;
import static com.example.test_lognin.calendar.CalendarUtils.dayInMonthArry;
import static com.example.test_lognin.calendar.CalendarUtils.monthYearFromDate;

public class FragmentCalender extends Fragment implements CalendarAdapter.OnItemListener{

    TextView monthYearText;
    RecyclerView calendarRecyclerView;
    ListView eventListView;
    onFgbtnWeek listener;
    Button weekAction,previousMonthAction,nextMonthAction;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_calender,container,false);

        calendarRecyclerView=view.findViewById(R.id.calendarRecyclerView);
        monthYearText=view.findViewById(R.id.monthYearTV);
        eventListView=view.findViewById(R.id.eventListView);
        CalendarUtils.selectedDate=LocalDate.now();
        weekAction=view.findViewById(R.id.weekAction);
        previousMonthAction=view.findViewById(R.id.previousMonthAction);
        nextMonthAction=view.findViewById(R.id.nextMonthAction);
        setMonthView();

        //利用 button 進行跳頁
        weekAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {listener.onWeekSelected();}
        });


        //月曆切換
        previousMonthAction.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
                setMonthView();
            }
        });
        nextMonthAction.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
                setMonthView();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof onFgbtnWeek){
            listener= (onFgbtnWeek) context;
        }else {
            throw new ClassCastException(context.toString()+"must implement listener");
        }
    }


    public interface onFgbtnWeek{
        public void onWeekSelected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth=dayInMonthArry(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter=new CalendarAdapter(daysInMonth,this);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity().getApplicationContext(),7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date!=null){
            CalendarUtils.selectedDate=date;
            setMonthView();
        }

    }
    private void setEventAdpater() {
        ArrayList<Event> dailyEvent=Event.eventForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter=new EventAdapter(getActivity().getApplicationContext(),dailyEvent);
        eventListView.setAdapter(eventAdapter);
    }

    /*public void weeklyAction(View view){
        startActivity(new Intent(getActivity().getApplicationContext(),FragmentWeek.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    */


}