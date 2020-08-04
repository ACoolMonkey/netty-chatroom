package com.hys.netty.chatroom;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义服务端Handler
 *
 * @author Robert Hou
 * @date 2020年05月02日 05:31
 **/
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) {
        String content = new String(msg.getContent(), CharsetUtil.UTF_8);
        Channel channel = ctx.channel();
        CHANNEL_GROUP.forEach(ch -> {
            String ct;
            if (ch == channel) {
                ct = "[ 自己 ] 发送了消息：" + content;
            } else {
                ct = "[ 客户端 " + channel.remoteAddress() + " ] 发送了消息：" + content;
            }
            byte[] bytes = ct.getBytes(CharsetUtil.UTF_8);
            MessageProtocol messageProtocol = MessageProtocol.builder().length(bytes.length).content(bytes).build();
            ch.writeAndFlush(messageProtocol);
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        //将该客户加入聊天的信息推送给其它在线的客户端
        String content = "[ 客户端 " + address + " ] 上线了 " + sdf.format(new Date());
        byte[] bytes = content.getBytes(CharsetUtil.UTF_8);
        MessageProtocol messageProtocol = MessageProtocol.builder().length(bytes.length).content(bytes).build();
        CHANNEL_GROUP.writeAndFlush(messageProtocol);
        //将当前channel加入到channelGroup
        CHANNEL_GROUP.add(channel);
        if (log.isInfoEnabled()) {
            log.info(address + " 上线了");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        String content = "[ 客户端 " + address + " ] 下线了";
        byte[] bytes = content.getBytes(CharsetUtil.UTF_8);
        MessageProtocol messageProtocol = MessageProtocol.builder().length(bytes.length).content(bytes).build();
        CHANNEL_GROUP.writeAndFlush(messageProtocol);
        if (log.isInfoEnabled()) {
            log.info(address + " 下线了");
            log.info("当前客户端数量：" + CHANNEL_GROUP.size());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
