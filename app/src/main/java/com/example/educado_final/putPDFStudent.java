package com.example.educado_final;

public class putPDFStudent {
    String name, url,passedDate,status,periodic;

    public putPDFStudent() {
    }

    public putPDFStudent(String name, String url, String passedDate, String status, String periodic) {
        this.name = name;
        this.url = url;
        this.passedDate = passedDate;
        this.status = status;
        this.periodic = periodic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassedDate() {
        return passedDate;
    }

    public void setPassedDate(String passedDate) {
        this.passedDate = passedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeriodic() {
        return periodic;
    }

    public void setPeriodic(String periodic) {
        this.periodic = periodic;
    }
}
