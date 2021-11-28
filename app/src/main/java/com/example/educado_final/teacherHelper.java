package com.example.educado_final;

public class teacherHelper {
    String emailAdd, password, name,userType;

    public teacherHelper() {
    }

    public teacherHelper(String emailAdd, String password, String name, String userType) {
        this.emailAdd = emailAdd;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
