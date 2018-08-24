package ch06.bytebuffer._2_netty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author  sunok
 * @Comment 네티 바이트 버퍼 <-> 자바 NIO 버퍼
 */
public class _6_ConvertedByteBufferTest {
	
	final String source = "Hello world";
	
	@Test
	public void convertNettyBufferToJavaBuffer() {
		
		// 네티 바이트 버퍼 생성
		ByteBuf buf = Unpooled.buffer(11);
		
		buf.writeBytes(source.getBytes());
		assertEquals(source, buf.toString(Charset.defaultCharset()));
		
		// 자바 바이트 버퍼 생성
		// 자바 바이트 버퍼와 네티 바이트 버퍼의 내부 배열은 서로 공유됨
		ByteBuffer nioByteBuffer = buf.nioBuffer();
		assertNotNull(nioByteBuffer);
		assertEquals(source, new String(nioByteBuffer.array(), nioByteBuffer.arrayOffset(), nioByteBuffer.remaining()));
	}
	
	@Test
	public void convertJavaBufferToNettyBuffer() {
		
		// 자바 바이트 버퍼 생성
		ByteBuffer byteBuffer = ByteBuffer.wrap(source.getBytes());
		// 네티 바이트 버퍼 생성
		// 자바 바이트 버퍼와 네티 바이트 버퍼의 내부 배열은 서로 공유됨
		// 이 같이 내부 배열을 공유하는 바이트 버퍼를 뷰 버퍼라고 함
		ByteBuf nettyBuffer = Unpooled.wrappedBuffer(byteBuffer);
		
		assertEquals(source, nettyBuffer.toString(Charset.defaultCharset()));
	}
	
}
