package ch06.bytebuffer._2_netty.ByteBufAllocator;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 네티 바이트 버퍼 풀은 네티 애플리케이션의 서버 소켓 채널이 초기화될 때 같이 초기화되며
 * ChannelHandlerContext 인터페이스의 alloc()로 생성된 바이트 버퍼 풀을 참조할 수 있음
 * 
 * ChannelInboundHandlerAdapter : 입력된 데이터 처리하는 이벤트 핸들러
 * SimpleChannelInboundHandler 클래스는 ChannelInboundHandlerAdapter 상속 받음
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	// 클라이언트로부터 수신이 이루어졌을 때 자동으로 호출하는 이벤트 메서드
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        ByteBuf readMessage = (ByteBuf) msg;
        System.out.println("channelRead : " + readMessage.toString(Charset.defaultCharset()));
        
        // ChannelHandlerContext을 통해서 네티 프레임워크에서 초기화된 ByteBufAllocator를 참조할 수 있음
        // ByteBufAllocator : 바이트 버퍼 풀을 관리하는 인터페이스. 플랫폼의 지원 여부에 따라 다이렉트 버퍼와 힙 버퍼 풀을 생성함
        // 						기본적으로 다이렉트 버퍼 풀 생성
        ByteBufAllocator byteBufAllocator = ctx.alloc();
        // newBuffer 생성
        // ByteBufAllocator의 buffer()를 사용하여 바이트 버퍼는 ByteBufAllocator의 풀에서 관리되며
        // 바이트 버퍼를 채널에 기록하거나 명시적으로 release 메서드를 호출하면 바이트 버퍼 풀로 돌아감
        ByteBuf newBuffer = byteBufAllocator.buffer();
        
        // write 메서드의 인수로 바이트 버퍼가 입력되면 데이터를 채널에 기록하고 난 뒤에 버퍼 풀로 돌아감
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        
        ctx.flush();
    }
    
}
