package com.cloudeducate.redtick.Model;

/**
 * Created by abhishek on 29-01-2016.
 */
public class Attendance_model {
    private String name;
    private String user_id;
    private String roll_no;
    int attendance;
    public String getstudentname(){ return name;}

    public void setstudentname(String name){ this.name=name;}

    public String getuserid(){ return user_id;}

    public void setuserid(String user_id){ this.user_id=user_id;}

    public String getrollno(){ return roll_no;}

    public void setrollno(String roll_no){ this.roll_no=roll_no;}

    public int getAttendancevalue(){return attendance;}

    public void setAttendancevalue(int value){this.attendance=value;}
}
