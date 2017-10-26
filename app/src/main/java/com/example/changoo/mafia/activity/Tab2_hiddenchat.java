package com.example.changoo.mafia.activity;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.changoo.mafia.Database.ChatDBOpenHelper;
import com.example.changoo.mafia.Database.HiddenChatDBOpenHelper;
import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.ChatCommand;
import com.example.changoo.mafia.command.Command;
import com.example.changoo.mafia.command.HiddenChatCommand;
import com.example.changoo.mafia.command.PlayCommand;
import com.example.changoo.mafia.model.Chat;
import com.example.changoo.mafia.model.ChatAdapter;
import com.example.changoo.mafia.model.HiddenChatAdapter;
import com.example.changoo.mafia.model.UserInfo;
import com.example.changoo.mafia.model.UserManager;

import java.util.ArrayList;
import java.util.Vector;

public class Tab2_hiddenchat extends NetworkFragment {

    private View rootView = null;
    private HiddenChatAdapter hiddenChatAdapter = null;
    private ArrayList<Chat> hiddentChats;
    private boolean imMafia = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserManager userManager = UserManager.getInstance();

        if (userManager.getUser(myName).getCharacter().equals("MAFIA"))
            imMafia = true;

        if (imMafia) {
            hiddentChats = new ArrayList<>();
            hiddenChatAdapter = new HiddenChatAdapter(getActivity(), hiddentChats, inflater);

            rootView = inflater.inflate(R.layout.tab2_hiddenchat, container, false);
            ListView lv_hiddenchats = (ListView) rootView.findViewById(R.id.lv_hiddenchats);
            lv_hiddenchats.setDivider(null);
            lv_hiddenchats.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            lv_hiddenchats.setAdapter(hiddenChatAdapter);

            TextView tv_mafia1 = (TextView) rootView.findViewById(R.id.tv_mafia1);
            TextView tv_mafia2 = (TextView) rootView.findViewById(R.id.tv_mafia2);
            TextView tv_mafia3 = (TextView) rootView.findViewById(R.id.tv_mafia3);

            String[] mafias = new String[]{"", "", ""};
            int num = 0;
            for (int i = 0; i < userManager.size(); i++)
                if (userManager.getUser(i).getCharacter().equals("MAFIA")) {
                    mafias[num] = userManager.getUser(i).getName();
                    num++;
                }

            tv_mafia1.setText(mafias[0]);
            tv_mafia2.setText(mafias[1]);
            tv_mafia3.setText(mafias[2]);

            setBtn_send_hidden();
            setBtn_Emoticon();
            setBtn_showEmoticons();

        } else {
            rootView = inflater.inflate(R.layout.tab2_hiddenchat_no, container, false);
        }

