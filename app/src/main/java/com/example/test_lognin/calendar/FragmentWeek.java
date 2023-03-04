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
import static com.example.test_lognin.calendar.CalendarUtils.dayInWeekArry;
import static com.example.test_lognin.calendar.CalendarUtils.monthYearFromDate;


public class FragmentWeek extends Fragment implements CalendarAdapter.OnItemListener{

    TextView monthYearText;
    RecyclerView calendarRecyclerView;
    ListView eventListView;
    onFgbtnMonth listener;
    onbtnevent event;
    Button monthAction,newevent,previousWeekAction,nextWeekAction;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_week,container,false);

        calendarRecyclerView=view.findViewById(R.id.calendarRecyclerView);
        monthYearText=view.findViewById(R.id.monthYearTV);
        eventListView=view.findViewById(R.id.eventListView);
        monthAction=view.findViewById(R.id.monthAction);
        newevent=view.findViewById(R.id.newEventAction);
        previousWeekAction=view.findViewById(R.id.previousWeekAction);
        nextWeekAction=view.findViewById(R.id.nextWeekAction);
        setWeekView();

        monthAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {listener.onMonthSelected();}
        });
        newevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {event.onAddEvent();}
        });

        //日曆切換
        previousWeekAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });
        nextWeekAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

        return view;
    }


   @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof onFgbtnMonth){
            listener= (onFgbtnMonth) context;
        }else {
            throw new ClassCastException(context.toString()+"must implement listener");
        }

       if(context instanceof onbtnevent){
           event= (onbtnevent) context;
       }else {
           throw new ClassCastException(context.toString()+"must implement listener");
       }
    }

    public interface onFgbtnMonth{
        public void onMonthSelected();
    }


    public interface onbtnevent{
        public void onAddEvent();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days=dayInWeekArry(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter=new CalendarAdapter(days,this);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getActivity().getApplicationContext(),7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        setEventAdpater();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate=date;
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater() {
        ArrayList<Event> dailyEvent=Event.eventForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter=new EventAdapter(getActivity().getApplicationContext(),dailyEvent);
        eventListView.setAdapter(eventAdapter);
    }



}