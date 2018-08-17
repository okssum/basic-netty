package ch06.bytebuffer._1_java;

import java.nio.ByteBuffer;

/**
 * @author  sunok
 * @Comment java ByteBuffer 예제 - 비정상적인 바이트 버퍼에 데이터 기록하고 조회하기 테스트
 */
public class ByteBufferTest3 {

    public static void main(String[] args) {
        
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("초기 상태 : " + firstBuffer); // 초기 상태 : java.nio.HeapByteBuffer[pos=0 lim=11 cap=11]
        
        // position 값 증가됨. 1
        firstBuffer.put((byte) 1);
        // get()도 position 값 증가되어 2. 2 position의 값은 초기화값인 0임
        System.out.println(firstBuffer.get()); // 0
        System.out.println(firstBuffer); // java.nio.HeapByteBuffer[pos=2 lim=11 cap=11]
    }

}
