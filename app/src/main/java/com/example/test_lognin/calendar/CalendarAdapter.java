package com.example.test_lognin.calendar;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test_lognin.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener){
        this.days=days;
        this.onItemListener = onItemListener;
    }

    // 建立 ViewHolder 的地方，如果有同時支援多種 layout 的需求
    @NonNull@Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
        if(days.size()>15){
            layoutParams.height=(int)(parent.getHeight()*0.166666666);
        }
        else{
            layoutParams.height=(int)parent.getHeight();
        }

        return new CalendarViewHolder(view, onItemListener,days);
    }

    // 因為 ViewHolder 會重複使用，依據 position把正確的資料跟 ViewHolder 綁定在一起
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        final LocalDate date = days.get(position);
        if(date==null){
            holder.dayofMonth.setText("");
        }
        else{
            holder.dayofMonth.setText(String.valueOf(date.getDayOfMonth()));
            if(date.equals(CalendarUtils.selectedDate)){
                holder.parentView.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    // 回傳整個 Adapter
    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface OnItemListener{
        void onItemClick(int position, LocalDate date);
    }
}

