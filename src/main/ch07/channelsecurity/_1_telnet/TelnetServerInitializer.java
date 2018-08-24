package ch07.channelsecurity._1_telnet;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {
	
	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	
	private static final TelnetServerHandler SERVER_HANDLER = new TelnetServerHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline pipeline = ch.pipeline();
		// DelimiterBasedFrameDecoder: 네티가 제공하는 기본 디코더. 구분자 기반의 패킷을 처리
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		// 연결되는 새로운 클라이언트채널들은 동일한 인코더/디코더 객체를 공유하게 됨
		pipeline.addLast(DECODER);
		pipeline.addLast(ENCODER);
		pipeline.addLast(SERVER_HANDLER);
	}
	
}
