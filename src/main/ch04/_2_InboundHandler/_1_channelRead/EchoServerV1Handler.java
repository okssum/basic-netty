package ch04._2_InboundHandler._1_channelRead;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandlerAdapter : 입력된 데이터 처리하는 이벤트 핸들러
 * SimpleChannelInboundHandler 클래스는 ChannelInboundHandlerAdapter 상속 받음
 * 
 * 데이터 수신 발생 시 수신한 데이터 출력 후 그 데이터를 상대방에게 그대로 돌려주기
 */
public class EchoServerV1Handler extends ChannelInboundHandlerAdapter {
    
    // 클라이언트로부터 수신이 이루어졌을 때 자동으로 호출하는 이벤트 메서드
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        // 네티 내부는 모든 데이터가 ByteBuf로 관리됨
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        
        System.out.println("수신한 문자열 [" + readMessage + ']');
        
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        
        cause.printStackTrace();
        ctx.close();
    }
    
}
