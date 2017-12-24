package com.cstu.model.json.objects;

public class ScheduleLesson {

    private String lessonNumber;
    private String time;
    private ScheduleInfo schedule;

    public ScheduleLesson(String time, ScheduleInfo schedule) {
        this.time = time;
        this.schedule = schedule;
    }

    public ScheduleLesson(String lessonNumber, String time, ScheduleInfo schedule) {
        this.lessonNumber = lessonNumber;
        this.time = time;
        this.schedule = schedule;
    }
}
