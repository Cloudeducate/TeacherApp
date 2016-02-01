package com.cloudeducate.redtick.Model;

/**
 * Created by yogesh on 22/1/16.
 */
public class Assignment_model {

    private String title;
    private String description;
    private String deadline;
    private String id;
    private String course;
    private Boolean submitted;
    private String filename;


    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The deadline
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     *
     * @param deadline
     * The deadline
     */
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The course
     */
    public String getCourse() {
        return course;
    }

    /**
     *
     * @param course
     * The course
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     *
     * @return
     * The submitted
     */
    public Boolean getSubmitted() {
        return submitted;
    }

    /**
     *
     * @param submitted
     * The submitted
     */
    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    /**
     *
     * @return
     * The filename
     */
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }



}