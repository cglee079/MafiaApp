package com.example.changoo.mafia.model;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.changoo.mafia.R;

import java.util.Vector;


public class UsersAdapter extends BaseAdapter {
    Vector<UserInfo> userinfos;
    LayoutInflater inflater;
    Context mcontext;
    String myName;

    public UsersAdapter(Context mcontext, String myName) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.userinfos = UserManager.getInstance().getUserinfos();
        this.myName = myName;
    }


    @Override
    public int getCount() {
        return userinfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userinfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;

        if (item == null)
            item = inflater.inflate(R.layout.usersitem_layout, null);

        TextView tv_username_users = (TextView) item.findViewById(R.id.tv_username_users);
        TextView tv_state_users = (TextView) item.findViewById(R.id.tv_state_users);

        String username = userinfos.get(position).getName();
        String state=userinfos.get(position).getState();

        if (state.equals("die")) {
            tv_username_users.setTextColor(Color.GRAY);
            tv_state_users.setTextColor(Color.GRAY);
        }

        if (username.equals(myName)) {
            tv_username_users.setTextColor(Color.BLUE);
            tv_state_users.setTextColor(Color.BLUE);
        }

        tv_username_users.setText(username);
        tv_state_users.setText(state);

        return item;
    }
}
