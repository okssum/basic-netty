package ch01.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author  sunok
 * @Comment 에코 서버 8888 포트로 접속하여 "Hello, Netty!" 문자열을 전송하고 서버의 응답을 수신하기
 */
public class EchoClient {

    public static void main(String[] args) throws Exception {
        
        EventLoopGroup group = new NioEventLoopGroup();
        
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new EchoClientHandler());
                    }
                });
            
            ChannelFuture f = b.connect("localhost", 8888).sync(); // 비동기 입출력 메서드 connect 호출
            // sync()는 ChannelFuture 객체의 요청이 완료될 때까지 대기함. 실패 시 예외를 던짐
            // 즉, connect() 처리가 완료될 때까지 다음 라인으로 진행하지 않음
            
            f.channel().closeFuture().sync();
        }
        finally {
            group.shutdownGracefully();
        }
    }

}
