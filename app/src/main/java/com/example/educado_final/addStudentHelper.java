package com.example.educado_final;

public class addStudentHelper {
    String studentNo,subjectTitle,subjectYear,subjectSection,time;
    Integer subjectCode;

    public addStudentHelper() {
    }

    public addStudentHelper(String studentNo, String subjectTitle, String subjectYear, String subjectSection, String time, Integer subjectCode) {
        this.studentNo = studentNo;
        this.subjectTitle = subjectTitle;
        this.subjectYear = subjectYear;
        this.subjectSection = subjectSection;
        this.time = time;
        this.subjectCode = subjectCode;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(Integer subjectCode) {
        this.subjectCode = subjectCode;
    }
}
