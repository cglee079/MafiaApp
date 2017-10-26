package com.example.changoo.mafia.model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.changoo.mafia.R;

import java.util.ArrayList;


public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Chat> chats;
    private LayoutInflater inflater;
    private Handler mHandler;

    public ChatAdapter(Context mContext, ArrayList<Chat> chats, LayoutInflater inflater) {
        this.mContext = mContext;
        this.chats = chats;
        this.inflater = inflater;
        this.mHandler = new Handler();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).getType();
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int position) {
        return chats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(Chat chat) {
        chats.add(chat);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View item = convertView;
        int type = getItemViewType(position);


            switch (type) {
                case 0: //other
                    item = inflater.inflate(R.layout.chatitem_layout, null);
                    break;
                case 1: //me
                    item = inflater.inflate(R.layout.chatitem_my_layout, null);
                    break;
                case 2: //notice
                    item = inflater.inflate(R.layout.chatitem_notice_layout, null);
                    break;
                case 3: //important notice
                    item = inflater.inflate(R.layout.chatitem_importantnotice_layout, null);
                    break;
            }

        switch (type) {
            case 0:
                TextView tv_username_msgitem = (TextView) item.findViewById(R.id.tv_username_msgitem);
                TextView tv_message = (TextView) item.findViewById(R.id.tv_message);
                ImageView img_icon_msgitem = (ImageView) item.findViewById(R.id.img_icon_mychrater);
                ImageView img_emoticon=(ImageView)item.findViewById(R.id.img_emoticon);
                img_emoticon.setVisibility(View.GONE);

                if(chats.get(position).getEmoticonID()!=-1){
                    img_emoticon.setVisibility(View.VISIBLE);
                    tv_message.setVisibility(View.GONE);
                    Integer emoticon_resID=chats.get(position).getEmoticonID();
                    img_emoticon.setImageResource(emoticon_resID);
                }
                else{
                    tv_message.setText(chats.get(position).getText());
                }

                if (position > 0)
                    if (chats.get(position - 1).getName().equals(chats.get(position).getName())){
                        Log.i("[positon]",position-1+" "+chats.get(position - 1).getName()+"  "+chats.get(position - 1).getText());
                        Log.i("[positon]",position+" "+chats.get(position).getName() +"   "+chats.get(position).getText());
                        img_icon_msgitem.setVisibility(View.GONE);
                        tv_username_msgitem.setVisibility(View.GONE);
                    }

                tv_username_msgitem.setText(chats.get(position).getName());

                break;

            case 1://me
                TextView tv_message_my = (TextView) item.findViewById(R.id.tv_message_my);
                ImageView img_emotion_my=(ImageView)item.findViewById(R.id.img_emotion_my);
                if(chats.get(position).getEmoticonID()!=-1){
                    img_emotion_my.setVisibility(View.VISIBLE);
                    tv_message_my.setVisibility(View.GONE);
                    Integer emoticon_resID=chats.get(position).getEmoticonID();
                    img_emotion_my.setImageResource(emoticon_resID);
                }
                else {
                    img_emotion_my.setVisibility(View.GONE);
                    tv_message_my.setText(chats.get(position).getText());
                }

                break;

            case 2: //notice
                TextView tv_notice_message = (TextView) item.findViewById(R.id.tv_notice_message);
                tv_notice_message.setText(chats.get(position).getText());
                break;

            case 3: //impotantnotice
                TextView tv_impotantnotice_message = (TextView) item.findViewById(R.id.tv_impotantnotice_message);
                tv_impotantnotice_message.setText(chats.get(position).getText());
                break;

        }
        return item;
    }
}
