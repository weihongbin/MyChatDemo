package util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jeefw.model.sys.TBSocketInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;



/**
 * Created by Administrator on 2016/8/15.
 * 发送端 ---客户端
 */
public class ResgisterRequset implements Runnable {
    private  String SERVERIP;
    private int  SERVERPORT;
    private String input;
    private int port;//要绑定的端口
    public Handler handler;

    public ResgisterRequset() {

    }

    public ResgisterRequset(String SERVERIP, int SERVERPORT, String input, Handler handler, int port) {
        this.SERVERIP = SERVERIP;
        this.SERVERPORT = SERVERPORT;
        this.input = input;
        this.handler = handler;
        this.port = port;
    }

    @Override
    public void run() {

        try {
//发送数据的套接字变量

            //UDP的数据包变量
//            DatagramPacket dp = null;
            //实例化UDP的套接字,端口号为9999,UDP套接字绑定的端口

//            Dssingle.getInstance().getDatagramSocket().setReuseAddress(true);
            //需要发送的数据

            String str=input;
            //指定需要发送的数据内容,数据长度,目的IP和目的端口号
//            byte[] sendBuf=str.getBytes("utf-8");
            TBSocketInfo im=new TBSocketInfo();
            im.setJsonData(input);
            byte[] by=new byte[1024*1024];
            ByteArrayOutputStream bs=new ByteArrayOutputStream();
            ObjectOutputStream bo=new ObjectOutputStream(bs);
            bo.writeObject(im);
            by=bs.toByteArray();


            InetAddress addr=InetAddress.getByName(SERVERIP);

            //从Buf数组中，取出Length长的数据创建数据包对象，目标是clientAddress地址，clientPort端口,通常用来发送数据
            DatagramPacket sendpacket=new DatagramPacket(by, by.length, addr, SERVERPORT);

            //发送报文sendpacket到目的地
            Dssingon.getInstance().getDatagramSocket(port).send(sendpacket);


//            dp = new DatagramPacket(str.getBytes("UTF-8" ),str.length(),InetAddress.getByName(SERVERIP),SERVERPORT);
            updatetrack("Client: Start connecting\n");
            //发送数据
//           .send(dp);

            updatetrack("Client: Message sent\n");
            updatetrack("Client: Succeed!\n");
            //关闭
//            Dssingle.getInstance().getDatagramSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }

        try {
//
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, 1024);
        Log.e("2222", "222222222222222222222222");
//         socket.receive(packet);
            Dssingon.getInstance().getDatagramSocket(port).receive(dp);
        ByteArrayInputStream bs=new ByteArrayInputStream(dp.getData());
        ObjectInputStream os=new ObjectInputStream(bs);
        TBSocketInfo m = (TBSocketInfo)os.readObject();
        //  System.out.println(m+"-----");
        System.out.println(m.toString());
////            socket.receive(packet);
//            updatetrack("Client: Message sent\n");
            updatetrack("Client: Succeed!\n"+m.toString());
//
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void updatetrack(String s) {
        Message msg = new Message();
        String textTochange = s;
        msg.obj = textTochange;
        handler.sendMessage(msg);
    }
}