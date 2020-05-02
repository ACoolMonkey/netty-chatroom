package com.hys.netty.chatroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端
 *
 * @author Robert Hou
 * @date 2020年05月02日 05:31
 **/
@Slf4j
public class Server {

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup("shutdownGracefully") EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        @Cleanup("shutdownGracefully") EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MessageEncoder());
                        pipeline.addLast(new MessageDecoder());
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });
        log.info("聊天室server启动。。。");
        ChannelFuture cf = bootstrap.bind(9000).sync();
        cf.channel().closeFuture().sync();
    }
}
