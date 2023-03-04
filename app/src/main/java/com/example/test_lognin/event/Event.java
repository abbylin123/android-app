package com.example.test_lognin.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event>eventList=new ArrayList<>();
    public static ArrayList<Event> eventForDate(LocalDate date){
        ArrayList<Event>events=new ArrayList<>();
        for(Event event:eventList){
            if(event.getDate().equals(date)){
                events.add(event);
            }
        }
        return events;
    }

    private String name;
    private LocalDate date;
    private LocalDateTime time;

    public Event(String name, LocalDate date, LocalDateTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
