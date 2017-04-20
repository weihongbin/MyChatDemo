package com.example.administrator.mychatdemo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;

import org.json.JSONException;

import util.SendMessage;
import util.RecieveMessage;


public class Main4Activity extends Activity implements
        android.view.View.OnClickListener {
    public String SERVERIP;
    public int SERVERPORT;
    public TextView text1;
    public EditText input;
    public EditText et_ip;
    public EditText et_port;
    public Button btn;
    public boolean start = false;
    public Handler Handler;
    private Button brn_read;
    int i = 1;
    Timer timer=new Timer();
    ArrayList<VideoFilePath> videoArrayList =new ArrayList<VideoFilePath>();
    int[] argds={R.mipmap.ic_launcher,R.drawable.logo,R.drawable.my_};
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        text1 = (TextView) findViewById(R.id.textView);
        input = (EditText) findViewById(R.id.editText3);
        btn = (Button) findViewById(R.id.btn_send);
        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);


        btn.setOnClickListener(this);
        start = false;
        Handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
              final  String text = (String) msg.obj;
//                if(!TextUtils.isEmpty(text)){//回复包型，消息的类型（与服务器的心跳包，与服务端通信的包，与客户端通信的包，客户端验证类型的，离线消息，群消息，文件）
//                    new Thread(new Client(SERVERIP, SERVERPORT, "应答：+成功", Handler, 8222)).start();//发送数据
//                     }

                text1.append(text);


            }
        };
//        new Thread(new RecieveMessage(8222, Handler)).start();//读取数据
    }

    @Override
    public void onClick(View v) {
//        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_send) {
//
            for (int i=0;i<5;i++){
                try {
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),argds[new Random().nextInt(3)]);
                    byte[] binput=convertIconToString(bitmap);
//                new Thread(new Utils(android.os.Environment.getExternalStorageDirectory() + "//" + "1474249976937.amr", 0, videoArrayList, Handler)).start();//发送数据
                    new Thread(new SendMessage("192.168.1.137",8888, binput, Handler, 8222)).start();//发送数据
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            }
          else {

        }


    }
    public static byte[] convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return appicon;

    }
//    class TimeRunable implements  Runnable{
//        i=
//        @Override
//        public void run() {
//            while(true){
//
//            }
//        }
//    }
}
