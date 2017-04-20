package com.example.administrator.mychatdemo;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
 * 上传文件
 */
class UploadThred extends Thread {
    Socket socket;
    InputStream in;
    OutputStream out;
    String path =  Environment.getExternalStoragePublicDirectory("我的").getPath();


    String functionName;
    String serverIp = "192.168.1.195";
    int socketPort = 8888;
    int fileSize,downLoadFileSize;
    public UploadThred(String functionName) {
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
                File file=new File(path+".mp3");
                Log.e(">>>>>>>" ,file.getAbsolutePath()+"");
                Log.e(">>>>>>>" ,file.getName()+"");
                DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(path+".mp3")));
                DataOutputStream ps = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//                将文件名及长度传给客户端。这里要//真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In Java 4th里有现成的代码。
                ps.writeUTF(file.getName());
                ps.flush();

                Log.e(">>>>>>>" ,file.getName()+"");

                ps.writeLong((long) file.length());
                ps.flush();
                int bufferSize = 8192;
                byte[] buf = new byte[1024*1024];
                while (true) {
                    int read = 0;
                    if (fis != null) {
                        read = fis.read(buf);
                    }
                    if (read == -1) {
                        break;
                    }
                    ps.write(buf, 0, read);
                }
                ps.flush();
                // 注意关闭socket链接哦，不然客户端会等待server的数据过来，
                // 直到socket超时，导致数据不完整。
                fis.close();
                ps.close();
//                s.close();

                Log.e(">>>>>>>" ,file.getName()+"文件传输完成");


              /*  InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                out.write((functionName + "\n").getBytes("gbk"));
                out.flush(); // 清理缓冲，确保发送到服务端

                DataInputStream dis = new DataInputStream(
                        new BufferedInputStream(in));

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
                */
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                this.interrupt();
            }
        }
    }
}