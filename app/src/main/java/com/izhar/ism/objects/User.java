package com.izhar.ism.objects;

import java.util.List;

public class User {
    String name;
    List<Request> requested, approved, declined;
    String total_requested, total_approved, total_declined;

    public User(String name, List<Request> requested, List<Request> approved, List<Request> declined, String total_requested, String total_approved, String total_declined) {
        this.name = name;
        this.requested = requested;
        this.approved = approved;
        this.declined = declined;
        this.total_requested = total_requested;
        this.total_approved = total_approved;
        this.total_declined = total_declined;
    }

    public User(String name, List<Request> requested, List<Request> approved, List<Request> declined) {
        this.name = name;
        this.requested = requested;
        this.approved = approved;
        this.declined = declined;
    }

    public String getTotal_requested() {
        return total_requested;
    }

    public void setTotal_requested(String total_requested) {
        this.total_requested = total_requested;
    }

    public String getTotal_approved() {
        return total_approved;
    }

    public void setTotal_approved(String total_approved) {
        this.total_approved = total_approved;
    }

    public String getTotal_declined() {
        return total_declined;
    }

    public void setTotal_declined(String total_declined) {
        this.total_declined = total_declined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Request> getRequested() {
        return requested;
    }

    public void setRequested(List<Request> requested) {
        this.requested = requested;
    }

    public List<Request> getApproved() {
        return approved;
    }

    public void setApproved(List<Request> approved) {
        this.approved = approved;
    }

    public List<Request> getDeclined() {
        return declined;
    }

    public void setDeclined(List<Request> declined) {
        this.declined = declined;
    }

    String type, password, username;

    public User(String name, String type, String password, String username) {
        this.name = name;
        this.type = type;
        this.password = password;
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
