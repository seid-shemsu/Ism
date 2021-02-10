package com.izhar.ism.objects;

import java.util.List;

public class History {
    List<Food> foods;
    String total, dateTime;

    public History(List<Food> foods, String total, String dateTime) {
        this.foods = foods;
        this.total = total;
        this.dateTime = dateTime;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
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
}
