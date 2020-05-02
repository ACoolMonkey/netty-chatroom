package com.hys.netty.chatroom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义客户端Handler
 *
 * @author Robert Hou
 * @date 2020年05月02日 05:32
 **/
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) {
        log.info(new String(msg.getContent(), CharsetUtil.UTF_8));
    }
}
