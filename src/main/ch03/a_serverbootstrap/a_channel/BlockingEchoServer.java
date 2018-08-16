package ch03.a_serverbootstrap.a_channel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

/**
 * 
 * @author  sunok
 * @Comment EchoServer.class 넌블로킹 -> 블로킹으로 변경
 */
public class BlockingEchoServer {

    public static void main(String[] args) throws Exception {
        
        EventLoopGroup bossGroup = new OioEventLoopGroup(1); // NioEventLoopGroup -> OioEventLoopGroup 로 변경
        EventLoopGroup workerGroup = new OioEventLoopGroup(); // NioEventLoopGroup -> OioEventLoopGroup 로 변경
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(OioServerSocketChannel.class) // NioServerSocketChannel -> OioServerSocketChannel 로 변경
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
