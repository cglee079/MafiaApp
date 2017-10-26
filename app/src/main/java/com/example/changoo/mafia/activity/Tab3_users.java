package com.example.changoo.mafia.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.changoo.mafia.Database.ChatDBOpenHelper;
import com.example.changoo.mafia.Database.HiddenChatDBOpenHelper;
import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.ChatCommand;
import com.example.changoo.mafia.command.Command;
import com.example.changoo.mafia.command.HiddenChatCommand;
import com.example.changoo.mafia.command.PlayCommand;
import com.example.changoo.mafia.model.UserInfo;
import com.example.changoo.mafia.model.UserManager;
import com.example.changoo.mafia.model.UsersAdapter;

import java.util.Vector;

public class Tab3_users extends NetworkFragment {
    private UsersAdapter usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_users, container, false);

        ListView lv_users = (ListView) rootView.findViewById(R.id.lv_users);
        usersAdapter = new UsersAdapter(getContext(),myName);
        lv_users.setAdapter(usersAdapter);
        lv_users.setDivider(null);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            usersAdapter.notifyDataSetChanged();
        } else {

        }
    }

    @Override
    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
        super.recvMsg(recv_command, recv_name, recv_object);
        ContentValues values;
        switch (recv_command) {
            case PlayCommand.NOTICE:
                values = new ContentValues();
                values.put("name", recv_name);
                values.put("emoticonID", -1);
                values.put("text", (String) recv_object);
                values.put("type",2);
                chatDB.insert("chats", null, values);
                break;

            case PlayCommand.IMPORTANTNOTICE:
                values = new ContentValues();
                values.put("name", recv_name);
                values.put("emoticonID", -1);
                values.put("text", (String) recv_object);
                values.put("type",3);
                chatDB.insert("chats", null, values);
                break;

            case ChatCommand.SENDMESSAGE:
                values = new ContentValues();
                values.put("name", recv_name);
                values.put("emoticonID", -1);
                values.put("text", (String) recv_object);
                if (recv_name.equals(myName))
                    values.put("type", 1);
                else
                    values.put("type", 0);

                chatDB.insert("chats", null, values);
                break;

            case ChatCommand.SENDEMOTICON:
                values = new ContentValues();
                values.put("name", recv_name);
                values.put("emoticonID", (int) recv_object);
                values.put("text", "");
                if (recv_name.equals(myName))
                    values.put("type", 1);
                else
                    values.put("type", 0);

                chatDB.insert("chats", null, values);
                break;

            case HiddenChatCommand.SENDMESSAGE:
                values = new ContentValues();
                values.put("name", recv_name);
                values.put("emoticonID", -1);
                values.put("text", (String) recv_object);

                if (recv_name.equals(myName))
                    values.put("type", 1);
                else
                    values.put("type", 0);

                hiddenChatDB.insert("hiddenchats", null, values);
                break;

            case HiddenChatCommand.SENDEMOTICON:
                values = new ContentValues();
                values.put("name", recv_name);
                values.put("emoticonID", (int) recv_object);
                values.put("text", "");

                if (recv_name.equals(myName))
                    values.put("type", 1);
                else
                    values.put("type", 0);

                hiddenChatDB.insert("hiddenchats", null, values);
                break;
        }
    }
}
