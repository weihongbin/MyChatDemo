package util;


import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Dssingon {
    private DatagramSocket datagramSocket=null;

    private static Dssingon instance = new Dssingon();

    private Dssingon() {
    }

    public static Dssingon getInstance() {
        return instance;
    }

    public DatagramSocket getDatagramSocket(int port) {
//双锁模式的单利，线程安全
        try {
            if(datagramSocket==null){
                synchronized (Dssingon.class){
                    if(datagramSocket==null) {
                        datagramSocket = new DatagramSocket(port);
                    }
                }

            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return datagramSocket;
    }
}