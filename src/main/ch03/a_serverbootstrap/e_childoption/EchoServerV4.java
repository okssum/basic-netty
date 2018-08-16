package ch03.a_serverbootstrap.e_childoption;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author  sunok
 * @Comment childOption() - 클라이언트 소켓 채널의 소켓 옵션 설정
 */
public class EchoServerV4 {

    public static void main(String[] args) throws Exception {
        
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 커널 버퍼에 남은 데이터를 상대방 소켓 채널로 모두 적송하고 즉시 연결을 끊음
                // TIME_WAIT 발생하지 않는 장점이 있지만, 마지막으로 전송한 데이터가 클라이언트로 모두 전송되었는지 확인할 방법이 없음
                .childOption(ChannelOption.SO_LINGER, 0)
                .childHandler(
                    new ChannelInitializer<SocketChannel>() {
                        
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            
                            p.addLast(new EchoServerHandler());
                        }
                    }
                );
            
            ChannelFuture f = b.bind(8888).sync();
            
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
