package jyun0.taskmanager;

import java.io.Serializable;

public class Item implements Serializable{

    private String title;
    private String date;

    public Item(){}

    public Item(String date, String title) {
        this.date = date;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

