package com.eventControlModel;

import java.util.HashMap;

public class Event {


    public EventEnum id;

    public String path;


    public HashMap<EventEnum, Object> params = new HashMap<EventEnum, Object>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public EventEnum getId() {
        return id;
    }

    public void setId(EventEnum id) {
        this.id = id;
    }

    public HashMap<EventEnum, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<EventEnum, Object> params) {
        this.params = params;
    }
}
