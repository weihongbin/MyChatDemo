package com.example.administrator.mychatdemo;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/8/8.
 *
 *
 * 下载文件
 */
class DownloadThred extends Thread {
    Socket socket;
    InputStream in;
    OutputStream out;



    String functionName;
    String serverIp = "192.168.1.195";
    int socketPort = 8080;
    int fileSize,downLoadFileSize;
    public DownloadThred(String functionName) {
        this.functionName = functionName;
    }
    @Override
    public void run() {
        Looper.prepare();
        while(!Thread.interrupted()) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(serverIp, socketPort), 100000);
                Log.e(">>>>>>>" ,socket.isConnected()+"");
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                out.write((functionName + "\n").getBytes("gbk"));
                out.flush(); // 清理缓冲，确保发送到服务端

                DataInputStream dis = new DataInputStream(
                        new BufferedInputStream(in));
                String path =  Environment.getExternalStoragePublicDirectory("我的").getPath();
                fileSize = (int) dis.readLong() - 1;
                String filename=dis.readUTF();
                String prefix=filename.substring(filename.lastIndexOf("."));
                Log.e(">>>>>>>" ,prefix);
                File f = new File(path+prefix);
                Log.e(">>>>>>>" ,f.getAbsolutePath());
                OutputStream song = new FileOutputStream(f);
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(song));
                System.out.println("开始下载");
                Log.e(">>>>>>>" ,"开始下载+");
                Log.e(">>>>>>>" ,fileSize+"");
                byte[] buffer = new byte[1024*1024];
                while (true) {
                    int read = 0;
                    if (dis != null) {
                        read = dis.read(buffer);
                        downLoadFileSize += read;
                    }
                    if (read == -1) {
                        break;
                    }
                    dos.write(buffer, 0, read);
                    Log.e(">>>>>>>" ,buffer.toString()+"");
                }

                Log.e(">>>>>>>" ,"文件下载完成+");
                dos.flush();
                dos.close();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                this.interrupt();
            }
        }
    }
}