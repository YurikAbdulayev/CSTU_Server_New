package com.cstu.service;

import com.cstu.model.json.objects.ScheduleDay;
import com.cstu.model.json.objects.ScheduleInfo;
import com.cstu.model.json.objects.ScheduleLesson;
import com.cstu.utils.C;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParserServiceImpl implements ParserService {
//Jsoup.connect(url).requestBody("group=%CC%CF%C7-1704&sdate=01.09.2017&edate=29.10.2017&n=700").post()

    @Override
    public String getSchedule(String group, String sdate, String edate) {
        String url = "http://195.95.232.162:8082/cgi-bin/timetable.cgi";
        String test = null;
        try {
            test = "group=" + C.toWin1251(URLDecoder.decode(group, "UTF-8"))            +//CC%CF%C7-1704
                    "&sdate=" + sdate           +//01.09.2017
                    "&edate=" + edate           +//29.10.2017
                    "&n=700";

            List<ScheduleDay> scheduleDays = new ArrayList<>();
            Document doc = Jsoup
                    .connect(url)
                    .requestBody(test)
                    .post();
            Elements days = doc.getElementsByClass("col-md-6");
            for (Element dayInfo : days) {
                try {
                    scheduleDays.add(getScheduleDay(dayInfo));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new Gson().toJson(scheduleDays)
                    .replace("\u0027", "'");
        }catch (UnsupportedEncodingException e){
            return "Unsupported group";
        }catch (IOException e) {
            e.printStackTrace();
        }

        return "[]";
    }

    private ScheduleDay getScheduleDay(Element dayInfo) {
        Element lessonsTable = dayInfo.getElementsByTag("table").first();
        String date = dayInfo.getElementsByTag("h4").first().text().split(" ", 2)[0];
        String dayName = dayInfo.getElementsByTag("small").first().text();
        return new ScheduleDay(date, dayName, getAllLessons(lessonsTable)
        );
    }

    private List<ScheduleLesson> getAllLessons(Element lessonsTable) {
        List<ScheduleLesson> scheduleData = new ArrayList<>();
        for (Element lesson : lessonsTable.getElementsByTag("tr")) {
            Elements td = lesson.getElementsByTag("td");
            String lessonDetails = td.get(2).text();
            if (!lessonDetails.isEmpty()) {
                String lessonIndex = td.get(0).text();
                String lessonTime = td.get(1).text().replace(" ", "-");
                scheduleData.add(
                        new ScheduleLesson(lessonIndex,lessonTime,
                                getLessonDetails(lessonDetails)
                        ));
            }
        }
        return scheduleData;
    }

    private ScheduleInfo getLessonDetails(String details) {
        boolean gr1 = details.toLowerCase().contains("(підгр. 1)");
        boolean gr2 = details.toLowerCase().contains("(підгр. 2)");
        if (gr1 && gr2) {
            return new ScheduleInfo("",
                    "",
                    "",
                    "",
                    "",
                    details);
        } else {
            return parseLessonDetails(details);
        }
    }

    private ScheduleInfo parseLessonDetails(String lesson) {
        boolean checked = false;
        String lessonStr = lesson;
        String lessonType = null;
        for (String type : C.lessonTypes) {
            if (lesson.contains(type) && !checked) {
                lessonType = type;
                lesson = lesson.replace(type, "").trim();
                checked = true;
            }
        }
        String roomNumberWithDetails = lesson.substring(lesson.indexOf(" а.") + 1).trim();
        String lessonRoom;
        String anotherDetails;
        if (roomNumberWithDetails.contains("Спорткомплекс")) {
            lessonRoom = roomNumberWithDetails
                    .substring(0, roomNumberWithDetails.indexOf("кс") + 2).trim();
            anotherDetails = getAnotherDetails(roomNumberWithDetails, lessonRoom);
        } else {
            lessonRoom = getLessonRoom(roomNumberWithDetails + " ");
            anotherDetails = getAnotherDetails(roomNumberWithDetails, lessonRoom);
        }

        String lessonNameWithTeacher = lesson.replace(roomNumberWithDetails, "").trim();

        String lessonName = null;
        String teacher = null;
        checked = false;
        for (String rang : C.teacherRangs) {
            if (lessonNameWithTeacher.contains(rang) && !checked) {
                Pattern p = Pattern.compile("([^\\s]+)[ ]\\W[.]\\W[.]");
                Matcher m = p.matcher(lessonNameWithTeacher);
                if (m.find()) {
                    int position = m.start();
                    teacher = lessonNameWithTeacher.substring(position).trim();

                } else {
                    teacher = lessonNameWithTeacher.substring(lessonNameWithTeacher.indexOf(rang)).trim();
                }

                lessonName = lessonNameWithTeacher.replace(teacher, "").trim();
                checked = true;
            }
        }

        return new ScheduleInfo(
                lessonType,
                lessonName,
                teacher,
                lessonRoom,
                anotherDetails,
                lessonStr
        );
    }

    private String getAnotherDetails(String roomNumberWithDetails, String lessonRoom) {
        return Objects.equals(lessonRoom, roomNumberWithDetails) ? "" :
                roomNumberWithDetails.replace(lessonRoom, "").trim();
    }

    private String getLessonRoom(String roomNumberWithDetails) {
        String pattern = "\\d\\s";
        int endIndex = 0;
        Matcher matcher = Pattern.compile(pattern).matcher(roomNumberWithDetails);
        if(matcher.find()){
            endIndex = matcher.end();
        }
        return roomNumberWithDetails.substring(0, endIndex).trim();
    }
}
