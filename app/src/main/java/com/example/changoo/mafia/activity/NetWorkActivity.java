package com.example.changoo.mafia.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.changoo.mafia.command.Command;
import com.example.changoo.mafia.model.Data;
import com.example.changoo.mafia.network.SocketListener;
import com.example.changoo.mafia.network.SocketManager;

import java.io.IOException;

/**
 * Created by changoo on 2016-12-02.
 */

public abstract class NetWorkActivity extends AppCompatActivity {
    protected Handler mainHandler;
    protected SocketListener sl = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();

                Data data = (Data) bundle.getSerializable("data");
                String recv_command = data.getCommand();
                String recv_name = data.getName();
                Object recv_object = data.getObject();
                recvMsg(recv_command,recv_name,recv_object);
            }

        };

        this.startListen();

    }

    private long backKeyPressedTime = 0;
    private Toast toast;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this,
                    "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(this);
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            toast.cancel();
        }
    }

    public abstract void recvMsg(String recv_command, String recv_name, Object recv_object);

    public void startListen() {
        if(sl==null) {
            Log.i("[NetworkActivity]", "START LISTENER");
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

}
