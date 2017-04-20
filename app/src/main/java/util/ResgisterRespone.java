package util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jeefw.model.sys.TBSocketInfo;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;


/**
 * Created by Administrator on 2016/8/15.
 * <p/>
 * <p/>
 * 接收端 =---服务端
 */
public class ResgisterRespone implements Runnable {
    private int PORT;
    private Handler handler;

    public ResgisterRespone() {
    }

    public ResgisterRespone(int PORT, Handler handler) {
        this.PORT = PORT;
        this.handler = handler;
    }

    public void run() {

        try {

            while (true) {

                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, 1024);
                Log.e("2222", "222222222222222222222222");

                Dssingon.getInstance().getDatagramSocket(PORT).receive(dp);//接收

//                String receiveStr = new String(dp.getData(), 0, dp.getLength(), "UTF-8") +
//                        "from" + dp.getAddress().getHostAddress() + ":" + dp.getPort();
                ByteArrayInputStream bs=new ByteArrayInputStream(dp.getData());
                ObjectInputStream os=new ObjectInputStream(bs);
                TBSocketInfo m = (TBSocketInfo)os.readObject();
                //  System.out.println(m+"-----");
                System.out.println(m.toString());

                Log.e("2222", m.toString());

                updatetrack("Client: Succeed!\n" +m.toString());
//                Dssingle.getInstance().getDatagramSocket().close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        }

    }


    public void updatetrack(String s) {
        Message msg = new Message();
        String textTochange = s;
        msg.obj = textTochange;
        handler.sendMessage(msg);
    }
}