package ch06.bytebuffer._2_netty;

import static org.junit.Assert.assertEquals;

import java.nio.ByteOrder;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author  sunok
 * @Comment 빅엔디안 -> 리틀엔디안 변환
 */
public class _5_OrderedByteBufferTest {
	
	@Test
	public void pooledHeepBufferTest() {
		
		ByteBuf buf = Unpooled.buffer();
		// 네티 바이트 버퍼의 기본 엔디안은 자바와 동일한 빅엔디안임
		assertEquals(ByteOrder.BIG_ENDIAN, buf.order());
		
		// 숫자 1을 2바이트 Short형으로 기록
		// 기본 엔디안은 빅엔디안이므로 0x0001이 저장
		buf.writeShort(1);
		
		// 현재 바이트 버퍼의 읽기 인덱스 위치를 표시함
		buf.markReaderIndex();
		assertEquals(1, buf.readShort());
		
		// 읽기 인덱스 위치를 markReaderIndex() 사용하여 표시한 위치로 이동시킴
		buf.resetReaderIndex();
		
		// 리틀엔디안 바이트 버퍼 생성
		// 새로운 바이트 버퍼를 생성하는 것이 아니라 내용(배열, 읽기 인덱스, 쓰기 인덱스)을 공유하는 파생 바이트 버퍼 객체 생성
		ByteBuf lettleEndianBuf = buf.order(ByteOrder.LITTLE_ENDIAN);
		// 리틀엔디안에 해당하는 2바이트 Shor형 데이터를 일고 그 값이 256인지 확인
		// 빅엔디안 0x0001 -> 리틀엔디안 0x0100. 십진수 256임
		assertEquals(256, lettleEndianBuf.readShort());
	}
	
}
