package com.hys.netty.chatroom;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * 客户端代码
 *
 * @author Robert Hou
 * @date 2020年05月02日 05:32
 **/
@Slf4j
public class Client {

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup("shutdownGracefully") EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new StringDecoder());
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
        Channel channel = channelFuture.channel();
        if (log.isInfoEnabled()) {
            log.info("==========" + channel.localAddress() + "==========");
        }
        //客户端输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            //通过channel发送到服务器端
            channel.writeAndFlush(msg);
        }
        channelFuture.channel().closeFuture().sync();
    }
}
