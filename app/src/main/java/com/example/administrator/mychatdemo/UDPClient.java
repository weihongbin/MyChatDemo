package com.example.administrator.mychatdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/8/10.
 */
public class UDPClient implements  Runnable    {
    private static final int SERVER_PORT = 8888;
    private DatagramSocket dSocket = null;
    private String msg;

    public UDPClient(String msg) {
        super();
        this.msg = msg;
    }

    @Override
    public void run() {

        byte[] buf = msg.getBytes();
        try {
            InetAddress address = InetAddress.getByName("192.168.1.227");  //服务器地址
            int port = UDPUtils.PORT;  //服务器的端口号
            //创建发送方的数据报信息
            DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, address, port);

            DatagramSocket socket = new DatagramSocket();  //创建套接字
            socket.send(dataGramPacket);  //通过套接字发送数据

            //接收服务器反馈数据
            byte[] backbuf = new byte[1024];
            DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);
            socket.receive(backPacket);  //接收返回数据
            String backMsg = new String(backbuf, 0, backPacket.getLength());
            System.out.println("服务器返回的数据为:" + backMsg);

            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public String send() {
//        StringBuilder sb = new StringBuilder();
//        InetAddress local = null;
//        try {
//            local = InetAddress.getByName("192.168.1.109"); // 本机测试
//            sb.append("已找到服务器,连接中...").append("/n");
//        } catch (UnknownHostException e) {
//            sb.append("未找到服务器.").append("/n");
//            e.printStackTrace();
//        }
//        try {
//            dSocket = new DatagramSocket(); // 注意此处要先在配置文件里设置权限,否则会抛权限不足的异常
//            sb.append("正在连接服务器...").append("/n");
//        } catch (SocketException e) {
//            e.printStackTrace();
//            sb.append("服务器连接失败.").append("/n");
//        }
//        int msg_len = msg == null ? 0 : msg.length();
//        DatagramPacket dPacket = new DatagramPacket(msg.getBytes(), msg_len,
//                local, SERVER_PORT);
//        try {
//            dSocket.send(dPacket);
//            sb.append("消息发送成功!").append("/n");
//        } catch (IOException e) {
//            e.printStackTrace();
//            sb.append("消息发送失败.").append("/n");
//        }
//        dSocket.close();
//        return sb.toString();
//    }
}