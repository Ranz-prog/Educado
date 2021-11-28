package com.example.educado_final;

public class addStudentsJoined {
    String name, studentNo;

    public addStudentsJoined() {
    }

    public addStudentsJoined(String name, String studentNo) {
        this.name = name;
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
}
