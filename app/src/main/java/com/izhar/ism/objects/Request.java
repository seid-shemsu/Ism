package com.izhar.ism.objects;


import java.util.List;
import java.util.Map;

public class Request {
    List<Food> foods;
    String id, total, dateTime, name;

    public Request() {
    }

    public Request(List<Food> foods, String total, String dateTime, String name) {
        this.foods = foods;
        this.total = total;
        this.dateTime = dateTime;
        this.name = name;
    }

    public Request(List<Food> foods, String total, String dateTime) {
        this.foods = foods;
        this.total = total;
        this.dateTime = dateTime;
    }

    public Request(String id, String total, String dateTime, String name) {
        this.id = id;
        this.total = total;
        this.dateTime = dateTime;
        this.name = name;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
