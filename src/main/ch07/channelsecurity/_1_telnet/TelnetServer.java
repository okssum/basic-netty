package ch07.channelsecurity._1_telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TelnetServer {
	
	private static final int listenPort = 8888;
	
	public static void main(String[] args) throws Exception {
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
			 // 텔넷 서버의 패널 파이프라인 설정 코드가 작성된 TelnetServerInitializer 클래스를 부트스트랩에 지정
			 .childHandler(new TelnetServerInitializer());
			
			// 부트스트랩에 텔넷 서비스 포트를 지정하고 서버 소켓에 바인딩된 채널이 종료될 때까지 대기하도록 설정
			// ChannelFuture 인터페이스의 sync() : 지정한 Future 객체의 동작이 완료될 때까지 대기하는 메서드
			b.bind(listenPort).sync().channel().closeFuture().sync();
			
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
}
