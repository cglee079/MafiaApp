package com.example.changoo.mafia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.LoginCommand;

public class MainActivity extends NetWorkActivity {
    private String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**No Title Bar**/
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setBtn_go();
    }

    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
        switch (recv_command) {
            case LoginCommand.CONFIRMNAME:
                boolean confirm = (Boolean) recv_object;
                if (confirm == true) ;
                else {
                    Toast.makeText(MainActivity.this, "동일 이름 확인, 접속불가!", Toast.LENGTH_SHORT).show();
                }
                break;

            case LoginCommand.GOWAITROOM:
                final Intent intent = new Intent(MainActivity.this, WaitActivity.class);
                intent.putExtra("myName", myName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }




    public void setBtn_go() {
        final EditText et_name = (EditText) findViewById(R.id.et_name);
        Button btn_go = (Button) this.findViewById(R.id.btn_go);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_go) {
                    myName = et_name.getText().toString();
                    if (myName.length() == 0) {
                        Toast.makeText(MainActivity.this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MainActivity.this.sendMsg(LoginCommand.REQUESTCONFIRMNAME, myName, "");

                }
            }
        });
    }

}
