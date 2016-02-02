package com.cloudeducate.redtick.Model;

/**
 * Created by yogesh on 22/1/16.
 */
public class Assignment_model {

    private String title;
    private String deadline;
    private String id;
    private String course;
    private String classroom;
    private String course_id;
    private String classroom_id;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourseid() {
        return course_id;
    }
    public void setCourseid(String courseid) {
        this.course_id = courseid;
    }

    public String getClassroomid() {
        return classroom_id;
    }
    public void setClassroomid(String classid) {
        this.classroom_id = classid;
    }

    public String getClassroom() {
        return classroom;
    }
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }




}