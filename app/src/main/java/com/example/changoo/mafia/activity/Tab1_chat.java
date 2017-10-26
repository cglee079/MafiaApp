package com.example.changoo.mafia.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.changoo.mafia.Database.HiddenChatDBOpenHelper;
import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.ChatCommand;
import com.example.changoo.mafia.command.HiddenChatCommand;
import com.example.changoo.mafia.command.PlayCommand;
import com.example.changoo.mafia.model.Chat;
import com.example.changoo.mafia.model.ChatAdapter;
import com.example.changoo.mafia.model.Data;

import java.util.ArrayList;

public class Tab1_chat extends NetworkFragment {
    private ArrayList<Chat> chats;
    private ChatAdapter chatAdapter;
    private View rootView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();

                Data data = (Data) bundle.getSerializable("data");
                String recv_command = data.getCommand();
                String recv_name = data.getName();
                Object recv_object = data.getObject();

                recvMsg(recv_command, recv_name, recv_object);
            }

        };
        this.startListen();

        sendMsg(PlayCommand.IMSTARTGAME, myName, "play");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.tab1_chat, container, false);

        chats = new ArrayList<>();

        chatAdapter = new ChatAdapter(getActivity(), chats, inflater);

        ListView lv_chat = (ListView) rootView.findViewById(R.id.lv_chats);
        lv_chat.setDivider(null);
        lv_chat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lv_chat.setAdapter(chatAdapter);

        setBtn_send();
        setBtn_Emoticon();
        setBtn_showEmoticons();
        return rootView;
    }

    @Override
    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
        super.recvMsg(recv_command, recv_name, recv_object);
        Chat chat = null;
        ContentValues values = null;

        switch (recv_command) {
            case PlayCommand.NOTICE:
                chat = new Chat(recv_name, (String) recv_object, 2);
                chatAdapter.add(chat);
                break;

            case PlayCommand.IMPORTANTNOTICE:
                chat = new Chat(recv_name, (String) recv_object, 3);
                chatAdapter.add(chat);
                break;

            case PlayCommand.YOUAREDIE:
                setDied();
                break;

            case ChatCommand.SENDMESSAGE:
                Log.i("ChatCommand.SENDMESSAGE", "tab1");
                if (recv_name.equals(myName)) //me
                    chat = new Chat(recv_name, (String) recv_object, 1);
                else //anothor
                    chat = new Chat(recv_name, (String) recv_object, 0);
                chatAdapter.add(chat);
                break;

            case ChatCommand.SENDEMOTICON:
                if (recv_name.equals(myName)) //me
                    chat = new Chat(recv_name, (Integer) recv_object, 1);
                else //anothor
                    chat = new Chat(recv_name, (Integer) recv_object, 0);
                chatAdapter.add(chat);
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

    public void setBtn_send() {
        final EditText et_text = (EditText) rootView.findViewById(R.id.et_text);
        Button btn_send = (Button) rootView.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et_text.getText().toString();
                if (text.length() != 0)
                    sendMsg(ChatCommand.SENDMESSAGE, myName, text);
                et_text.setText("");
            }
        });
    }

    private void setBtn_Emoticon() {
        ImageButton[] btn_emoticons = new ImageButton[8];
        btn_emoticons[0] = (ImageButton) rootView.findViewById(R.id.btn_emoticon1);
        btn_emoticons[1] = (ImageButton) rootView.findViewById(R.id.btn_emoticon2);
        btn_emoticons[2] = (ImageButton) rootView.findViewById(R.id.btn_emoticon3);
        btn_emoticons[3] = (ImageButton) rootView.findViewById(R.id.btn_emoticon4);
        btn_emoticons[4] = (ImageButton) rootView.findViewById(R.id.btn_emoticon5);
        btn_emoticons[5] = (ImageButton) rootView.findViewById(R.id.btn_emoticon6);
        btn_emoticons[6] = (ImageButton) rootView.findViewById(R.id.btn_emoticon7);
        btn_emoticons[7] = (ImageButton) rootView.findViewById(R.id.btn_emoticon8);


        ImageButton.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_emoticon1:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon1);
                        break;
                    case R.id.btn_emoticon2:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon2);
                        break;
                    case R.id.btn_emoticon3:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon3);
                        break;
                    case R.id.btn_emoticon4:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon4);
                        break;
                    case R.id.btn_emoticon5:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon5);
                        break;
                    case R.id.btn_emoticon6:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon6);
                        break;
                    case R.id.btn_emoticon7:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon7);
                        break;
                    case R.id.btn_emoticon8:
                        sendMsg(ChatCommand.SENDEMOTICON, myName, R.drawable.emoticon8);
                        break;
                }
                return true;
            }
        };

        for (int i = 0; i < 8; i++)
            btn_emoticons[i].setOnLongClickListener(onLongClickListener);
    }

    private void setBtn_showEmoticons() {
        EditText et_text = (EditText) rootView.findViewById(R.id.et_text);
        final Button btn_showemoticons = (Button) rootView.findViewById(R.id.btn_showemoticons);
        final TableLayout layout_emotions = (TableLayout) rootView.findViewById(R.id.layout_emoticons);

        btn_showemoticons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_emotions.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn_showemoticons.getWindowToken(), 0);
            }
        });
        et_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_emotions.setVisibility(View.GONE);
            }
        });
    }

    private void setDied() {
        Button btn_send = (Button) rootView.findViewById(R.id.btn_send);
        btn_send.setEnabled(false);
        btn_send.setClickable(false);
        Button btn_showEmoticons = (Button) rootView.findViewById(R.id.btn_showemoticons);
        btn_showEmoticons.setEnabled(false);
        btn_showEmoticons.setClickable(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isOnCreate) {
            if (userManager.getUser(myName).getState().equals("die"))
                setDied();

            /*모든 Chat.db에 내용을 읽어옴, 다른 frgment에서 저장된 */
            Cursor cursor = chatDB.query("chats", new String[]{"name", "emoticonID", "text", "type"}, null, null, null, null, null);

            if (cursor == null) {
                Log.i("[LOADCHAT]", "cursor null");
            }
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                Integer emoticonID = cursor.getInt(1);
                String text = cursor.getString(2);
                Integer type = cursor.getInt(3);

                Chat chat = new Chat(name, emoticonID, text, type);
                Log.i("[LOADCHAT]", chat.toString());
                chatAdapter.add(chat);
            }
            /*모든 Chat.db에 내용을 삭제*/
            chatDB.delete("chats", null, null);
            chatAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("chats", chats);
    }
}

