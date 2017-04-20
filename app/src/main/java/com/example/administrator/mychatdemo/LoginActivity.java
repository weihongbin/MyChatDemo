package com.example.administrator.mychatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jeefw.model.sys.User;
import util.ResgisterRequset;

/**
 * 登陆
 */
public class LoginActivity extends AppCompatActivity {
    private Button mbtn;
    private Button mbtnlogin;

    private EditText medit;
    private EditText meditpass;
    private String name;
    private String password;
    private boolean flag = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Toast.makeText(LoginActivity.this,msg.obj.toString(),Toast.LENGTH_LONG).show();
//            new Thread(new ResgisterRespone(8222, handler)).start();//读取数据
            if (msg.what == 0x1234) {
                // 将读取的内容追加显示在文本框中
//                show.append("\n" + msg.obj.toString());

                User user = (User) msg.obj;
                if (flag) {

                    if (user.isFlag()) {
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), msg.toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (user.isFlag()) {
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), msg.toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//
//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    }

                }


            }else if (msg.what==0x12345){
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
        mbtnlogin = (Button) findViewById(R.id.button);//登陆
        mbtn = (Button) findViewById(R.id.button2);//注册
        medit = (EditText) findViewById(R.id.editText);
        meditpass = (EditText) findViewById(R.id.editText2);//

        mbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = medit.getText().toString().trim();//获取用户输入的数据
                password = meditpass.getText().toString().trim();//登录
                User user = new User();
                user.setUserName(name);
                user.setPassWord(password);
                user.setType("login");

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {

                    flag = true;
                    new BusinThread(user, handler).start();
                } else {
                    Toast.makeText(getApplicationContext(), "请输入用户名或密码", Toast.LENGTH_SHORT).show();

                }
            }
        });
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//注册
                name = medit.getText().toString().trim();//获取用户输入的数据
                password = meditpass.getText().toString().trim();
//                User user = new User();
//                user.setUserName(name);
//                user.setPassWord(password);
//                user.setType("register");
//                TBSocketInfo ti=new TBSocketInfo();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
//                    ti.setJsonData("{username:22222,pwd:22222222222222222222222222222}");
                    String content="{username:"+name+",pwd:"+password+"}";
                    flag = false;
//                    new BusinThread(ti, handler).start();

                    new Thread(new ResgisterRequset("192.168.1.143", 8222,content, handler, 8222)).start();//发送数据
                } else {
                    Toast.makeText(getApplicationContext(), "请输入用户名或密码", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
