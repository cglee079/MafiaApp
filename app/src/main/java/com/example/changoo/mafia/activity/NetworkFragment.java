package com.example.changoo.mafia.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.changoo.mafia.Database.ChatDBOpenHelper;
import com.example.changoo.mafia.Database.HiddenChatDBOpenHelper;
import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.Command;
import com.example.changoo.mafia.command.PlayCommand;
import com.example.changoo.mafia.model.Data;
import com.example.changoo.mafia.model.HiddenChatAdapter;
import com.example.changoo.mafia.model.UserInfo;
import com.example.changoo.mafia.model.UserManager;
import com.example.changoo.mafia.network.SocketListener;
import com.example.changoo.mafia.network.SocketManager;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by changoo on 2016-12-02.
 */

public abstract class NetworkFragment extends Fragment {

    protected Handler mainHandler;
    protected SocketListener sl = null;

    protected String myName;
    protected UserManager userManager;

    protected  boolean isOnCreate=false;
    protected SQLiteDatabase hiddenChatDB;
    protected SQLiteDatabase chatDB;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myName = getArguments().getString("myName");
        userManager = UserManager.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ChatDBOpenHelper chatDBOpenHelper= new ChatDBOpenHelper(getActivity(),"chats.db",3);
        chatDB=chatDBOpenHelper.getWritableDatabase();
        HiddenChatDBOpenHelper hiddenChatDBOpenHelper = new HiddenChatDBOpenHelper(getActivity(),"hiddenchats.db",3);
        hiddenChatDB=hiddenChatDBOpenHelper.getWritableDatabase();

        isOnCreate=true;
    }


    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
        TextView tv_playnotice = (TextView) getActivity().findViewById(R.id.tv_playnotice);
        TextView tv_timer = (TextView) getActivity().findViewById(R.id.tv_timer);
        ToggleButton tbtn_wantnext = (ToggleButton)getActivity().findViewById(R.id.tbtn_wantnext);

        switch (recv_command) {
            case Command.USERUPDATE:
                userManager.clear();
                userManager.addAllUser((Vector<UserInfo>) recv_object);
                break;

            case PlayCommand.NOTICE:
                tv_playnotice.setTextColor(Color.RED);
                tv_playnotice.setText((String) recv_object);
                break;
            case PlayCommand.IMPORTANTNOTICE:
                tv_playnotice.setTextColor(Color.BLUE);
                tv_playnotice.setText((String) recv_object);
                break;

            case PlayCommand.GAMEOVER:
                String winner=(String)recv_object;
                Intent intent=new Intent(getActivity(),GameOverActivity.class);
                intent.putExtra("myName",myName);
                switch(winner){
                    case "MAFIAWIN":
                        intent.putExtra("winner","MAFIA 승리");
                        break;
                    case "MAFIALOSE":
                        intent.putExtra("winner","MAFIA 패배");
                        break;
                }
                startActivity(intent);
                getActivity().finish();
                break;

            case PlayCommand.YOUAREDIE:
                tv_timer.setText("I'm die....");
                tbtn_wantnext.setEnabled(false);
                tbtn_wantnext.setClickable(false);
                break;

            case PlayCommand.NOTOUCHABLE:
                this.setTouchable(false);
                break;

            case PlayCommand.TOUCHABLE:
                this.setTouchable(true);
                break;

            case PlayCommand.TIMER:
                final Integer timer = (Integer) recv_object;
                if (timer == 20)
                    tv_timer.setTextColor(Color.RED);
                tv_timer.setText(timer.toString());
                break;

            case PlayCommand.STARTVOTE:
                this.setTouchable(true);
                final String[] aliveUsernames = userManager.getAliveUserNames();
                DialogInterface.OnClickListener mDlgListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String votedUser = aliveUsernames[which];
                        sendMsg(PlayCommand.VOTEUSER, myName, votedUser);
                        setTouchable(false);
                    }
                };
                new AlertDialog.Builder(getActivity())
                        .setTitle("처형될 유저를 선택하십시오.")
                        .setIcon(R.drawable.user_icon)
                        .setItems(aliveUsernames, mDlgListener)
                        .setCancelable(false)
                        .show();
                break;

            case PlayCommand.GONIGHT:
                sendMsg(PlayCommand.IMINNIGHT, myName, "night");
                if(userManager.getUser(myName).getCharacter().equals("CIVIL")==false) {
                    this.setTouchable(true);

                    String title = "";
                    switch (userManager.getUser(myName).getCharacter()) {
                        case "MAFIA":
                            title = "당신은 마피아입니다.\n 살인할 인원을 선택하십시오.";
                            break;
                        case "COP":
                            title = "당신은 경찰입니다.\n 신원확인을 원하는 인원을 선택하십시오.";
                            break;
                        case "DOCTOR":
                            title = "당신은 의사입니다.\n 마피아로부터 구할 인원을 선택하십시오.";
                            break;
                    }

                    final String[] aliveUsernames2 = userManager.getAliveUserNames();
                    DialogInterface.OnClickListener mDlgListener2 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String choiceuser = aliveUsernames2[which];
                            sendMsg(PlayCommand.CHOICEUSERINNIGHT, myName, choiceuser);
                            setTouchable(false);
                        }
                    };
                    new AlertDialog.Builder(getActivity())
                            .setTitle(title)
                            .setIcon(R.drawable.user_icon)
                            .setItems(aliveUsernames2, mDlgListener2)
                            .setCancelable(false)
                            .show();
                }
                break;

            case PlayCommand.GOSUNNY:
                setTouchable(true);
                tbtn_wantnext = (ToggleButton) getActivity().findViewById(R.id.tbtn_wantnext);
                tbtn_wantnext.setChecked(false);
                sendMsg(PlayCommand.IMINSUNNY, myName, "sunny");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        chatDB.delete("chats",null,null);
        hiddenChatDB.delete("hiddenchats",null,null);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
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
        } else {
            sl = null;
        }
    }

    public void startListen() {
        if (sl == null) {
            Log.i("[NetworkFragment]", "START LISTENER");
            sl = SocketListener.getInstance();
            sl.setHandler(mainHandler);
        }
    }

    public void sendMsg(String command, String name, Object object) {
        try {
            SocketManager.sendMsg(command, name, object);
            Log.i("[SEND]:", command);
        } catch (IOException e) {
            Log.e("[SEND/ERROR]:", command);
        }
    }

    public void setTouchable(boolean touchable) {
        if (touchable == false)
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        else
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

}
