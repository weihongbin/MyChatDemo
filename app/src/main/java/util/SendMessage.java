package util;

import android.os.Handler;
import android.util.Log;

import com.jeefw.model.sys.Information;
import com.jeefw.model.sys.TBSocketInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Administrator on 2016/8/15.
 * 发送端 ---客户端
 */
public class SendMessage implements Runnable {
    private  String SERVERIP;
    private int  SERVERPORT;
    private byte[] input;
    private int port;//要绑定的端口
    public Handler handler;
    DatagramSocket datagramSocket;
    public SendMessage() {

    }

    public SendMessage(String SERVERIP, int SERVERPORT, byte[] input, Handler handler, int port) {
        this.SERVERIP = SERVERIP;
        this.SERVERPORT = SERVERPORT;
        this.input = input;
        this.handler = handler;
        this.port = port;
    }

    @Override
    public void run() {

        try {

//            TBSocketInfo  im=new TBSocketInfo();
//            im.setJsonData(input);
//            byte[] by=new byte[1024*1024];
//            ByteArrayOutputStream bs=new ByteArrayOutputStream();
//            ObjectOutputStream bo=new ObjectOutputStream(bs);
//            FileOutputStream fos=new FileOutputStream(bo);

//            bo.writeObject(input);
//            by=bs.toByteArray();

            //从Buf数组中，取出Length长的数据创建数据包对象，目标是clientAddress地址，clientPort端口,通常用来发送数据
            DatagramPacket sendpacket=new DatagramPacket(input, input.length, InetAddress.getByName("192.168.1.137"), 8888);
            //发送报文sendpacket到目的地
            datagramSocket = new DatagramSocket();
            datagramSocket.send(sendpacket);
            Log.e("datagramSocket", "run:33333333333 "+InetAddress.getByName("192.168.1.137")+"\n"+ input.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}