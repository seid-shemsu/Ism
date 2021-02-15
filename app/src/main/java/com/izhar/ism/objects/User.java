package com.izhar.ism.objects;

import java.util.List;

public class User {
    String name;
    List<Request> requested, approved, declined;

    public User(String name, List<Request> requested, List<Request> approved, List<Request> declined) {
        this.name = name;
        this.requested = requested;
        this.approved = approved;
        this.declined = declined;
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
}
