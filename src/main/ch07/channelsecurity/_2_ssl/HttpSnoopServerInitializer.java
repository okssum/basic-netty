package ch07.channelsecurity._2_ssl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

public class HttpSnoopServerInitializer extends ChannelInitializer<SocketChannel> {
	
	private final SslContext sslCtx;
	
	public HttpSnoopServerInitializer(SslContext sslCtx) {
		
		this.sslCtx = sslCtx;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline p = ch.pipeline();
		
		// 새로운 채널이 생성될 때마나 SslHandler를 넘겨줌
		if (sslCtx != null) {
			p.addLast(sslCtx.newHandler(ch.alloc()));
		}
		p.addLast(new HttpRequestDecoder());
		p.addLast(new HttpObjectAggregator(1048576));
		p.addLast(new HttpResponseEncoder());
		p.addLast(new HttpSnoopServerHandler());
	}
	
}
