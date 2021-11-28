package com.example.educado_final;

public class studentHelper {

    String emailAdd, Name,password,studentNo,userType ;

    public studentHelper() {
    }

    public studentHelper(String emailAdd, String name, String password, String studentNo, String userType) {
        this.emailAdd = emailAdd;
        Name = name;
        this.password = password;
        this.studentNo = studentNo;
        this.userType = userType;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
