package com.example.test_lognin.event;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.test_lognin.FragmentHome;
import com.example.test_lognin.R;
import com.example.test_lognin.calendar.CalendarUtils;
import com.example.test_lognin.event.Event;

import java.time.LocalDateTime;

public class FragmentEventEdit extends Fragment {
    EditText eventNameET;
    TextView eventDateTV,eventTimeTV;
    LocalDateTime time;
    onbtnSave save;
    Button btnSave;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_event_edit, container, false);

        eventNameET=view.findViewById(R.id.eventNameET);
        eventDateTV=view.findViewById(R.id.eventDateTV);
        eventTimeTV=view.findViewById(R.id.eventTimeTV);
        btnSave=view.findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName=eventNameET.getText().toString();
                Event newEvent=new Event(eventName, CalendarUtils.selectedDate,time);
                Event.eventList.add(newEvent);
                save.onSaveEvent();
            }
        });
        time= LocalDateTime.now();
        eventDateTV.setText("Date: "+CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: "+CalendarUtils.formattedTime(time));

        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentHome.onFragmentBtnSelected){
            save= (onbtnSave) context;
        }else {
            throw new ClassCastException(context.toString()+"must implement listener");
        }

    }

    public interface  onbtnSave{
        public void onSaveEvent();
    }
}
