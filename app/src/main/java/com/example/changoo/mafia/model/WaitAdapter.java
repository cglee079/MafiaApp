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

/**
 * Created by changoo on 2016-11-17.
 */

public class WaitAdapter extends BaseAdapter {
    private Vector<UserInfo> userinfos;
    private LayoutInflater inflater;
    private Context mcontext;
    private String myName;

    public WaitAdapter(Context mcontext, String myName) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.userinfos = UserManager.getInstance().getUserinfos();
        this.myName=myName;
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
            item = inflater.inflate(R.layout.waititem_layout, null);
        TextView tv_username_wait = (TextView) item.findViewById(R.id.tv_username_wait);
        TextView tv_ready = (TextView) item.findViewById(R.id.tv_ready);

        String username=userinfos.get(position).getName();
        String state=userinfos.get(position).getState();
        tv_username_wait.setText(username);

        if (state.equals("ready"))
            tv_ready.setText("READY!!!");
        else if (state.equals("wait"))
            tv_ready.setText("WAIT");

        if(myName.equals(username)){
            tv_ready.setTextColor(Color.BLUE);
            tv_username_wait.setTextColor(Color.BLUE);
        }
        return item;
    }
}
