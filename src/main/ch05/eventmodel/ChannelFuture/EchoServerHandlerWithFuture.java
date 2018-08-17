package ch05.eventmodel.ChannelFuture;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 클라이언트에서 수신한 데이터 돌려주고 전송한 데이터 크기를 출력한 다음 클라이언트 채널 닫기
 * 
 * ChannelInboundHandlerAdapter : 입력된 데이터 처리하는 이벤트 핸들러
 * SimpleChannelInboundHandler 클래스는 ChannelInboundHandlerAdapter 상속 받음
 */
public class EchoServerHandlerWithFuture extends ChannelInboundHandlerAdapter {
    
    // 클라이언트로부터 수신이 이루어졌을 때 자동으로 호출하는 이벤트 메서드
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        // 수신된 데이터를 클라이언트 소켓 버퍼에 기록하고 
        // 버퍼의 데이터를 채널로 전송하는 비동기 메서드인 writeAndFlush를 호출하고 
        // ChannelFuture 객체를 돌려받는다.
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
        
        // 데이터 크기 얻기
        final int writeMessageSize = ((ByteBuf)msg).readableBytes();
        
        // 사용자 정의 채널 리스너 생성
        channelFuture.addListener(new ChannelFutureListener() {
            // ChannelFuture 객체에서 발생하는 작업 완료 이벤트 메서드
            // 사용자 정의 채널 리스너 구현에 포함되어야 함
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("전송한 Bytes : " + writeMessageSize);
                future.channel().close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        
        cause.printStackTrace();
        ctx.close();
    }
    
}
