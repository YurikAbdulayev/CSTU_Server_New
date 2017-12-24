package com.cstu.model.json.objects;

public class ScheduleInfo {

    String lessonType;
    String lessonName;
    String teacher;
    String lessonRoom;
    String lessonDetails;
    String fullData;

    public ScheduleInfo(String lessonType, String lessonName, String teacher, String lessonRoom, String lessonDetails, String fullData) {
        this.lessonType = lessonType;
        this.lessonName = lessonName;
        this.teacher = teacher;
        this.lessonRoom = lessonRoom;
        this.lessonDetails = lessonDetails;
        this.fullData = fullData;
    }
}
