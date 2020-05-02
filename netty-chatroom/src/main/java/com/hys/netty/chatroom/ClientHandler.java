package com.hys.netty.chatroom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义客户端Handler
 *
 * @author Robert Hou
 * @date 2020年05月02日 05:32
 **/
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        log.info(msg.trim());
    }
}
