package com.example.test_lognin.calendar;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {
    public static LocalDate selectedDate;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formattedDate(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(dateTimeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formattedTime(LocalDateTime time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(dateTimeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(dateTimeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<LocalDate> dayInMonthArry(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArry = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayofweek = firstOfMonth.getDayOfWeek().getValue();
        for (int i = 1; i <= 42; i++) {
            if (i <= dayofweek || i > daysInMonth + dayofweek) {
                daysInMonthArry.add(null);
            } else {
                daysInMonthArry.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayofweek));
            }
        }
        return daysInMonthArry;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<LocalDate> dayInWeekArry(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayforDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDate sundayforDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);
        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return current;
            }
            current = current.minusDays(1);
        }
        return null;
    }
}

