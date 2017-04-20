package com.example.administrator.mychatdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Administrator on 2016/8/8.
 */
public class AgainConnection {
    private Socket s=null;
    private InetSocketAddress remoteAddr;
    private void start(String severIP, int serverPort){

        s=new Socket();
        try {
            s.setReuseAddress(true);
            remoteAddr = new InetSocketAddress(severIP,
                    serverPort);
            try {
                s.connect(remoteAddr);
                while(!s.isConnected()){
                    try {
                        Thread.sleep(1000);
                        s.connect(remoteAddr);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }
}
