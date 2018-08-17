package ch05.eventmodel.ChannelFuture;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandlerAdapter : 입력된 데이터 처리하는 이벤트 핸들러
 * SimpleChannelInboundHandler 클래스는 ChannelInboundHandlerAdapter 상속 받음
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    
    // 클라이언트로부터 수신이 이루어졌을 때 자동으로 호출하는 이벤트 메서드
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        // 수신된 데이터를 클라이언트 소켓 버퍼에 기록하고 
        // 버퍼의 데이터를 채널로 전송하는 비동기 메서드인 writeAndFlush를 호출하고 
        // ChannelFuture 객체를 돌려받는다.
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
        // 채널을 종료하는 리스너 등록
        // ChannelFuture 객체가 완료 이벤트를 수신할 때 수행됨
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        
        cause.printStackTrace();
        ctx.close();
    }
    
}
