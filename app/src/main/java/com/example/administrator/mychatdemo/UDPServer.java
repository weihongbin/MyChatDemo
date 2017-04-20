package com.example.administrator.mychatdemo;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/8/10.
 */
public class UDPServer implements Runnable {
    private static final int PORT = 8888;
    private byte[] msg = new byte[1024];
    private boolean life = true;
    public UDPServer() {
    }

    public boolean isLife() {
        return life;
    }

    public void setLife(boolean life) {
        this.life = life;
    }

    @Override
    public void run() {
//        DatagramSocket dSocket = null;
//        DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
//        try {
//            dSocket = new DatagramSocket(PORT);
//            while (life) {
//                try {
//                    dSocket.receive(dPacket);
//                    Log.e("msg sever received", new String(dPacket.getData(),"gbk"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            InetAddress address = InetAddress.getLocalHost();
            int port = 8888;

            //创建DatagramSocket对象
            DatagramSocket socket = new DatagramSocket(port, address);

            byte[] buf = new byte[1024];  //定义byte数组
            DatagramPacket packet = new DatagramPacket(buf, buf.length);  //创建DatagramPacket对象

            socket.receive(packet);  //通过套接字接收数据

            String getMsg = new String(buf, 0, packet.getLength());
            System.out.println("客户端发送的数据为：" + getMsg);

            //从服务器返回给客户端数据
            InetAddress clientAddress = packet.getAddress(); //获得客户端的IP地址
            int clientPort = packet.getPort(); //获得客户端的端口号
            SocketAddress sendAddress = packet.getSocketAddress();
            String feedback = "Received";
            byte[] backbuf = feedback.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(backbuf, backbuf.length, sendAddress); //封装返回给客户端的数据
            socket.send(sendPacket);  //通过套接字反馈服务器数据

            socket.close();  //关闭套接字

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
//}