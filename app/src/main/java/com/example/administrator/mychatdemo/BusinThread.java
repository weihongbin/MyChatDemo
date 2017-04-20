package com.example.administrator.mychatdemo;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;


import com.jeefw.model.sys.User;

class BusinThread extends Thread {
    private User user;
    protected Socket s;

    public BusinThread() {
    }

    ;
    Handler handler;
    // 该线程处理Socket所对用的输入输出流
    BufferedReader br = null;
    OutputStream os = null;

    public BusinThread(User user, Handler handler) {
        this.user = user;
        this.handler = handler;
    }

    @Override
    public void run() {


        try {
            if (s == null) {
                s = new Socket();
            }
            System.out.print(s.getKeepAlive() + "\n" + s.getRemoteSocketAddress());


            if (!s.getKeepAlive()) {
//                s.connect();
                s.connect(new InetSocketAddress("192.168.1.227", 8989), 5000);

            }
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(user);//写数据
//            oos.write("12".getBytes());
            oos.flush();
//            SocketChannel socketChannel=new SocketChannel();

//            ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
//            User ms=(User) ois.readObject();

//
////            byte[] bytes=new byte[1024*1024];
//            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//            PrintWriter pw=new PrintWriter(s.getOutputStream());
//            pw.write(user.toString());
//            os.write(.getBytes());getBytes
//            pw.flush();
//            pw.close();
//            bw.write("222");
//            bw.close();
//            bw.flush();
//            br = new BufferedReader(new InputStreamReader(s.getInputStream()));//获取输入流
            //                ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
//                oos.writeObject(obj);
//            Selector selector = Selector.open();


            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            final User ms = (User) ois.readObject();
//            ois.read();
            new Thread() {

//开启读取数据的子线程

                @Override
                public void run() {
//                    String content = null;
                    // 不断的读取Socket输入流的内容
                    try {
//                        while ((content = br.readLine()) != null) {
                            // 每当读取到来自服务器的数据之后，发送的消息通知程序

                            Message msg = new Message();
                            msg.what = 0x1234;
                            msg.obj = ms;

                            handler.sendMessage(msg);
//                        }
                    } catch (Exception io) {
                        Message msg = new Message();
                        msg.what = 0x12345;
                        msg.obj = "数据异常";
                        handler.sendMessage(msg);
                        io.printStackTrace();
                    }
                }


            }.start();
        } catch (Exception e) {//
            e.printStackTrace();
            Message msg = new Message();
            msg.what = 0x12345;
            msg.obj = "数据异常";
            handler.sendMessage(msg);
        }
        super.run();
    }
}

