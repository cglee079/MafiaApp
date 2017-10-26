package com.example.changoo.mafia.activity;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.Command;
import com.example.changoo.mafia.command.GameoverCommand;
import com.example.changoo.mafia.model.GameoverAdapter;
import com.example.changoo.mafia.model.UserInfo;
import com.example.changoo.mafia.model.UserManager;

import java.util.Vector;

public class GameOverActivity extends NetWorkActivity {
    private String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**No Title Bar**/
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();
        myName = intent.getStringExtra("myName");
        String winner = intent.getStringExtra("winner");


        TextView tv_winner = (TextView) findViewById(R.id.tv_winner);
        tv_winner.setText(winner);

        GameoverAdapter gameoverAdapter = new GameoverAdapter(this,myName);
        ListView lv_gameover = (ListView) findViewById(R.id.lv_gameover);
        lv_gameover.setAdapter(gameoverAdapter);
        lv_gameover.setDivider(null);

        setBtn_regame();
        setBtn_exit();
    }

    private void setBtn_regame() {
        Button btn_regame = (Button) findViewById(R.id.btn_regame);
        btn_regame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg(GameoverCommand.REGAME, myName, "");
                Intent intent = new Intent(GameOverActivity.this, WaitActivity.class);
                intent.putExtra("myName", myName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setBtn_exit() {
        Button btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAffinity(GameOverActivity.this);
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    @Override
    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
        switch (recv_command) {
            case Command.USERUPDATE:
                UserManager userManager = UserManager.getInstance();
                userManager.clear();
                userManager.addAllUser((Vector<UserInfo>) recv_object);
                break;
        }
    }
}
