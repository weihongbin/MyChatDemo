package com.example.administrator.mychatdemo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jeefw.model.sys.Information;


public class ClientThread  implements Runnable {
Socket s;
    // 定义向UI线程发送消息的Handler对象
    Handler handler;
    // 定义接收UI线程的Handler对象
    Handler revHandler;
    // 该线程处理Socket所对用的输入输出流
    BufferedReader br = null;
    ObjectOutputStream oos=null;

    Information ms;
    public ClientThread(Handler handler) {
        this.handler = handler;


    }


    @Override
    public void run() {
        try {
                        s=new Socket();
//            if (!s.getKeepAlive()){
                s.connect(new InetSocketAddress("192.168.1.195", 3000), 5000);

//            }
//            System.out.print(s.getKeepAlive()+"\n"+s.getRemoteSocketAddress());
//            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            os = s.getOutputStream();
            try {
               //读取数据
                ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
                ms=(Information) ois.readObject();
                Message msg = new Message();
                msg.what = 0x123;
                msg.obj = ms;
                handler.sendMessage(msg);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            // 启动一条子线程来读取服务器相应的数据
//            new Thread() {
//
//
//                @Override
//                public void run() {
//                    String content = null;
//                    // 不断的读取Socket输入流的内容
//                    try {
////                        while ((content = br.readLine()) != null) {
//                            // 每当读取到来自服务器的数据之后，发送的消息通知程序
//                            // 界面显示该数据
//                            Message msg = new Message();
//                            msg.what = 0x123;
//                            msg.obj = ms;
//                            handler.sendMessage(msg);
////                        }
//                    } catch (Exception io) {
//                        io.printStackTrace();
//                    }
//                }
//
//
//            }.start();
            // 为当前线程初始化Looper
            Looper.prepare();
            // 创建revHandler对象
            revHandler = new Handler() {


                @Override
                public void handleMessage(Message msg) {
                    // 接收到UI线程的中用户输入的数据
                    if (msg.what == 0x345) {
                        // 将用户在文本框输入的内容写入网络
                        try {
//                            os.write((msg.obj.toString() + "\r\n")
//                                    .getBytes("gbk"));
                            Information mm=(Information) msg.obj;
                            oos.writeObject(mm);//写数据

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            };
            // 启动Looper
            Looper.loop();


        } catch (SocketTimeoutException e) {
            Message msg = new Message();
            msg.what = 0x123;
            msg.obj = "网络连接超时！";
            handler.sendMessage(msg);
        } catch (IOException io) {
            io.printStackTrace();
        }


    }
}
