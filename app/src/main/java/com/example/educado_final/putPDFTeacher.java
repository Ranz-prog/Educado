package com.example.educado_final;

public class putPDFTeacher {
    String name,url,description,date,subTitle,periodic;

    public putPDFTeacher() {
    }

    public putPDFTeacher(String name, String url, String description, String date, String subTitle, String periodic) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.date = date;
        this.subTitle = subTitle;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPeriodic() {
        return periodic;
    }

    public void setPeriodic(String periodic) {
        this.periodic = periodic;
    }
}
