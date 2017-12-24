package com.cstu.model.json.objects;

import java.util.List;

public class ScheduleDay {

    private String date;
    private String dayName;
    private List<ScheduleLesson> lessons;

    public ScheduleDay(String date, String dayName, List<ScheduleLesson> lessons) {
        this.date = date;
        this.dayName = dayName;
        this.lessons = lessons;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<ScheduleLesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<ScheduleLesson> lessons) {
        this.lessons = lessons;
    }
}
