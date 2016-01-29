package com.cloudeducate.redtick.Utils;

/**
 * Created by yogesh on 28/1/16.
 */
public class URL {
    public static String getTeacherLoginURL()
    {
        String url="auth/login.json";
        return Constants.BASE_URL + url;
    }
    public static String getAttendanceURL()
    {
        String url1="teacher/manageAttendance.json";
        return Constants.BASE_URL + url1;
    }

}
