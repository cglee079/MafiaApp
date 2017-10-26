package com.example.changoo.mafia.model;

import java.io.Serializable;

public class Chat implements Serializable{
    private String  name="";
    private int emoticonID=-1;
    private String text="";
    private int type;

    public Chat(String name, String text, int type) {
        this.name = name;
        this.text = text;
        this.type = type;
    }

    public Chat(String name, Integer emoticonID, int type) {
        this.name = name;
        this.emoticonID = emoticonID;
        this.type = type;
    }

    public Chat(String name, Integer emoticonID, String text, int type) {
        this.name = name;
        this.emoticonID = emoticonID;
        this.text = text;
        this.type = type;
    }

    public Integer getEmoticonID() {
        return emoticonID;
    }

    public void setEmoticonID(Integer emoticonID) {
        this.emoticonID = emoticonID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
