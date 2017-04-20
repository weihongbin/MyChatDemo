package com.example.hellow;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class TestUDPClient {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		DatagramChannel channel=DatagramChannel.open();
		
		String newData="hello,itbuluoge!"+System.currentTimeMillis();
		ByteBuffer buf=ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();
		/*发送UDP数据包*/
		int bytesSent=channel.send(buf, new InetSocketAddress("127.0.0.1",9999));

	}

}
