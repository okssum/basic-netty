package ch07.channelsecurity._1_telnet;

import java.net.InetAddress;
import java.util.Date;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

// @Sharable
// 네티가 제공하는 공유가능 상태표시 어노테이션
// Sharable로 지정된 클래스를 채널 파이프라인에서 공유할 수 있다는 의미. 즉, 다중 스레드에서 스레드 경합 없이 참조가 가능함
// @Sharable이 지정된 대표적인 클래스는 StringDecoder, StringEncoder가 있으며 대부분 codec 패키지에 속함
//
// SimpleChannelInboundHandler<String>
// 여기에 지정된 제너릭 타입은 데이터 수신 이벤트인 channelRead0 메서드의 두번째 인수 데이터형이 됨(수신된 데이터가 String 데이터임을 의미)
@Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {
	
	// 채널이 생성된 다음 바로 호출되는 이벤트
	// 클라이언트가 서버에 접속되면 네티의 채널이 생성되고 해당 채널이 활성화되는데 이때 호출
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		ctx.write(InetAddress.getLocalHost().getHostName() + " 서버에 접속 하셨습니다!\r\n");
		ctx.write("현재 시간은 " + new Date() + " 입니다.\r\n");
		// 채널에 기록된 데이터를 즉시 클라이언트로 전송
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
		
		String response;
		boolean close = false;
		
		if (request.isEmpty()) {
			response = "명령을 입력해주세요.\r\n";
		} else if ("bye".equals(request.toLowerCase())) {
			response = "안녕히 가세요!\r\n";
			close = true;
		} else {
			response = "입력하신 명령은 '" + request + "' 입니다.\r\n";
		}
		
		ChannelFuture future = ctx.write(response);
		
		// 연결된 클라이언트 채널 비동기로 닫기
		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	// channelRead0 이벤트가 완료되면 호출되는 이벤트
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
		// 채널에 기록된 데이터를 즉시 클라이언트로 전송
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		cause.printStackTrace();
		ctx.close();
	}
	
	

}
