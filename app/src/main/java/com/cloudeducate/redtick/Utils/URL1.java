package com.cloudeducate.redtick.Utils;

/**
 * Created by yogesh on 28/1/16.
 */
public class URL1 {
    public static String getTeacherLoginURL()
    {
        String url="auth/login.json";
        return Constants.BASE_URL + url;
    }
    public static String getAttendanceURL() {
        String url1= "teacher/manageAttendance.json";
        return Constants.BASE_URL + url1;
    }
    public static String getCoursesURL() {
        String url2 = "teacher/courses.json";
        return Constants.BASE_URL + url2;
    }
    public static String getAssignmentURL(String course_id) {
        String url3 = "assignments/manage/";
        return Constants.BASE_URL + url3+course_id+".json";
    }
    public static String getsubmitlistURL(String assignment_id) {
        String url4 = "assignments/submissions/";
        return Constants.BASE_URL + url4+assignment_id+".json";
    }
    public static String getsubmitgradeURL(String assignment_id) {
        String url5 = "assignments/gradeIt/";
        return Constants.BASE_URL + url5+assignment_id+".json";
    }

    public static String getperformanceURL(String course_id,String classroom_id) {
        String url1 = "teacher/weeklyStudentsPerf/";
        return Constants.BASE_URL + url1+course_id+"/"+classroom_id+".json";
    }
}
