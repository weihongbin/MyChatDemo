package com.example.administrator.mychatdemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/27.
 */
public class Utils implements Runnable {
    /**
     * 断点发送文件
     * @param filepath 文件路径
     * @param startindex 文件已经发送大小
     * @param videoArrayList 需要发送的文件列表
     * @throws JSONException json异常
     * @throws IOException io异常
     */
    DatagramSocket client;
    boolean isSendFile=true;
     int soTimeIndex=0;
    String filepath;
    long startindex;
    ArrayList<VideoFilePath> videoArrayList;
    Handler handler;

    public Utils() {
    }

    public Utils(String filepath, long startindex, ArrayList<VideoFilePath> videoArrayList, Handler handler) {
        this.filepath = filepath;
        this.startindex = startindex;
        this.videoArrayList = videoArrayList;
        this.handler = handler;
    }

    /**
     * int转byte数组
     *

     * @param a
     * @param b
     * @param offset
     * @return
     */
    public static void int2Byte(int a, byte[] b, int offset) {
        b[offset++] = (byte) (a >> 24);
        b[offset++] = (byte) (a >> 16);
        b[offset++] = (byte) (a >> 8);
        b[offset++] = (byte) (a);
    }
    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param ary
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset]&0xFF)
                | ((ary[offset+1]<<8) & 0xFF00)
                | ((ary[offset+2]<<16)& 0xFF0000)
                | ((ary[offset+3]<<24) & 0xFF000000));
        return value;
    }
        /**
         * 断点接受文件
         * @param filepath 文件路径
         * @param startindex 文件已接受大小
         * @param totallen 文件总长度
         * @param videoArrayList 还需接受文件列表
         * @return 是否接受成功
         * @throws Exception
         */
    DatagramSocket datagramServer;
    @SuppressWarnings("resource")
    private boolean recevfileContinue(String filepath, long startindex,long totallen, ArrayList<VideoFilePath> videoArrayList)throws Exception {
        ArrayList<VideoFilePath> fileList = new ArrayList<VideoFilePath>();
        fileList.addAll(videoArrayList);
        ArrayList<byte[]> recevList = new ArrayList<byte[]>();
        boolean isSucc = true;
        Log.e("UDP", "recevFile---->startindex:" + startindex);
        Log.e("UDP", "recevFile---->totallen:" + totallen);
        // 清除上一次链接
        if (datagramServer != null && !datagramServer.isClosed()) {
            datagramServer.close();
            datagramServer = null;
        }
        Log.e("UDP", "接受文件 datagramServer.close");
        byte[] sendbufs = new byte[1];
        datagramServer = new DatagramSocket(8221);
        datagramServer.setReuseAddress(true);
//        datagramServer.bind(new InetSocketAddress(8221));
        datagramServer.setSoTimeout(2500);// 设置超时时间为20s
        Log.e("UDP", "接受文件 setSoTimeout");
        byte[] recvBuf = new byte[1];
        Log.e("UDP", "接受文件 recvBuf");
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        Log.e("UDP", "接受文件 recvPacket");
        datagramServer.receive(recvPacket);// 接受udp链接成功校验
        Log.e("UDP", "接受文件 receive");
        String recvStr = new String(recvPacket.getData(), 0,
                recvPacket.getLength());// 接受的数据转换为字符串
        Log.e("UDP", "接受文件 recvStr" + recvStr);
        while (!"C".equals(recvStr)) {// 是否为规定协议，不是，继续接受
            datagramServer.receive(recvPacket);
            recvStr = new String(recvPacket.getData(), 0,
                    recvPacket.getLength());
        }
        int port = recvPacket.getPort();// 获取链接端口
        InetAddress addr = recvPacket.getAddress();// 获取链接ip地址
        Log.e("UDP", "接受文件 port" + port + "   addr" + addr.toString());
        String sendStr = "S";// 发送链接回应
        byte[] sendBuf;
        sendBuf = sendStr.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length,
                addr, port);
        datagramServer.send(sendPacket);// 发送响应

        VideoFilePath videoFilePath = new VideoFilePath();
        videoFilePath.setVideoPath(filepath);//文件路径
        videoFilePath.setVideoAccess(startindex);//文件已经发送大小
        videoFilePath.setVideoLength(totallen);//文件总长度
        fileList.add(videoFilePath);//把文件加到接受列表中
        for (int v = 0; v < fileList.size(); v++) {
            VideoFilePath videoFile = fileList.get(v);
            String[] str = videoFile.getVideoPath().split("/");
            String videoPath =android.os.Environment.getExternalStorageDirectory()+ "//" + str[str.length - 1];
            File file = new File(videoPath);
            long tatol = -1;
            RandomAccessFile access = null;
            access = new RandomAccessFile(file, "rw");
            access.skipBytes((int) videoFile.getVideoAccess());
            tatol = videoFile.getVideoLength() - videoFile.getVideoAccess();

            Log.e("UDP", "接受文件 while");
            while (tatol > 0) {
                // 防止下面接受循环时 数组越界
                for (int arraySize = 0; arraySize < 20; arraySize++) {
                    recevList.add(arraySize, new byte[0]);
                }
                for (int i = 0; i < 20; i++) {
                    if (tatol <= 0) {
                        break;
                    }
                    recvBuf = new byte[(1024 * 5) + 4];//加上前标长度
                    recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                    try {
                        datagramServer.receive(recvPacket);
                    } catch (SocketTimeoutException e) {
                        Log.e("UDP", "接受文件时链接超时 e" + e.getLocalizedMessage());
                        if (datagramServer != null&& !datagramServer.isClosed()) {
                            datagramServer.close();
                            datagramServer = null;
                        }
                        return false;
                    }
                    Log.e("UDP", "接受文件 arraycopy file");
                    int reallen = recvPacket.getLength() - 4;//实际文件长度
                    Log.d("UDP", "接受文件 reallen == " + reallen);
                    if (reallen <= 0) {
                        return false;
                    }
                    tatol -= reallen;
                    byte[] recvBuffs = new byte[reallen];
                    Log.e("UDP", "接受文件 recvPacket.getData().length"
                            + recvPacket.getData().length);
                    System.arraycopy(recvPacket.getData(), 4, recvBuffs, 0,
                            reallen);
                    int index = this.bytesToInt(recvPacket.getData(), 0);//获取文件包前标
                    recevList.add(index, recvBuffs);//加入接受缓存数组等接受完毕20个一起写入
                    Log.e("UDP", "index   ；  " + index);
                }
                sendbufs[0] = (byte) 0x06;
                sendPacket = new DatagramPacket(sendbufs, sendbufs.length,addr, port);
                Log.e("UDP", "接受文件 send data");
                datagramServer.send(sendPacket);// 接受20个包成功，发送成功回执
                for (int re = 0; re < recevList.size(); re++) {//循环写入文件
                    byte[] buffs = recevList.get(re);
                    Log.e("UDP", "接受文件 write file");
                    access.write(buffs, 0, buffs.length);
                }
                recevList.clear();//清理缓存数组
                Log.d("recevfileContinue", "tatol == " + tatol);
            }
            access.close();
        }
        datagramServer.close();
        Log.d("recevfileContinue", "isSucc == " + isSucc);
        return isSucc;
    }

    @Override
    public void run() {
        try {
            long fileAccess = 0;
            if (client != null && !client.isClosed()) {
                client.close();
                client = null;
            }
            client = new DatagramSocket(9997);
            client.setSoTimeout(30 * 1000);// 设置超时时间为3s
            try {//首先和接收方握手
                String sendStr = "C";
                byte[] sendBuf;
                sendBuf = sendStr.getBytes();
                InetAddress addr = InetAddress.getByName("192.168.1.227");
                DatagramPacket sendPacket = new DatagramPacket(sendBuf,sendBuf.length, addr, 8221);
                client.send(sendPacket);
                Log.e("UDP", "UDP Control--> sendFile 发送C");
                byte[] recvBuf = new byte[1];
                DatagramPacket recvPacket = new DatagramPacket(recvBuf,0,recvBuf.length);
                client.receive(recvPacket);
                String recvStr = new String(recvPacket.getData(), 0,recvPacket.getLength());
                Log.e("UDP", "UDP Control--> sendFile 接受响应" + recvStr);
                while (!"S".equals(recvStr)) {
                    client.receive(recvPacket);
                    recvStr = new String(recvPacket.getData(), 0,recvPacket.getLength());
                }
                VideoFilePath zipFilePath = new VideoFilePath();// 把zip文件加入列表一起处理。
                zipFilePath.setVideoPath(filepath);//文件路径
                zipFilePath.setVideoAccess(startindex);//设置已经发送大小
                File zipFile = new File(filepath);
                if (!zipFile.exists()) {//判断文件是否存在
                    throw new FileNotFoundException();
                }
                zipFilePath.setVideoLength(zipFile.length());//获取文件总长度
                videoArrayList.add(zipFilePath);
                for (int v = 0; v < videoArrayList.size(); v++) {//循环发送多个文件
                    VideoFilePath videoFilePath = videoArrayList.get(v);
                    File file = new File(videoFilePath.getVideoPath());
                    if (!file.exists()) {
                        throw new FileNotFoundException();
                    }
                    RandomAccessFile access = new RandomAccessFile(file, "r");
                    // 缓冲区5KB
                    sendBuf = new byte[1024 * 5];
                    // 剩余要读取的长度
                    long tatol = videoFilePath.getVideoLength() - videoFilePath.getVideoAccess();
                    access.skipBytes((int) videoFilePath.getVideoAccess());
                    Log.e("UDP", "UDP Control--> 开始发送文件");
                    // 本次要读取的长度假设为剩余长度
                    int len = (int) (videoFilePath.getVideoLength() - videoFilePath.getVideoAccess());
                    // 如果本次要读取的长度大于缓冲区的容量
                    if (len > sendBuf.length) {
                        // 修改本次要读取的长度为缓冲区的容量
                        len = sendBuf.length;
                    }
                    fileAccess += videoFilePath.getVideoAccess();//已经发送文件长度（用于显示进度条）

                    while (tatol > 0 && isSendFile) {
                        int sendAccess = 0;
                        for (int i = 0; i < 20; i++) {
                            if (tatol < len) {
                                sendBuf = new byte[(int) tatol];
                                len = (int) tatol;
                            }
                            // 读取文件，返回真正读取的长度
                            int rlength = access.read(sendBuf, 0, len);
                            // 将剩余要读取的长度减去本次已经读取的
                            tatol -= rlength;
                            sendAccess += rlength;
                            // 如果本次读取个数不为0则写入输出流，否则结束
                            if (rlength > 0) {
                                byte[] sendIndexBuf = new byte[rlength + 4];
                                System.arraycopy(sendBuf, 0, sendIndexBuf, 4,rlength);//把读取的文件包写入新的byte数组
                                this.int2Byte(i, sendIndexBuf, 0);//给新的数组写入文件包前标 0-20
                                // 将本次读取的写入输出流中
                                sendPacket = new DatagramPacket(sendIndexBuf,sendIndexBuf.length, addr, 8221);
                                client.send(sendPacket);
                            }
                            if (tatol <= 0) {//本个文件发送完毕
                                break;
                            }
                        }
                        recvBuf = new byte[1];
                        recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                        client.receive(recvPacket);//接受回执，是否20个包接受成功
                        soTimeIndex = 0;// 初始化超时次数
                        // 发送已经发送文件进度
                        fileAccess += sendAccess;
                        Message startMsg = new Message();
                        startMsg.arg1 =1;
                        startMsg.obj = fileAccess;
                        if (tatol <= 0 && v == videoArrayList.size() - 1) {// 最后一个文件发送完毕
                            startMsg.arg1 =2;
                        }
                        handler.sendMessage(startMsg);
                    }
                    access.close();
                }
                // 如果剩余长度小于等于0则结束
                client.close();
            } catch (SocketTimeoutException e) {
                //如果连续5次链接超时，则认定链接失败。
                if (isSendFile) {
                    Message msg = handler.obtainMessage();
                    soTimeIndex++;
                    if (soTimeIndex >= 5) {
                        soTimeIndex = 0;
                        msg.arg1 = 3;
                    } else {
                        msg.arg1 = 4;
                    }
                    handler.sendMessage(msg);
                    client.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}







//    /**
//     * 判断是否为断点续传
//     *
//     * @param fileName
//     *            文件名字
//     * @param md5
//     *            文件md5
//     * @return 文件已经存在长度
//     */
//    private long sendFileContinue(String fileName, String md5,
//                                  ArrayList<VideoFilePath> videoArray) {
//        // 判断文件是否存在
//        String filepath = FileHelper.TEMP_DIR + "//" + fileName;
//        String textFilePath = FileHelper.TEMP_DIR + "//fileText.xml";
//        File textFile = new File(textFilePath);
//        File file = new File(filepath);
//        try {
//            for (int v = 0; v < videoArray.size(); v++) {// 查询已经接受视频的大小
//                VideoFilePath videoFilePath = videoArray.get(v);
//                String[] str = videoFilePath.getVideoPath().split("/");
//                String videoPath = FileHelper.TEMP_DIR + "//"
//                        + str[str.length - 1];
//                File videoFile = new File(videoPath);
//                if (videoFile.exists()) {
//                    videoFilePath.setVideoAccess(videoFile.length());
//                } else {
//                    videoFilePath.setVideoAccess(0);
//                }
//            }
//            if (!textFile.exists()) {// 管理文件是否存在
//                textFile.createNewFile();
//            }
//            String textStr = FileHelper.readFile(textFilePath);
//            ArrayList<SendFileText> sendFileTextArrayList = Json
//                    .analysisFileText(textStr);
//            if (file.exists()) {// 如果存在，读取文件管理文本，查看md5是否一致
//                for (int i = 0; i < sendFileTextArrayList.size(); i++) {
//                    SendFileText sendFileText = sendFileTextArrayList.get(i);
//                    if (fileName.equals(sendFileText.getFileName())) {
//                        if (md5.equals(sendFileText.getFileMD5())) {
//                            return FileHelper.fileSize(filepath);
//                        } else {
//                            sendFileTextArrayList.remove(sendFileText);
//                            break;
//                        }
//                    }
//                }
//                FileHelper.deleteFile(filepath);
//                SendFileText addSendFileText = new SendFileText();
//                addSendFileText.setFileMD5(md5);
//                addSendFileText.setFileName(fileName);
//                sendFileTextArrayList.add(addSendFileText);
//                FileHelper.writeFile(textFilePath,
//                        Json.structureFileText(sendFileTextArrayList));
//                return 0;
//            } else {// 如果不存在，把文件管理文本内相同文件名字的字段删除，并且写入新的文件字段，return 0.
//                for (int i = 0; i < sendFileTextArrayList.size(); i++) {
//                    SendFileText sendFileText = sendFileTextArrayList.get(i);
//                    if (fileName.equals(sendFileText.getFileName())
//                            || md5.equals(sendFileText.getFileMD5())) {
//                        sendFileTextArrayList.remove(sendFileText);
//                        break;
//                    }
//                }
//                SendFileText addSendFileText = new SendFileText();
//                addSendFileText.setFileMD5(md5);
//                addSendFileText.setFileName(fileName);
//                sendFileTextArrayList.add(addSendFileText);
//                FileHelper.writeFile(textFilePath,
//                        Json.structureFileText(sendFileTextArrayList));
//                return 0;
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return 0;
//        }
//    }