package ch06.bytebuffer._1_java;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.Test;

/**
 * @author  sunok
 * @Comment java ByteBuffer 예제 - 정상적인 바이트 버퍼에 데이터 기록하고 조회하기 테스트
 */
public class ByteBufferTest4 {
    
    @Test
    public void test() {
        
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("초기 상태 : " + firstBuffer);
        
        firstBuffer.put((byte) 1);
        firstBuffer.put((byte) 2);
        assertEquals(2, firstBuffer.position());
        
        // position 속성 0으로 변경
        firstBuffer.rewind();
        assertEquals(0, firstBuffer.position());
        
        assertEquals(1, firstBuffer.get());
        assertEquals(1, firstBuffer.position());
        
        System.out.println(firstBuffer);
    }

}
