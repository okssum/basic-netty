package ch04._2_InboundHandler._3_multi;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandlerAdapter : 입력된 데이터 처리하는 이벤트 핸들러
 * SimpleChannelInboundHandler 클래스는 ChannelInboundHandlerAdapter 상속 받음
 * 
 * 데이터 수신 발생 시 수신한 데이터 출력 후 그 데이터를 상대방에게 그대로 돌려주기
 */
public class EchoServerV3SecondHandler extends ChannelInboundHandlerAdapter {
    
    // 채널의 데이터를 다 읽어서 더 이상 데이터가 없을 때 발생
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        
        System.out.println("channelReadComplete 발생");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        
        cause.printStackTrace();
        ctx.close();
    }
    
}
