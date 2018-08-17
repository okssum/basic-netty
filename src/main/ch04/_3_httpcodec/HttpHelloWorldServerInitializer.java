package ch04._3_httpcodec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
    
    private final SslContext sslCtx;
    
    public HttpHelloWorldServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        // 네티가 제공하는 HTTP 서버 코덱. 인바운드, 아웃바운드 핸들러를 모두 구현함
        // 즉, HttpServerCodec 생성자에서 HttpRequestDecoder, HttpResponseEncoder를 모두 생성
        // 간단한 웹 서버를 생성하는데 사용되는 코덱으로서 수신된 ByteBuf 객체를 HttpRequest, HttpContent 객체로 변환하고
        // HttpResponse 객체를 ByteBuf로 인코딩하여 송신함
        p.addLast(new HttpServerCodec());
        // HttpServerCodec이 수신한 이벤트와 데이터를 처리하여 Http 객체로 변환한 다음
        // channelRead 이벤트를 HttpHelloWorldServerHandler 클래스로 전달함
        p.addLast(new HttpHelloWorldServerHandler());
    }
    
}
