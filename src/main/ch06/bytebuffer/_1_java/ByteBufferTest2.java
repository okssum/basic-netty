package ch06.bytebuffer._1_java;

import java.nio.ByteBuffer;

/**
 * @author  sunok
 * @Comment java ByteBuffer 예제 - 바이트 버퍼 크기보다 많은 데이터를 기록했을 때
 */
public class ByteBufferTest2 {

    public static void main(String[] args) {
        
        ByteBuffer firstBuffer = ByteBuffer.allocate(11);
        System.out.println("초기 상태 : " + firstBuffer);
        
        byte[] source = "Hello World".getBytes();
        
        for (byte item : source) {
            firstBuffer.put(item);
            System.out.println("현재 상태 : " + firstBuffer);
        }
    }

}
