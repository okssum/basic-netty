package ch05.eventmodel.ChannelFuture;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServerWithFuture {

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
            
//            ChannelFuture f = b.bind(8888).sync();
//            
//            f.channel().closeFuture().sync();
            
            /* ChannelFuture 풀어서 코딩 start */
            // ChannelFuture : 채널 I/0의 비동기 호출을 지원하고자 제공
            // 8888 포트를 사용하도록 바인드하는 비동기 bind 메서드 호출
            // 포트 바인딩이 안료되기 전에 ChannelFuture 객체를 돌려줌
            ChannelFuture bindFuture = b.bind(8888);
            // 주어진 ChannelFuture 객체의 작업이 완료될 때까지 블로킹하는 메서드
            // bind 메서드 처리가 완료될 때 sync 메서드도 같이 완료됨
            bindFuture.sync();
            
            // bindFuture 객체를 통해서 채널을 얻어옴
            // 8888 포트에 바인딩된 서버 채널임
            Channel serverChannel = bindFuture.channel();
            // 바인드가 완료된 서버 채널의 CloseFuture 객체를 돌려줌
            // 네티 내부에서는 채널이 생성될 때 CloseFuture 객체도 같이 생성되므로 closeFuture 메서드가 돌려주는 CloseFuture 객체는 항상 동일한 객체임
            ChannelFuture closeFuture = serverChannel.closeFuture();
            // CloseFuture 객체는 채널의 연결이 종료될 때 연결 종료 이벤트를 받음
            // 채널이 생성될 때 같이 생성되는 기본 CloseFuture 객체에는 아무 동작도 설정되어 있지 않으므로
            // 이벤트를 받았을 때 아무 동작도 하지 않는다.
            closeFuture.sync();
            /* ChannelFuture 풀어서 코딩 end */
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
