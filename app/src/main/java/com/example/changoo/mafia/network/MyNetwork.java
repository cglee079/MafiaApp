//package com.example.changoo.mafia.network;
//
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.changoo.mafia.R;
//import com.example.changoo.mafia.activity.PlayActivity;
//import com.example.changoo.mafia.activity.WaitActivity;
//import com.example.changoo.mafia.command.ChatCommand;
//import com.example.changoo.mafia.command.Command;
//import com.example.changoo.mafia.command.LoginCommand;
//import com.example.changoo.mafia.command.WaitCommand;
//import com.example.changoo.mafia.model.Message;

//import com.example.changoo.mafia.model.UserManager;
//import com.example.changoo.mafia.model.UserInfo;
//import com.example.changoo.mafia.model.WaitAdapter;
//
//import java.io.IOException;
//import java.util.Vector;
//
//
//public class MyNetwork extends Thread {
//
//    private static final MyNetwork instance = new MyNetwork();
//
//    public static MyNetwork getInstance() {
//        return instance;
//    }
//
//    private Thread receiveMsg;
//    private SocketManager mySocket;
//    private Activity mContext;
//
//    private String myName;
//    private Handler mHandler;
//    private WaitAdapter waitadapter = null;

//    private UserManager userManager = UserManager.getInstance();
//
//    private MyNetwork() {
//        this.mHandler = new Handler();
//    }
//
//    public void setMyName(String myName) {
//        this.myName = myName;
//    }
//
//    public Activity getmContext() {
//        return mContext;
//    }
//
//    public void setmContext(Activity mContext) {
//        this.mContext = mContext;
//    }
//
//    public WaitAdapter getWaitadapter() {
//        return waitadapter;
//    }
//
//    public void setWaitadapter(WaitAdapter waitadapter) {
//        this.waitadapter = waitadapter;
//    }
//

//        return messageAdapter;
//    }
//

//        this.messageAdapter = messageAdapter;
//    }
//
//    public void stopNetwork() {
//        try {
//            this.interrupt();
//            mySocket.close();
//            receiveMsg = null;
//        } catch (Exception e) {
//
//        }
//    }
//
//    public void run() {
//        try {
//            mySocket = new SocketManager();
//            Log.i("[Client]", " Server connected !!");
//
//
//            receiveMsg = new Thread(new ReceiveMsgThread());
//            receiveMsg.setDaemon(true);
//            receiveMsg.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("[MyNetwork]", " Server don't connected !!");
//        }
//    }
//
//    public void send_Message(String command, String username, Object obj) {
//        try {
//            mySocket.writeObject(command);
//            mySocket.writeObject(username);
//            mySocket.writeObject(obj);
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    class ReceiveMsgThread implements Runnable {
//        @SuppressWarnings("null")
//        @Override
//        public void run() {
//            while (Thread.currentThread() == receiveMsg) {
//                try {
//                    String recv_command = (String) mySocket.readObject();
//                    String recv_username = (String) mySocket.readObject();
//                    Object recv_object = mySocket.readObject();
//                    recvMsgLogic(recv_command, recv_username, recv_object);
//
//                } catch (IOException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                    stopNetwork();
//                }
//            }
//            Log.d("[MyNetwork]", " Stopped");
//        }
//    }
//
//
//    public void recvMsgLogic(String recv_command, String recv_username, Object recv_object) throws IOException, ClassNotFoundException {
//        switch (recv_command) {
//            case LoginCommand.CONFIRMNAME:
//                Log.i("[RECV]", LoginCommand.CONFIRMNAME);
//                boolean confirmname = (Boolean) recv_object;
//                if (confirmname == true) ;
//                else {
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(mContext, "동일 이름 확인, 접속불가!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                break;
//
//            case LoginCommand.GOWAITROOM:
//                Log.i("[RECV]", LoginCommand.GOWAITROOM);
//                Intent intent = new Intent(mContext, WaitActivity.class);
//                intent.putExtra("myName", myName);
//                mContext.startActivity(intent);
//                mContext.finish();
//                mHandler.postDelayed(new Runnable() {// 1 초 후에 실행
//                    @Override
//                    public void run() {
//                        send_Message(WaitCommand.IMWAITROOM, myName, null);
//                        mHandler.sendEmptyMessage(0);    // 실행이 끝난후 알림
//                    }
//                }, 100);
//
//                break;
//
//            case Command.USERUPDATE:
//                Log.i("[RECV]", WaitCommand.USERUPDATE);
//                userManager.clear();
//                userManager.addAllUser((Vector<UserInfo>) recv_object);
//                mHandler.post(new Runnable() {
//                    public void run() {
//                        waitadapter.notifyDataSetChanged();
//                    }
//                });
//                break;
//
//            case WaitCommand.NOTICE:
//                Log.i("[RECV]", WaitCommand.NOTICE);
//                final String notice = (String) recv_object;
//                mHandler.post(new Runnable() {
//                    public void run() {
//                        TextView tv_wait_notice = (TextView) mContext.findViewById(R.id.tv_wait_notice);
//                        tv_wait_notice.setText(notice);
//                    }
//                });
//                break;
//
//            case WaitCommand.STARTGAME:
//                Intent intent_playactivity = new Intent(mContext, PlayActivity.class);
//                intent_playactivity.putExtra("myName", myName);
//                mContext.startActivity(intent_playactivity);
//                break;
//

//
//        }
//    }
//
//
//}
