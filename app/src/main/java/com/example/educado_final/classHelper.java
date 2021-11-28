package com.example.educado_final;

public class classHelper {

    Integer subjectCode;
    String subjectTitle;
    String subjectYear;
    String subjectSection;
    String subjectTeacher;
    String date;
    String time;

    public classHelper() {
    }

    public classHelper(Integer subjectCode, String subjectTitle, String subjectYear, String subjectSection, String subjectTeacher, String date, String time) {
        this.subjectCode = subjectCode;
        this.subjectTitle = subjectTitle;
        this.subjectYear = subjectYear;
        this.subjectSection = subjectSection;
        this.subjectTeacher = subjectTeacher;
        this.date = date;
        this.time = time;
    }

    public Integer getSubjectCode() {
        return Integer.valueOf(subjectCode);
    }

    public void setSubjectCode(Integer subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectYear() {
        return subjectYear;
    }

    public void setSubjectYear(String subjectYear) {
        this.subjectYear = subjectYear;
    }

    public String getSubjectSection() {
        return subjectSection;
    }

    public void setSubjectSection(String subjectSection) {
        this.subjectSection = subjectSection;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}