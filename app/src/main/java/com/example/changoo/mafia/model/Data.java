package com.example.changoo.mafia.model;

import java.io.Serializable;

/**
 * Created by changoo on 2016-12-01.
 */

public class Data implements Serializable {
    private String command;
    private String name;
    private Object object;

    public Data(String command, String name, Object object) {
        this.command = command;
        this.name = name;
        this.object = object;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
