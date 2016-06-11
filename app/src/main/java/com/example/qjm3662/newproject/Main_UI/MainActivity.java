package com.example.qjm3662.newproject.Main_UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.Final_Static_data;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.StoryBean;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Finding.Finding;
import com.example.qjm3662.newproject.LoginAndRegister.LoginActivity;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.StoryView.Edit_Acticity;
import com.example.qjm3662.newproject.StoryView.Main2Activity;
import com.example.qjm3662.newproject.myself.Myself;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class MainActivity extends Activity implements OnClickListener {

    private ImageView story;
    private ImageView find;
    private ImageView message;
    private ImageView my;
    private StoryFragment storyFragment;
    private Finding findFragment;
    private MessageFragment messageFragment;
    private Myself myFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        init();
        setTab_selection(0);
//        // 获取Calendar实例
//        Calendar calendar = Calendar.getInstance();
//        // 输出日期信息，还有许多常量字段，我就不再写出来了
//        System.out.println(calendar.get(Calendar.YEAR) + "年"
//                + calendar.get(Calendar.MONTH) + "月"
//                + calendar.get(Calendar.DAY_OF_MONTH) + "日"
//                + calendar.get(Calendar.HOUR_OF_DAY) + "时"
//                + calendar.get(Calendar.MINUTE) + "分"
//                + calendar.get(Calendar.SECOND) + "秒" + "\n今天是星期"
//                + calendar.get(Calendar.DAY_OF_WEEK) + "是今年的第"
//                + calendar.get(Calendar.WEEK_OF_YEAR) + "周");
//        String time = DateFormat
//                      .format("yyyy-MM-dd kk:mm:ss", calendar).toString();
//        System.out.println(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.story:
                setTab_selection(0);
                break;
            case R.id.find:
                setTab_selection(1);
                System.out.println("MainActivity : " + User.getInstance().getLoginToken());
                if(User.getInstance().getLoginToken() != null){
                    NetWorkOperator.Get_finding_story();
                }
                else{
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.message:
                setTab_selection(2);

                break;
            case R.id.my:
                setTab_selection(3);

                //启动登陆界面
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                intent.putExtra("JUDGE",false);
//                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
        }
    }

    private void init() {
        story = (ImageView) findViewById(R.id.story);
        find = (ImageView) findViewById(R.id.find);
        message = (ImageView) findViewById(R.id.message);
        my = (ImageView) findViewById(R.id.my);

        story.setOnClickListener(this);
        find.setOnClickListener(this);
        message.setOnClickListener(this);
        my.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Edit_Acticity.REQUEST_CODE_SAVE:
                System.out.println("save fail");
                if(storyFragment != null){
                    System.out.println("save success");
                    storyFragment.slideAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void clearSelection() {
        story.setImageResource(R.drawable.img_story);
        find.setImageResource(R.drawable.img_findings);
        message.setImageResource(R.drawable.img_message);
        my.setImageResource(R.drawable.img_my);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (storyFragment != null) {
            transaction.hide(storyFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    private void setTab_selection(int index) {
        //每次选中前先清除掉上次选中的状态
        clearSelection();
        //开启一个Fragment事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //先隐藏掉所有的fragment,以防有多个fragment显示在界面上
        hideFragments(transaction);

        switch (index) {
            case 0:
                story.setImageResource(R.drawable.img_story_choose);
                if (storyFragment == null) {
                    storyFragment = new StoryFragment();
                    transaction.add(R.id.framelayout, storyFragment);
                } else {
                    transaction.show(storyFragment);
                }
                break;

            case 1:
                find.setImageResource(R.drawable.img_findings_choose);
                if (findFragment == null) {
                    findFragment = new Finding();
                    transaction.add(R.id.framelayout, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;

            case 2:
                message.setImageResource(R.drawable.img_message_choose);
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.framelayout, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;

            case 3:
                my.setImageResource(R.drawable.img_my_choose);
                if (myFragment == null) {
                    myFragment = new Myself();
                    transaction.add(R.id.framelayout, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;
        }

        transaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.dbWrite.close();
        App.dbRead.close();
    }


}
