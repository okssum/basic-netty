package ch06.bytebuffer._2_netty;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class _3_DynamicByteBufferTest {

	@Test
	public void createUnpooledHeapBufferTest() {
		
		// 바이트 버퍼 풀을 사용하지 않는 11 바이트 힙 버퍼 생성
		// 인수 지정 안할 경우 기본값 256 바이트
		ByteBuf buf = Unpooled.buffer(11);
		
		testBuffer(buf, false);
	}
	
	@Test
	public void createUnpooledDirectBufferTest() {
		
		// 바이트 버퍼 풀을 사용하지 않는 11 바이트 다이렉트 버퍼 생성
		ByteBuf buf = Unpooled.directBuffer(11);
		
		testBuffer(buf, true);
	}
	
	@Test
	public void createPooledHeapBufferTest() {
		
		// 풀링된 11 바이트 힙 버퍼 생성
		ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);
		
		testBuffer(buf, false);
	}
	
	@Test
	public void createPooledDirectBufferTest() {
		
		// 풀딩된 11 바이트 다이렉트 버퍼 생성
		ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);
		
		testBuffer(buf, true);
	}
	
	// 생성한 바이트버퍼가 정상인지 확인
	private void testBuffer(ByteBuf buf, boolean isDirect) {
		
		assertEquals(11, buf.capacity());
		assertEquals(isDirect, buf.isDirect());
		
		String sourceData = "hello world";
		
		buf.writeBytes(sourceData.getBytes());
		assertEquals(11, buf.readableBytes());
		assertEquals(0, buf.writableBytes());
		
		assertEquals(sourceData, buf.toString(Charset.defaultCharset()));
		
		buf.capacity(6);
		assertEquals("hello ", buf.toString(Charset.defaultCharset()));
		assertEquals(6, buf.capacity());
		
		buf.capacity(13);
		assertEquals("hello ", buf.toString(Charset.defaultCharset()));
		
		buf.writeBytes("world".getBytes());
		assertEquals(sourceData, buf.toString(Charset.defaultCharset()));
		
		assertEquals(13, buf.capacity());
		assertEquals(2, buf.writableBytes());
	}

}
