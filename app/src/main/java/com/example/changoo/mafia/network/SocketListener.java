package com.example.changoo.mafia.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.changoo.mafia.model.Data;
import com.example.changoo.mafia.model.SocketData;

import java.io.IOException;
import java.io.ObjectInputStream;

public class SocketListener extends Thread {
    private static SocketListener instance=null;

    public static SocketListener getInstance(){
        if(instance==null){
            instance=new SocketListener();
            instance.start();
        }
        return instance;
    }


    private ObjectInputStream ois;
    private Handler mHandler;
    private boolean life;

    public SocketListener() {
        life = true;
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        super.run();
        Log.i("[SocketListener]", "LISTENR START");
        try {
            SocketManager.getSocket();
            ois = SocketManager.getObjectInputStream();
        } catch (IOException e) {
            Log.e("SocketListener", e.getMessage());
        }

        while (life) {
            try {

                SocketData socketData=(SocketData)ois.readUnshared();
                String recv_command = socketData.getCommand();
                String recv_name = socketData.getName();
                Object recv_object = socketData.getObject();

                Log.i("[RECV]", "Thread " + this.getId() + " " + recv_command);

                Message msg = Message.obtain(mHandler);
                Bundle bundle = new Bundle();

                bundle.putSerializable("data", new Data(recv_command, recv_name, recv_object));
                msg.setData(bundle);

                mHandler.sendMessage(msg);

            } catch (IOException e) {
                Log.e("SocketListener", "IOException " + e.getMessage());
                try{
                    SocketManager.closeSocket();

                }catch (IOException ee){}
                return;
            } catch (ClassNotFoundException e) {
                Log.e("SocketListener", "ClassNotFoundException " + e.getMessage());
                try{
                    SocketManager.closeSocket();
                }catch (IOException ee){}
                return;
            }
        }


    }

}
