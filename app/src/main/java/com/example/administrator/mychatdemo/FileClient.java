package com.example.administrator.mychatdemo;



import android.util.Log;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class FileClient implements Runnable {
	public static final  String TAG=FileClient.class.getSimpleName();
	private static final String SEND_FILE_PATH = "/storage/emulated/0"+ "/"+"我的.png";
//	private static final String SEND_FILE_PATH = android.os.Environment.getExternalStorageDirectory() + "/"+"我的.png";sd卡
	long startTime = System.currentTimeMillis();

	byte[] buf = new byte[UDPUtils.BUFFER_SIZE];
	byte[] receiveBuf = new byte[1];

	RandomAccessFile accessFile = null;
	DatagramPacket dpk = null;
	DatagramSocket dsk = null;

	@Override
	public void run() {
		int readSize = -1;
		try {
			accessFile = new RandomAccessFile(SEND_FILE_PATH,"r");
			dpk = new DatagramPacket(buf, buf.length,new InetSocketAddress(InetAddress.getByName("192.168.1.227"), UDPUtils.PORT));
			dsk = new DatagramSocket();
			int sendCount = 0;

				Log.e(TAG,"start client message");
				dpk.setData(UDPUtils.start,0,UDPUtils.start.length);
				dsk.send(dpk);
				dpk.setData(receiveBuf,0,receiveBuf.length);
				dsk.receive(dpk);
				// byte[] receiveData = dpk.getData();
				if(!UDPUtils.isEqualsByteArray(UDPUtils.ok, receiveBuf, dpk.getLength())){

						Log.e(TAG,"exit");


				}else{
					while((readSize = accessFile.read(buf,0,buf.length)) != -1){
						dpk.setData(buf, 0, readSize);
						dsk.send(dpk);
						// wait server response
						{
							while(true){
								dpk.setData(receiveBuf, 0, receiveBuf.length);
								dsk.receive(dpk);
								// confirm server receive
								if(!UDPUtils.isEqualsByteArray(UDPUtils.successData,receiveBuf,dpk.getLength())){
									Log.e(TAG, "resend ...");
									dpk.setData(buf, 0, readSize);
									dsk.send(dpk);
								}else
									break;
							}
						}
						Log.e(TAG, "send count of "+(++sendCount)+"!");

					}
					// send exit wait server response
					while(true){
						Log.e(TAG, "client send exit message ....");
						dpk.setData(UDPUtils.exitData,0,UDPUtils.exitData.length);
						dsk.send(dpk);
						dpk.setData(receiveBuf,0,receiveBuf.length);
						Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
						dsk.receive(dpk);
						// byte[] receiveData = dpk.getData();
						if(!UDPUtils.isEqualsByteArray(UDPUtils.exitData, receiveBuf, dpk.getLength())){
							Log.e(TAG,"client Resend exit message ....");
							dsk.send(dpk);
						}else
							break;
					}
				}

		}catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(accessFile != null)
					accessFile.close();
				if(dsk != null)
					dsk.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		long endTime = System.currentTimeMillis();
		Log.e(TAG,"time:"+(endTime - startTime));
	}
}
