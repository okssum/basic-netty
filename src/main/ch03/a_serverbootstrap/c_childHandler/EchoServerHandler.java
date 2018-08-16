package ch03.a_serverbootstrap.c_childHandler;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandlerAdapter : 입력된 데이터 처리하는 이벤트 핸들러
 * SimpleChannelInboundHandler 클래스는 ChannelInboundHandlerAdapter 상속 받음
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception { // 클라이언트로부터 수신이 이루어졌을 때 자동으로 호출하는 이벤트 메서드
        
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        
        System.out.println("수신한 문자열 [" + readMessage + ']');
        
        ctx.write(msg); // ChannelHandlerContext로 채널 파이프라인에 대한 이벤트를 처리함
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        
        ctx.flush();
    }
    
}
