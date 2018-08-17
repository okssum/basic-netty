package ch06.bytebuffer._2_netty;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class _2_ReadWriteByteBufferByNettyTest {

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
		
		// 4바이트의 정수 65537 기록. write 메서드는 기록한 데이터 크기만큼 writeIndex 속성값을 증가시킴
		buf.writeInt(65537);
		// 읽어드릴 수 있는 바이트 크기가 4인지 확인
		assertEquals(4, buf.readableBytes());
		// 기록할 수 있는 바이트 크기가 7인지 확인
		assertEquals(7, buf.writableBytes());
		
		// 2바이트 크기의 정수를 읽고, 1인지 확인
		// 65537 16진수 - 0x10001, 4바이트 패팅 - 0x00010001
		// 앞쪽 2바이트를 읽었을 때 값은 1임
		assertEquals(1, buf.readShort());
		// 읽을 수 있는 바이트 수가 2인지 확인
		// read 메서드는 읽어들인 데이터만큼 readerIndex를 감소시킴
		assertEquals(2, buf.readableBytes());
		assertEquals(7, buf.writableBytes());
		
		// 아직 읽어들일 데이터가 남았는지 확인
		// isReadable()은 쓰기 인덱스가 읽기 인덱스보다 큰지 검사함
		assertEquals(true, buf.isReadable());
		
		// 버퍼 초기화. 읽기 인덱스, 쓰기 인덱스 모두 0으로 변경
		// 남은 데이터 읽지않고 버림
		buf.clear();
		
		assertEquals(0, buf.readableBytes());
		assertEquals(11, buf.writableBytes());
	}

}
