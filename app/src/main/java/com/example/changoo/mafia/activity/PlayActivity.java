package com.example.changoo.mafia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.changoo.mafia.R;
import com.example.changoo.mafia.command.PlayCommand;
import com.example.changoo.mafia.model.UserInfo;
import com.example.changoo.mafia.model.UserManager;

public class PlayActivity extends NetWorkActivity {
    private Tab1_chat tab1;
    private Tab2_hiddenchat tab2;
    private Tab3_users tab3;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private String myName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        myName = intent.getStringExtra("myName");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        UserManager userManager = UserManager.getInstance();
        UserInfo user = userManager.getUser(myName);
        String character = user.getCharacter();

        TextView tv_character = (TextView) findViewById(R.id.tv_chracter);
        tv_character.setText(character);

        ImageView img_icon = (ImageView) findViewById(R.id.img_icon_mychrater);
        switch (character) {
            case "MAFIA":
                img_icon.setImageResource(R.drawable.icon_mafia);
                break;
            case "COP":
                img_icon.setImageResource(R.drawable.icon_cop);
                break;
            case "DOCTOR":
                img_icon.setImageResource(R.drawable.icon_doctor);
                break;
            case "CIVIL":
                img_icon.setImageResource(R.drawable.lcon_civil);
                break;
        }

        TextView tv_username = (TextView) findViewById(R.id.tv_username_play);
        tv_username.setText(myName);
        setTbtn_wantnext();



    }

    @Override
    public void onBackPressed() {
        Toast.makeText(PlayActivity.this, "게임중 종료 불가!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recvMsg(String recv_command, String recv_name, Object recv_object) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("myName", myName);
            switch (position) {
                case 0:
                    tab1 = new Tab1_chat();
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    tab2 = new Tab2_hiddenchat();
                    tab2.setArguments(bundle);
                    return tab2;
                case 2:
                    tab3 = new Tab3_users();
                    tab3.setArguments(bundle);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CHAT";
                case 1:
                    return "HIDDEN CHAT";
                case 2:
                    return "USERS";
            }
            return null;
        }
    }

    private void setTbtn_wantnext() {
        ToggleButton tbtn_wantnext = (ToggleButton) findViewById(R.id.tbtn_wantnext);
        tbtn_wantnext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed() == true) {
                    if (isChecked == true) {
                        sendMsg(PlayCommand.IWANTNEXT, myName, true);
                    } else {
                        sendMsg(PlayCommand.IWANTNEXT, myName, false);
                    }
                }
            }
        });

    }


}
