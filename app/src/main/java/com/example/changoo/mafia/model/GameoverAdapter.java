package com.example.changoo.mafia.model;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.changoo.mafia.R;

import java.util.Vector;

/**
 * Created by changoo on 2016-12-11.
 */

public class GameoverAdapter extends BaseAdapter {
    private Vector<UserInfo> userinfos;
    private LayoutInflater inflater;
    private Context mcontext;
    private String myName;

    public GameoverAdapter(Context mcontext,String myName) {
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
            item = inflater.inflate(R.layout.gameoveritem_layout, null);

        TextView tv_username_gameover = (TextView) item.findViewById(R.id.tv_username_gameover);
        TextView tv_character_gameover = (TextView) item.findViewById(R.id.tv_character_gameover);
        TextView tv_state_gameover = (TextView) item.findViewById(R.id.tv_state_gameover);
        ImageView img_character_gameover = (ImageView) item.findViewById(R.id.img_character_gameover);

        String username = userinfos.get(position).getName();
        String character = userinfos.get(position).getCharacter();
        String state = userinfos.get(position).getState();

        tv_username_gameover.setText(username);
        tv_character_gameover.setText(character);
        tv_state_gameover.setText(state);

        if (state.equals("die")) {
            tv_username_gameover.setTextColor(Color.GRAY);
            tv_character_gameover.setTextColor(Color.GRAY);
            tv_state_gameover.setTextColor(Color.GRAY);
        }

        if(username.equals(myName)){
            tv_username_gameover.setTextColor(Color.BLUE);
            tv_character_gameover.setTextColor(Color.BLUE);
            tv_state_gameover.setTextColor(Color.BLUE);
        }

        switch (userinfos.get(position).getCharacter()) {
            case "MAFIA":
                img_character_gameover.setImageResource(R.drawable.icon_mafia);
                break;
            case "COP":
                img_character_gameover.setImageResource(R.drawable.icon_cop);
                break;
            case "DOCTOR":
                img_character_gameover.setImageResource(R.drawable.icon_doctor);
                break;
            case "CIVIL":
                img_character_gameover.setImageResource(R.drawable.lcon_civil);
                break;
        }

        return item;
    }
}
