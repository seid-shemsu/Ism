package com.izhar.ism.objects;

public class Activity {
    String actor, date, id, name, time, type, food_id;

    public Activity(String actor, String date, String id, String name, String time, String type, String food_id) {
        this.actor = actor;
        this.date = date;
        this.id = id;
        this.name = name;
        this.time = time;
        this.type = type;
        this.food_id = food_id;
    }
    public Activity(){}
    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Activity(String id) {
        this.id = id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
