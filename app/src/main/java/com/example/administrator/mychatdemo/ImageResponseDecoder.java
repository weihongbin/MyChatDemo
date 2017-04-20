/*
package com.example.administrator.mychatdemo;

import java.io.ByteArrayInputStream;

*/
/**
 * Created by Administrator on 2016/8/8.
 *//*


    public class ImageResponseDecoder extends CumulativeProtocolDecoder {
        */
/**
         * 返回值的解释：
         * 1、false, 继续接收下一批数据，有两种情形，如缓冲区数据刚刚就是一个完整消息，或不够一条消息时。如果不够一条消息，那么会将下一批数据和剩余消息进行合并
         * 2、true, 当缓冲区的消息多于一条消息时，剩余消息会再会推送至doDecode
         *//*

        protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)throws Exception {
//发送数据时，头四个字节记录了消息的长度。 此方法会读四个字节，并和实现流长度对比。返回前，将流reset.
            if (in.prefixedDataAvailable(4)) {
                int length = in.getInt();
                byte [] bytes =newbyte[length];
                in.get(bytes);
                ByteArrayInputStream bais =new ByteArrayInputStream(bytes);
                BufferedImage image = ImageIO.read(bais);
                out.write(image);
                return true;//如果读取内容后还粘了包，系统会自动处理。
            }else{
                returnfalse;//继续接收数据，以待数据完整
            }
        }
    }


*/
