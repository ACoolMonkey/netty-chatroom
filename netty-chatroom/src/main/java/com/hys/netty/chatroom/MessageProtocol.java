package com.hys.netty.chatroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义协议包
 *
 * @author Robert Hou
 * @date 2020年05月02日 23:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageProtocol implements Serializable {

    private static final long serialVersionUID = 609914401677840006L;
    /**
     * 一次发送包体长度
     */
    private int length;
    /**
     * 一次发送包体内容
     */
    private byte[] content;
}
