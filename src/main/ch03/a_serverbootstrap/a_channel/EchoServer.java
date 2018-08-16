package ch03.a_serverbootstrap.a_channel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    public static void main(String[] args) throws Exception {
        
        // EventLoopGroup 인터페이스에 NioEventLoopGroup 클래스의 객체를 할당
        // 클라이언트 연결을 수락하는 부모 스레드 그룹
        // 1 : 최대 스레드 수. 단일 스레드
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 연결된 클라이언트의 소켓으로부터 데이터 입출력 및 이벤트 처리를 담당하는 자식 스레드 그룹
        // 인수 없는 생성자 : 서버 애플리케이션이 동작하는 하드웨어 코어 수를 기준으로 사용할 스레드 수 설정. CPU 코어 수의 2배 사용
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                // 서버 소켓(부모 스레드)이 사용할 네트워크 입출력 모드 설정
                .channel(NioServerSocketChannel.class)
                // 자식 채널의 초기화 방법 설정
                .childHandler(
                    // 클라이언트로부터 연결된 채널이 초기화될 때 기본 동작이 지정된 추상 클래스
                    new ChannelInitializer<SocketChannel>() {
                        
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            
                            // 클라이언트 연결이 생성되었을 때 데이터 처리를 담당
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
