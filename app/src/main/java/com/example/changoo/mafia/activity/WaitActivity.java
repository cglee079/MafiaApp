package com.example.changoo.mafia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.Command;
import com.example.changoo.mafia.command.WaitCommand;
import com.example.changoo.mafia.model.UserInfo;
import com.example.changoo.mafia.model.UserManager;
import com.example.changoo.mafia.model.WaitAdapter;

import java.util.Vector;

public class WaitActivity extends NetWorkActivity {
    private WaitAdapter waitAdapter;
    private String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.waitlayout);
        Intent intent = getIntent();
        myName = intent.getStringExtra("myName");

        ListView lv_waitlist = (ListView) findViewById(R.id.lv_waitlist);
        waitAdapter = new WaitAdapter(this, myName);
        lv_waitlist.setDivider(null);
        lv_waitlist.setAdapter(waitAdapter);


        setbtn_wait_ready();
        sendMsg(WaitCommand.IMMWAITACTIVITY, myName, "");
    }


    @Override
    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
        switch (recv_command) {
            case Command.USERUPDATE:
                UserManager userManager = UserManager.getInstance();
                userManager.clear();
                userManager.addAllUser((Vector<UserInfo>) recv_object);
                Vector<UserInfo> userInfos = ((Vector<UserInfo>) recv_object);
                for (int i = 0; i < userInfos.size(); i++) {
                    Log.i("[USERUPDATE]", userInfos.get(i).toString() + "\n");
                }

                waitAdapter.notifyDataSetChanged();
                break;

            case WaitCommand.NOTICE:
                final String notice = (String) recv_object;
                TextView tv_wait_notice = (TextView) findViewById(R.id.tv_wait_notice);
                tv_wait_notice.setText(notice);
                break;

            case WaitCommand.STARTGAME:
                Intent intent_playactivity = new Intent(this, PlayActivity.class);
                intent_playactivity.putExtra("myName", myName);
                intent_playactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent_playactivity);
                finish();
                break;
        }
    }

    public void setbtn_wait_ready() {
        Button btn_wait_ready = (Button) findViewById(R.id.btn_wait_ready);
        btn_wait_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager userManager = UserManager.getInstance();
                if (userManager.getUser(myName).getState().equals("ready"))
                    sendMsg(WaitCommand.IMREADY, myName, "wait");
                else
                    sendMsg(WaitCommand.IMREADY, myName, "ready");
            }
        });
    }

}
