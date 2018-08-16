package ch03.a_serverbootstrap.a_channel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;

/**
 * 
 * @author  sunok
 * @Comment EchoServer.class 넌블로킹 -> epoll로 변경
 *          Epoll 입출력 보드는 리눅스에서만 동작함
 */
public class EpollEchoServer {

    public static void main(String[] args) throws Exception {
        
        EventLoopGroup bossGroup = new EpollEventLoopGroup(1); // NioEventLoopGroup -> EpollEventLoopGroup 로 변경
        EventLoopGroup workerGroup = new EpollEventLoopGroup(); // NioEventLoopGroup -> EpollEventLoopGroup 로 변경
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(EpollServerSocketChannel.class) // NioServerSocketChannel -> EpollServerSocketChannel 로 변경
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
