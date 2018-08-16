package ch01.echo;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    // 소켓 채널이 최초 활성화 되었을 때 실행
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String sendMessage = "Hello, Netty!";
        
        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());
        
        StringBuilder builder = new StringBuilder();
        builder.append("전송할 문자열 [");
        builder.append(sendMessage);
        builder.append("]");
        
        System.out.println(builder.toString());
        
        // 채널에 데이터를 기록하는 write() and 채널에 기록된 데이터를 서버로 전송하는 flush()
        ctx.writeAndFlush(messageBuffer);
    }
    
    // 서버로부터 수신된 데이터가 있을 때 호출되는 네티 이벤트 메시지
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        
        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");
        
        System.out.println(builder.toString());
    }

    // 수신된 데이터를 모두 읽었을 때 호출되는 이벤트 메서드
    // channelRead 메서드 수행 완료 후 자동으로 호출 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        
        cause.printStackTrace();
        ctx.close();
    }
    
}
