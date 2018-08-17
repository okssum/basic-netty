package ch06.bytebuffer._2_netty;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author  sunok
 * @Comment 부호 없는 데이터 읽기
 */
public class _4_UnsignedByteBufferTest {
	
	final String source = "Hello World";
	
	@Test
	public void unsignedBufferToJavaBuffer() {
		
		ByteBuf buf = Unpooled.buffer(11);
		
		buf.writeShort(-1);
		
		// 자바는 부호 없는 데이터가 없음. 부호 없는 데이터로 변환하려면 2배 크기의 데이터형에 저장해야 함
		// getUnsignedShort 메서드 - 원본데이터(short), 리턴데이터(int)
		// -1 16진수 - 0xFFFF. 부호없는 정수로 표현하면 65535임
		// getUnsignedShort로 0~2바이트를 읽어서 4바이트 데이터인 int로 읽어들이면 65535임
		assertEquals(65535, buf.getUnsignedShort(0));
	}

}
