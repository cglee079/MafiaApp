package com.example.changoo.mafia.model;


import java.util.ArrayList;
import java.util.Vector;

public class UserManager {
    Vector<UserInfo> userinfos = new Vector<>();

    private static final UserManager instance = new UserManager();

    public static UserManager getInstance() {
        return instance;
    }

    public void addUser(UserInfo userinfo) {
        userinfos.add(userinfo);
    }

    public boolean checkingName(UserInfo userinfo) {
        for (int i = 0; i < userinfos.size(); i++) {
            UserInfo u = userinfos.get(i);
            if (userinfo.getName().equals(u.getName()) == true)
                return false;
        }
        return true;
    }

    public int size() {
        return userinfos.size();
    }

    public UserInfo getUser(int i) {
        return userinfos.get(i);
    }

    public void clear() {
        userinfos.clear();
    }

    public UserInfo getUser(String username) {
        for (int i = 0; i < userinfos.size(); i++) {
            if (username.equals(userinfos.get(i).getName()))
                return userinfos.get(i);
        }
        return null;
    }


    public String[] getUserNames (){
        int size= userinfos.size();
        String[] names= new String[size];
        for(int i=0; i<size; i++){
            names[i]=userinfos.get(i).getName();
        }
        return  names;
    }

    public String[] getAliveUserNames (){
        ArrayList<String> usernames=new ArrayList<>();
        int size= userinfos.size();
        for(int i=0; i<size; i++){
            if(userinfos.get(i).getState().equals("play"))
                usernames.add(userinfos.get(i).getName());
        }
        String[] names=usernames.toArray((new String[usernames.size()]));
        return  names;
    }

    public void addAllUser(Vector<UserInfo> recv_userinfos) {
        this.userinfos.addAll(recv_userinfos);
    }

    public Vector<UserInfo> getUserinfos() {
        return userinfos;
    }

}
