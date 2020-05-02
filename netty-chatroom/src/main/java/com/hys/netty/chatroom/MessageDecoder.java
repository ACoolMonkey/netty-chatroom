package com.hys.netty.chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 接收包解码
 *
 * @author Robert Hou
 * @date 2020年05月02日 23:15
 **/
public class MessageDecoder extends ByteToMessageDecoder {

    private int length = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return;
        }
        if (length == 0) {
            length = in.readInt();
        }
        if (in.readableBytes() < length) {
            //当前可读数据不够，继续等待
            return;
        }
        byte[] content = new byte[length];
        in.readBytes(content);
        //封装成MessageProtocol对象，传递到下一个handler业务处理
        MessageProtocol messageProtocol = MessageProtocol.builder().length(length).content(content).build();
        out.add(messageProtocol);
        length = 0;
    }
}