        return rootView;
    }

    public void setBtn_send_hidden() {
        final EditText et_text_hidden = (EditText) rootView.findViewById(R.id.et_text_hidden);
        Button btn_send_hiddenchat = (Button) rootView.findViewById(R.id.btn_send_hidden);
        btn_send_hiddenchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et_text_hidden.getText().toString();
                if (text.length() != 0)
                    sendMsg(HiddenChatCommand.SENDMESSAGE, myName, text);
                et_text_hidden.setText("");
            }
        });
    }

    private void setBtn_Emoticon() {
        ImageButton[] btn_emoticons = new ImageButton[8];
        btn_emoticons[0] = (ImageButton) rootView.findViewById(R.id.btn_emoticon1_hidden);
        btn_emoticons[1] = (ImageButton) rootView.findViewById(R.id.btn_emoticon2_hidden);
        btn_emoticons[2] = (ImageButton) rootView.findViewById(R.id.btn_emoticon3_hidden);
        btn_emoticons[3] = (ImageButton) rootView.findViewById(R.id.btn_emoticon4_hidden);
        btn_emoticons[4] = (ImageButton) rootView.findViewById(R.id.btn_emoticon5_hidden);
        btn_emoticons[5] = (ImageButton) rootView.findViewById(R.id.btn_emoticon6_hidden);
        btn_emoticons[6] = (ImageButton) rootView.findViewById(R.id.btn_emoticon7_hidden);
        btn_emoticons[7] = (ImageButton) rootView.findViewById(R.id.btn_emoticon8_hidden);


        ImageButton.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_emoticon1_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon1);
                        break;
                    case R.id.btn_emoticon2_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon2);
                        break;
                    case R.id.btn_emoticon3_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon3);
                        break;
                    case R.id.btn_emoticon4_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon4);
                        break;
                    case R.id.btn_emoticon5_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon5);
                        break;
                    case R.id.btn_emoticon6_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon6);
                        break;
                    case R.id.btn_emoticon7_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon7);
                        break;
                    case R.id.btn_emoticon8_hidden:
                        sendMsg(HiddenChatCommand.SENDEMOTICON, myName, R.drawable.emoticon8);
                        break;
                }
                return true;
            }
        };

        for (int i = 0; i < 8; i++)
            btn_emoticons[i].setOnLongClickListener(onLongClickListener);
    }

    private void setBtn_showEmoticons() {
        EditText et_text_hidden = (EditText) rootView.findViewById(R.id.et_text_hidden);
        final Button btn_showemoticons_hidden = (Button) rootView.findViewById(R.id.btn_showemoticons_hidden);
        final TableLayout layout_emoticons_hidden = (TableLayout) rootView.findViewById(R.id.layout_emoticons_hidden);

        btn_showemoticons_hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_emoticons_hidden.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn_showemoticons_hidden.getWindowToken(), 0);
            }
        });

        et_text_hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_emoticons_hidden.setVisibility(View.GONE);
            }
        });
    }

    private void setDied() {
        Button btn_send_hidden = (Button) rootView.findViewById(R.id.btn_send_hidden);
        btn_send_hidden.setEnabled(false);
        btn_send_hidden.setClickable(false);
        Button btn_showemoticons_hidden = (Button) rootView.findViewById(R.id.btn_showemoticons_hidden);
        btn_showemoticons_hidden.setEnabled(false);
        btn_showemoticons_hidden.setClickable(false);
    }

    @Override
    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
        super.recvMsg(recv_command, recv_name, recv_object);
        Chat chat = null;
        ContentValues values=null;

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
        }

        if (imMafia)
            switch (recv_command) {
                case PlayCommand.YOUAREDIE:
                    setDied();
                    break;

                case HiddenChatCommand.SENDMESSAGE:
                    Log.i("ChatCommand.SENDMESSAGE", "tab1");
                    if (recv_name.equals(myName)) //me
                        chat = new Chat(recv_name, (String) recv_object, 1);
                    else //anothor
                        chat = new Chat(recv_name, (String) recv_object, 0);
                    hiddenChatAdapter.add(chat);
                    break;

                case HiddenChatCommand.SENDEMOTICON:
                    if (recv_name.equals(myName)) //me
                        chat = new Chat(recv_name, (int) recv_object, 1);
                    else //anothor
                        chat = new Chat(recv_name, (int) recv_object, 0);
                    hiddenChatAdapter.add(chat);
                    break;
            }


    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isOnCreate && imMafia) {

            if (userManager.getUser(myName).getState().equals("die"))
                setDied();

            /*모든 hiddenchats.db에 내용을 읽어옴, 다른 frgment에서 저장된 */
            Cursor cursor = hiddenChatDB.query("hiddenchats", new String[]{"name", "emoticonID", "text", "type"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                Integer emoticonID = cursor.getInt(1);
                String text = cursor.getString(2);
                Integer type = cursor.getInt(3);

                Chat chat = new Chat(name, emoticonID, text, type);
                hiddenChatAdapter.add(chat);
            }

            /*모든 hiddenchat.db에 내용을 삭제*/
            hiddenChatDB.delete("hiddenchats", null, null);

            hiddenChatAdapter.notifyDataSetChanged();
        }
    }


}
